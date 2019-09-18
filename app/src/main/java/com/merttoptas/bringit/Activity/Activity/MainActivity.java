package com.merttoptas.bringit.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.merttoptas.bringit.Activity.Fragment.AccountFragment;
import com.merttoptas.bringit.Activity.Fragment.MapsFragment;
import com.merttoptas.bringit.Activity.Fragment.MessageFragment;
import com.merttoptas.bringit.Activity.Fragment.OfferFragment;
import com.merttoptas.bringit.R;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    AccountFragment accountFragment = new AccountFragment();
    MapsFragment mapsFragment = new MapsFragment();
    MessageFragment messageFragment = new MessageFragment();
    OfferFragment offerFragment = new OfferFragment();
    public double latitude;
    public double longitude;
    AutoCompleteTextView etIl, etIlce, etToİl, etToIlce;
    TextView etEsyaSekli, etKatSayisi, etKat;
    EditText etBaslik, etAciklama;
    Button btnOfferSave;
    Typeface typeface;


    @Override
    protected void onStart() {
        super.onStart();

        bottomNavigationView.setSelectedItemId(R.id.navigation_maps);

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, accountFragment).commit();
        }else if(AppCompatDelegate.getDefaultNightMode() ==AppCompatDelegate.MODE_NIGHT_NO){
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, accountFragment).commit();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        bottomNavigationView = findViewById(R.id.bottom_navigation_view_linear);
        bottomNavigationView.setSelectedItemId(R.id.navigation_maps);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/rubik.ttf");

        //Resource of Items
        etBaslik = findViewById(R.id.mTvBaslik);
        etEsyaSekli = findViewById(R.id.mTvEsyaSekli);
        etKatSayisi = findViewById(R.id.mTvKatSayisi);
        etIl = findViewById(R.id.mTvetIl);
        etIlce = findViewById(R.id.mTvIlce);
        etToİl = findViewById(R.id.mTvToİl);
        etKat = findViewById(R.id.mTvKat);
        etToIlce = findViewById(R.id.mTvToIlce);
        btnOfferSave = findViewById(R.id.btnSend);
        etAciklama = findViewById(R.id.mTvAciklama);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.navigation_account:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, accountFragment).commit();
                return  true;
            case R.id.navigation_offer:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, offerFragment).commit();
                return  true;

            case R.id.navigation_maps:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, mapsFragment).commit();
                return true;
            case R.id.navigation_message:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, messageFragment).commit();
                return true;
        }

        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;



        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            int bubleButtomcolor = Color.parseColor("#424242");
            bottomNavigationView.setBackgroundColor(bubleButtomcolor);

        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            int bubleBottomColor = Color.parseColor("#ffffffff");
            bottomNavigationView.setBackgroundColor(bubleBottomColor);
        }

    }

}
