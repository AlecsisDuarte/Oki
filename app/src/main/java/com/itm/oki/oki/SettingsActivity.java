package com.itm.oki.oki;


import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private static final int TONE_PICKER = 999;
    LinearLayout tono_layout, alerta_layout, calibrar_layout, camara_layout;
    TextView alerta_text, calibrar_text, camara_text, tono_text;
    TextInputEditText phone_text;
    Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Agregamos el boton de regresar al TitleBar
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tono_layout = (LinearLayout) findViewById(R.id.tono_layout);
        ringtone = RingtoneManager.getRingtone(this, Settings.System.DEFAULT_RINGTONE_URI);
        tono_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tonePicker();
            }
        });


        alerta_layout = (LinearLayout) findViewById(R.id.alert_layout);

        calibrar_layout = (LinearLayout) findViewById(R.id.calibrar_layout);
        camara_layout = (LinearLayout) findViewById(R.id.camara_layout);

        phone_text = (TextInputEditText) findViewById(R.id.phone_text);
        alerta_text = (TextView) findViewById(R.id.alerta_text);
        calibrar_text = (TextView) findViewById(R.id.calibrar_text);
        camara_text = (TextView) findViewById(R.id.camara_text);
        tono_text = (TextView) findViewById(R.id.tono_text);
        tono_text.setText(ringtone.getTitle(this));
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);

    }

    public void tonePicker(){
        final Uri currentTone= RingtoneManager.getActualDefaultRingtoneUri(SettingsActivity.this, RingtoneManager.TYPE_ALARM);
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        startActivityForResult(intent, TONE_PICKER);
        tono_text.setText(ringtone.getTitle(this));

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
