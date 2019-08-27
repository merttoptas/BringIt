package com.merttoptas.bringit.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.MyOptionsPickerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.merttoptas.bringit.Activity.Fragment.AccountFragment;
import com.merttoptas.bringit.Activity.Fragment.MapsFragment;
import com.merttoptas.bringit.Activity.Fragment.MessageFragment;
import com.merttoptas.bringit.Activity.Fragment.OfferFragment;
import com.merttoptas.bringit.Activity.Model.GlobalBus;
import com.merttoptas.bringit.Activity.Model.Offer;
import com.merttoptas.bringit.R;
import com.tbuonomo.morphbottomnavigation.MorphBottomNavigationView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements MorphBottomNavigationView.OnNavigationItemSelectedListener{

    MorphBottomNavigationView bottomNavigationView;

    AccountFragment accountFragment = new AccountFragment();
    MapsFragment mapsFragment = new MapsFragment();
    MessageFragment messageFragment = new MessageFragment();
    OfferFragment offerFragment = new OfferFragment();
    public double latitude;
    public double longitude;
    AutoCompleteTextView etIl, etIlce, etToİl, etToIlce;
    TextView etEsyaSekli, etKatSayisi, etKat;
    EditText etBaslik, etdateTime, etAciklama;
    Button btnOfferSave;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_maps);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        //Resource of Items
        etBaslik = findViewById(R.id.etBaslik);
        etEsyaSekli = findViewById(R.id.etEsyaSekli);
        etKatSayisi = findViewById(R.id.etKatSayisi);
        etIl = findViewById(R.id.etIl);
        etIlce = findViewById(R.id.etIlce);
        etToİl = findViewById(R.id.etToİl);
        etKat = findViewById(R.id.etKat);
        etToIlce = findViewById(R.id.etToIlce);
        btnOfferSave = findViewById(R.id.btnSave);
        etdateTime = findViewById(R.id.etdateTime);
        etAciklama = findViewById(R.id.etAciklama);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case  R.id.navigation_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, accountFragment).commit();

                return  true;

            case R.id.navigation_offer:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, offerFragment).commit();

                return  true;
            case R.id.navigation_maps:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mapsFragment).commit();
                return true;

            case  R.id.navigation_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, messageFragment).commit();

                return true;

        }
        return false;
    }



}
