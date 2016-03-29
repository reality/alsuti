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
    Context context;
    UploadActivity uploader;
    String ext;

    public Encrypter(UploadActivity uploader, Context context) throws IOException {
        this.context = context;
        this.uploader = uploader;
        am = context.getAssets();

        Scanner cjsScanner = new Scanner(am.open("cryptojs.js"));
        cjsString = cjsScanner.useDelimiter("\\A").next();
        cjsScanner.close();

        Scanner cliScanner = new Scanner(am.open("encrypt_file.js"));
        cliString = cliScanner.useDelimiter("\\A").next();
        cliScanner.close();

    }

    public void encryptFile(String fileName, String password) throws IOException, JSException {
        byte[] b = FileUtils.readFileToByteArray(new File(fileName));

        String content = "YW5kcm9pZHN1Y2tz" + android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("var plain = \'" + content.replace("\n", "") + "\';");
        stringBuilder.append("var password = \'" + password + "\';");
        stringBuilder.append(cjsString);
        stringBuilder.append(cliString);

        String jsCode = stringBuilder.toString();

        ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

        new RunEncryption(this, jsCode, password).execute();
        Log.w("progress", "started encryption");
    }

    void done(String cipherText) {
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

        Log.w("progress", "got the ciphertext and written!");

        uploader.doneEncryption(outputFile.getAbsolutePath());
    }

    private class RunEncryption extends AsyncTask <String, String, String> {
        Encrypter encrypter;
        String password;
        String jsCode;

        public RunEncryption(Encrypter enc, String jsCode, String password) {
            encrypter = enc;
            this.password = password;
            this.jsCode = jsCode;
        }

        @Override
        protected String doInBackground(String... params) {
            JSContext js = new JSContext();
            try {
                js.evaluateScript(jsCode);
            } catch (JSException e) {
                e.printStackTrace();
            }
            String ret = "";
            try {
                ret = js.property("result").toString();
            } catch (JSException e) {
                e.printStackTrace();
            }

            Log.w("progress", "done encryption");
            return ret;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.w("progress", "calling callback");
            encrypter.done(result);
        }
    }
}