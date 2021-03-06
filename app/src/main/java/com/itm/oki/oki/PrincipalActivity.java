package com.itm.oki.oki;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class PrincipalActivity extends AppCompatActivity {
    private ImageButton eyeButton, settingsButton;
    public boolean started = true;
    private FloatingActionButton preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_layout);

        eyeButton = (ImageButton) findViewById(R.id.eye_button);
        eyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButton();
            }
        });

        settingsButton = (ImageButton) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettings();
            }
        });

        preview = (FloatingActionButton) findViewById(R.id.preview_button);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreview();
            }
        });

    }

    private void changeButton(){
        if(started){
            eyeButton.setImageResource(R.drawable.eye_close_svg);
            started = false;
        }else{
            started = true;
            eyeButton.setImageResource(R.drawable.eye_open_svg);
        }
    }

    private void goToSettings(){
        Intent settingsActivity = new Intent(this, SettingsActivity.class);
        startActivity(settingsActivity);

    }

    private void openPreview(){
        Intent previewActivity = new Intent(this, GooglyEyesActivity.class);
        startActivity(previewActivity);
    }


}
