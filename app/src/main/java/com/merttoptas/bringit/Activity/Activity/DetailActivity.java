package com.merttoptas.bringit.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.merttoptas.bringit.Activity.Fragment.MapsFragment;
import com.merttoptas.bringit.Activity.Model.Offer;
import com.merttoptas.bringit.R;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    Typeface typeface;
    FirebaseUser currentUser;
    Toolbar toolbar;
    CardView mCardView;
    TextView mTitle, mTvTarget, mTvBaslik, mTvEsyaSekli, mTvKatSayisi, mTvetIl, mTtvIlce, mTvToIl, mTvToIlce, mTvKat, mTvDate, tvAciklama, mtvNameSurname;
    Button mMessageSend;
    FirebaseAuth mAuth;
    ImageView navOfferPhoto;
    private List<Object> list;
    FirebaseDatabase mdb;
    MapsFragment mapsFragment = new MapsFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mTvBaslik = findViewById(R.id.tvBaslik);
        mTvEsyaSekli = findViewById(R.id.tvEsyaSekli);
        mTvKatSayisi = findViewById(R.id.tvKatSayisi);
        mTvetIl = findViewById(R.id.tvetIl);
        mTtvIlce = findViewById(R.id.mTtvIlce);
        mTvToIl = findViewById(R.id.mTvToIl);
        mTvToIlce = findViewById(R.id.mTvToIlce);
        mTvKat = findViewById(R.id.mTvKat);
        mTvDate = findViewById(R.id.mTvDate);
        tvAciklama = findViewById(R.id.tvAciklama);
        mtvNameSurname = findViewById(R.id.mtvNameSurname);
        mMessageSend = findViewById(R.id.btnMessage);
        navOfferPhoto = findViewById(R.id.navOfferPhoto);
        //toolbar set name
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle = findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.ilan_detay);

        mCardView = findViewById(R.id.cardView);
        mTvTarget = findViewById(R.id.mTvTarget);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/rubik.ttf");
        mTvTarget.setTypeface(typeface);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        getDetail();
    }

    public void getDetail() {

        mTvBaslik.setText(getIntent().getStringExtra("baslik"));
        mTvEsyaSekli.setText(getIntent().getStringExtra("esyaS"));
        mTvetIl.setText(getIntent().getStringExtra("esyaIl"));
        mTtvIlce.setText(getIntent().getStringExtra("esyaIlce"));
        mTvToIl.setText(getIntent().getStringExtra("esyaToIl"));
        mTvToIlce.setText(getIntent().getStringExtra("esyaIlce"));
        mTvKatSayisi.setText(getIntent().getStringExtra("katSayisi"));
        mTvDate.setText(getIntent().getStringExtra("date"));
        tvAciklama.setText(getIntent().getStringExtra("aciklama"));
        mTvKat.setText(getIntent().getStringExtra("kat"));
        mtvNameSurname.setText(getIntent().getStringExtra("nameSurname"));

    }

    public void sendMessage(View view) {
        Intent i = new Intent(getApplicationContext(), MessageActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}