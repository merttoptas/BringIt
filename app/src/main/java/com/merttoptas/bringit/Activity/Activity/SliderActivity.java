package com.merttoptas.bringit.Activity.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.merttoptas.bringit.Activity.Adapter.IntroViewPagerAdapter;
import com.merttoptas.bringit.Activity.Model.ScreenItem;
import com.merttoptas.bringit.R;

import java.util.ArrayList;
import java.util.List;

public class SliderActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnGetStarted;
    TextView tvSkip;
    final  List<ScreenItem> mList = new ArrayList<>();
    Animation btnAnim ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make the activity  on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // when this activity is about to be launch we need to check if its opened before or not

        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class );
            startActivity(mainActivity);
            finish();


        }
        setContentView(R.layout.activity_slider);

        bindViews();
        final  List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem(getString(R.string.app_name), getString(R.string.tv_slider), R.drawable.slider1));
        mList.add(new ScreenItem(getString(R.string.ilan_yayınla), getString(R.string.tv_slider1), R.drawable.slider3));
        mList.add(new ScreenItem(getString(R.string.teklif_ver), getString(R.string.tv_slider2), R.drawable.slider6));

        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);
        tabIndicator.setupWithViewPager(screenPager);

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {

                    loadLastScreen();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void loadLastScreen() {

        // show the GETSTARTED Button and hide the indicator and the next button
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // setup animation
       btnGetStarted.setAnimation(btnAnim);

    }

    public void getStarted(View view) {

        //open main activity

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        // also we need to save a boolean value to storage so next time when the user run the app
        // we could know that he is already checked the intro screen activity
        // i'm going to use shared preferences to that process
        savePrefsData();
        finish();

    }
    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        return pref.getBoolean("isIntroOpened",false);

    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened",true);
        editor.apply();

    }

    private void bindViews(){
        tabIndicator =findViewById(R.id.tab_indicator);
        screenPager = findViewById(R.id.screen_viewpager);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tvSkip = findViewById(R.id.tv_skip);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

    }

}
