package com.itm.oki.oki;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class splashLogo extends AppCompatActivity {


    private boolean mVisible;
    private ImageView gif;
    private static int SPLASH_TIME = 2400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_logo_layout);

        gif = (ImageView)findViewById(R.id.eye_gif);
        Glide.with(this).load(R.drawable.oky_animation).into(gif);

        mVisible = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splashLogo.this, PrincipalActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME);

    }
}
