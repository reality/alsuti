package rehab.reality.alsuti;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlsutiActivity extends AppCompatActivity {

    SharedPreferences prefs;
    Activity activity;
    EditText apiEndpoint;
    EditText apiKey;
    CheckBox useTor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_alsuti);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            apiEndpoint = (EditText) findViewById(R.id.editApiEndpointText);
            apiKey = (EditText) findViewById(R.id.editApiKeyText);

            apiEndpoint.setText(this.prefs.getString("apiEndpoint", ""));
            apiKey.setText(this.prefs.getString("apiKey", ""));

            useTor = (CheckBox) findViewById(R.id.useTor);
            useTor.setChecked(this.prefs.getBoolean("useTor", false));
    }

    public void onClickBtn(View v) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("apiEndpoint", apiEndpoint.getText().toString());
        editor.putString("apiKey", apiKey.getText().toString());
        editor.putBoolean("useTor", useTor.isChecked());
        editor.commit();
        Toast.makeText(this, "Settings saved", Toast.LENGTH_LONG).show();
    }
}