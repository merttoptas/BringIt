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
    TextView tvUserNameSurname, tvOfferTitle, tvTransportMethod, tvNumberOfFloors, tvProvince, tvDistrict, tvTargetProvince, tvTargetDistrict,tvToFloors,tvDate, tvExplanation,mTitle,mTvTarget,tvTransport;
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

        tvUserNameSurname = findViewById(R.id.tvUserNameSurname);
        tvOfferTitle = findViewById(R.id.tvOfferTitle);
        tvTransportMethod = findViewById(R.id.tvTransportMethod);
        tvNumberOfFloors = findViewById(R.id.tvNumberOfFloors);
        tvProvince = findViewById(R.id.tvProvince);
        tvDistrict = findViewById(R.id.tvDistrict);
        tvTargetProvince = findViewById(R.id.tvTargetProvince);
        tvTargetDistrict = findViewById(R.id.tvTargetDistrict);
        tvToFloors = findViewById(R.id.tvToFloors);
        tvDate = findViewById(R.id.TvDate);
        tvExplanation = findViewById(R.id.tvExplanation);
        tvExplanation = findViewById(R.id.tvExplanation);
        tvTransport = findViewById(R.id.tvTransport);

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
        typeface = Typeface.createFromAsset(getAssets(),"fonts/rubik.ttf");
        tvTransport.setTypeface(typeface);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/rubik.ttf");
        tvUserNameSurname.setTypeface(typeface);
        mTvTarget.setTypeface(typeface);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        getDetail();
    }

    public void getDetail() {

        tvOfferTitle.setText(getIntent().getStringExtra("title"));
        tvTransportMethod.setText(getIntent().getStringExtra("transport"));
        tvToFloors.setText(getIntent().getStringExtra("toFloors"));
        tvProvince.setText(getIntent().getStringExtra("province"));
        tvDistrict.setText(getIntent().getStringExtra("district"));
        tvTargetProvince.setText(getIntent().getStringExtra("targetProvince"));
        tvTargetDistrict.setText(getIntent().getStringExtra("targetDistrict"));
        tvNumberOfFloors.setText(getIntent().getStringExtra("numberOfFloors"));
        tvDate.setText(getIntent().getStringExtra("date"));
        tvExplanation.setText(getIntent().getStringExtra("explanation"));
        tvUserNameSurname.setText(getIntent().getStringExtra("nameSurname"));

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