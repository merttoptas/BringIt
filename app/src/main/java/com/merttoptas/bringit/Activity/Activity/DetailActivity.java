package com.merttoptas.bringit.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.merttoptas.bringit.Activity.Fragment.AccountFragment;
import com.merttoptas.bringit.Activity.Fragment.MapsFragment;
import com.merttoptas.bringit.Activity.Model.Offer;
import com.merttoptas.bringit.Activity.Model.User;
import com.merttoptas.bringit.R;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    Typeface typeface;
    FirebaseUser currentUser;
    CardView mCardView;
    TextView tvUserNameSurname, tvOfferTitle, tvTransportMethod, tvNumberOfFloors, tvProvince, tvDistrict,
            tvTargetProvince, tvTargetDistrict,tvToFloors,tvDate, tvExplanation,mTitle,mTvTarget,tvTransport;
    Button mMessageSend;
    FirebaseAuth mAuth;
    ImageView navOfferPhoto;
    FirebaseDatabase mdb;
    MapsFragment mapsFragment = new MapsFragment();
    SharedPreferences myPrefs;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){

            setTheme(R.style.darktheme);
        }else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_detail);

        //toolbar set name
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        getDelegate().setLocalNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);

        typeface = Typeface.createFromAsset(getAssets(),"fonts/rubik.ttf");
        bindViews();
        tvTransport.setTypeface(typeface);
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
        User user = new User();
        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
        intent.putExtra("namesurname" , user.getUsername());
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    public void bindViews(){

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
        mTitle = findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.ilan_detay);
        mCardView = findViewById(R.id.cardView);
        mTvTarget = findViewById(R.id.mTvTarget);
    }

}