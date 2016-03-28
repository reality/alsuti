package rehab.reality.alsuti;

import android.content.Context;
import android.content.res.AssetManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by reality on 3/28/16.
 */
public class Encrypter {
    AssetManager am;
    String jsCode;
    Context context;

    public Encrypter(Context context) throws IOException {
        this.context = context;
        am = context.getAssets();

        Scanner cjsScanner = new Scanner(am.open("cryptojs.js"));
        String cjsString = cjsScanner.useDelimiter("\\A").next();
        cjsScanner.close();

        Scanner cliScanner = new Scanner(am.open("encrypt_file.js"));
        String cliString = cliScanner.useDelimiter("\\A").next();
        cliScanner.close();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cjsString);
        stringBuilder.append(cliString);

        jsCode = stringBuilder.toString();
    }

    public void encryptFile(String fileName, String password, final JsCallback superCallback) throws IOException {
        RandomAccessFile f = new RandomAccessFile(fileName, "r");
        byte[] b = new byte[(int)f.length()];
        f.read(b);

        String content = "YW5kcm9pZHN1Y2tz" + android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);

        final String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

        JsEvaluator jsEvaluator = new JsEvaluator(context);

        try {
            jsEvaluator.callFunction(jsCode, new JsCallback() {
                @Override
                public void onResult(String cipherText) {
                    File outputDir = context.getCacheDir(); // context being the Activity pointer
                    File outputFile = null;
                    try {
                        outputFile = File.createTempFile("alsutiTemp", "." + ext, outputDir);
                        PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
                        writer.print(cipherText);
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    superCallback.onResult(outputFile.getAbsolutePath());
                }
            }, "encrypt", content, password);
        } catch(Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}