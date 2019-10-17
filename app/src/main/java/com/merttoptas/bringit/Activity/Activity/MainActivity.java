package com.merttoptas.bringit.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.merttoptas.bringit.Activity.Fragment.AccountFragment;
import com.merttoptas.bringit.Activity.Fragment.MapsFragment;
import com.merttoptas.bringit.Activity.Fragment.MessageFragment;
import com.merttoptas.bringit.Activity.Fragment.OfferFragment;
import com.merttoptas.bringit.R;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    AccountFragment accountFragment = new AccountFragment();
    MapsFragment mapsFragment = new MapsFragment();
    MessageFragment messageFragment = new MessageFragment();
    OfferFragment offerFragment = new OfferFragment();
    Typeface typeface;
    SharedPreferences myPrefs;
    Switch mySwitch;
    DatabaseReference ref;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    AdView mAdView;
    private Activity mActivity;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("nightMode", Context.MODE_PRIVATE);
        boolean switchState = prefs.getBoolean("nightOpen", false);

        if(switchState){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.container, accountFragment).commit();
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.container, mapsFragment).commit();
        }
        status("offline");
        bottomNavigationView.setSelectedItemId(R.id.navigation_maps);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view_linear);
        bottomNavigationView.setSelectedItemId(R.id.navigation_maps);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/rubik.ttf");
        mySwitch = findViewById(R.id.mySwitch);
        Trace myTrace = FirebasePerformance.getInstance().newTrace("test_trace");
        myTrace.start();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.navigation_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, accountFragment,"accountFragment")
                        .commit();
                return  true;
            case R.id.navigation_offer:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, offerFragment, "offerFragment").commit();
                return  true;

            case R.id.navigation_maps:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mapsFragment, "mapsFragment").commit();
                return true;
            case R.id.navigation_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, messageFragment,"messageFragment")
                        .commit();
                return true;
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){

            int bottomNavigationColor = Color.parseColor("#424242");
            bottomNavigationView.setBackgroundColor(bottomNavigationColor);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            int bottomNavigationColor = Color.parseColor("#ffffffff");
            bottomNavigationView.setBackgroundColor(bottomNavigationColor);
        }
        status("online");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFinishing();

    }

    private void status(String status){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", status);
            ref.updateChildren(hashMap);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
