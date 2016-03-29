package rehab.reality.alsuti;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.liquidplayer.webkit.javascriptcore.JSContext;
import org.liquidplayer.webkit.javascriptcore.JSException;

/**
 * Created by reality on 3/28/16.
 */
public class Encrypter {
    AssetManager am;
    String cjsString;
    String cliString;
    Context context;

    public Encrypter(Context context) throws IOException {
        this.context = context;
        am = context.getAssets();

        Scanner cjsScanner = new Scanner(am.open("cryptojs.js"));
        cjsString = cjsScanner.useDelimiter("\\A").next();
        cjsScanner.close();

        Scanner cliScanner = new Scanner(am.open("encrypt_file.js"));
        cliString = cliScanner.useDelimiter("\\A").next();
        cliScanner.close();

    }

    public String encryptFile(String fileName, String password) throws IOException, JSException {
        byte[] b = FileUtils.readFileToByteArray(new File(fileName));

        String content = "YW5kcm9pZHN1Y2tz" + android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);

        final String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("var plain = \'" + content.replace("\n", "") + "\';");
        stringBuilder.append("var password = \'" + password + "\';");
        stringBuilder.append(cjsString);
        stringBuilder.append(cliString);

        String jsCode = stringBuilder.toString();

        JSContext js = new JSContext();

        js.evaluateScript(jsCode);

        String cipherText = js.property("result").toString();

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

        return outputFile.getAbsolutePath();
    }
}