package rehab.reality.alsuti;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
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
    File outputDir;
    UploadActivity uploader;
    String ext;
    String password;
    String fileName;

    public Encrypter(UploadActivity uploader, Context context, String fileName, String password) throws IOException {
        this.uploader = uploader;
        this.fileName = fileName;
        this.password = password;
        am = context.getAssets();
        this.outputDir = context.getCacheDir();

        Scanner cjsScanner = new Scanner(am.open("cryptojs.js"));
        cjsString = cjsScanner.useDelimiter("\\A").next();
        cjsScanner.close();

        Scanner cliScanner = new Scanner(am.open("encrypt_file.js"));
        cliString = cliScanner.useDelimiter("\\A").next();
        cliScanner.close();
    }

    public void runEncryption() {
        byte[] b = new byte[0];
        try {
            b = FileUtils.readFileToByteArray(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String content = "YW5kcm9pZHN1Y2tz" + android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("var plain = \'" + content.replace("\n", "") + "\';");
        stringBuilder.append("var password = \'" + password + "\';");
        stringBuilder.append(cjsString);
        stringBuilder.append(cliString);

        String jsCode = stringBuilder.toString();

        ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

        Log.w("progress", "built string");

        JSContext js = new JSContext();
        try {
            js.evaluateScript(jsCode);
        } catch (JSException e) {
            e.printStackTrace();
        }
        Log.w("progress", "built ciphertext");
        String ret = "";
        try {
            ret = js.property("result").toString();
        } catch (JSException e) {
            e.printStackTrace();
        }
        Log.w("progress", "got ciphertext");

        File outputFile = null;
        try {
            outputFile = File.createTempFile("alsutiTemp", "." + ext, outputDir);
            PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
            writer.print(ret);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("error", String.valueOf(e.getStackTrace()));
        }
        Log.w("progress", "written temp file");

        Log.w("progress", "done encryption");
        uploader.doneEncryption(outputFile.getAbsolutePath());
    }
}