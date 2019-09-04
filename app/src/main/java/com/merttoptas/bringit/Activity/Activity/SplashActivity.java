package com.merttoptas.bringit.Activity.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.merttoptas.bringit.R;

public class SplashActivity extends AppCompatActivity {

    TextView tvSplash;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        tvSplash = findViewById(R.id.tvSplash);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/Billabong.ttf");
        tvSplash.setTypeface(typeface);

        getSupportActionBar().hide();

        Animation mySplashAnim = AnimationUtils.loadAnimation(this, R.anim.splash_transition);
        tvSplash.startAnimation(mySplashAnim);

        new starting().start();

    }

    private class starting extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            }catch (Exception e){
                e.printStackTrace();
            }
            finally {

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

            }
        }
    }
}
