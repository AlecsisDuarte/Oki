package com.itm.oki.oki;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class principal extends AppCompatActivity {
    private ImageButton eyeButton;
    public boolean started = true;

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

    }

    private void changeButton(){
        if(started){
            eyeButton.setImageResource(R.mipmap.eye_close);
            started = false;
        }else{
            started = true;
            eyeButton.setImageResource(R.mipmap.eye_open);
        }
    }


}
