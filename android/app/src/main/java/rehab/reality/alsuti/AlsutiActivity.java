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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlsutiActivity extends AppCompatActivity {

    SharedPreferences prefs;
    Activity activity;
    EditText apiEndpoint;
    EditText apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_alsuti);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            apiEndpoint = (EditText) findViewById(R.id.editApiEndpointText);
            apiKey = (EditText) findViewById(R.id.editApiKeyText);

            apiEndpoint.setText(this.prefs.getString("apiEndpoint", ""));
            apiKey.setText(this.prefs.getString("apiKey", ""));

    }

    public void onClickBtn(View v) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("apiEndpoint", apiEndpoint.getText().toString());
        editor.putString("apiKey", apiKey.getText().toString());
        editor.commit();
        Toast.makeText(this, "Settings saved", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alsuti, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}