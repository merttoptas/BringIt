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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.merttoptas.bringit.Activity.Model.User;
import com.merttoptas.bringit.R;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {
    Typeface typeface;
    FirebaseUser currentUser;
    CardView mCardView;
    TextView tvUserNameSurname, tvOfferTitle, tvTransportMethod, tvNumberOfFloors, tvProvince, tvDistrict,
            tvTargetProvince, tvTargetDistrict,tvToFloors,tvDate, tvExplanation,mTitle,mTvTarget,tvTransport;
    Button mMessageSend;
    FirebaseAuth mAuth;
    CircleImageView navOfferPhoto;
    SharedPreferences myPrefs;
    DatabaseReference ref;
    Intent intent;
    Animation btnAnim ;
    CardView cardView, cardView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){

            setTheme(R.style.darktheme);
        }else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_detail);

        bindViews();
        //toolbar set name
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        getDelegate().setLocalNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);

        typeface = Typeface.createFromAsset(getAssets(),"fonts/rubik.ttf");
        tvTransport.setTypeface(typeface);
        tvUserNameSurname.setTypeface(typeface);
        mTvTarget.setTypeface(typeface);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String userid = getIntent().getStringExtra("useridRw");

        if(currentUser.getUid().equals(userid)){
            mMessageSend.setVisibility(View.INVISIBLE);
        }else{
            mMessageSend.setAnimation(btnAnim);
        }
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

        final String userid = getIntent().getStringExtra("useridRw");
        ref = FirebaseDatabase.getInstance().getReference("Users");
        // Users in position get image photo
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:  dataSnapshot.getChildren()){

                    User user = snapshot.getValue(User.class);

                    if(user.getId().equals(userid)){

                        String imageUrl = user.getImageURL();
                        Glide.with(getApplicationContext()).load(imageUrl).into(navOfferPhoto);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
        String userid = getIntent().getStringExtra("useridRw");
        Log.d("useridRw1", "useridRw1: " + userid);


        intent.putExtra("useridRw1", userid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition (0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition (0, 0);            return true;
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
        cardView1 = findViewById(R.id.cardView1);

        //Animations
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        mCardView.setAnimation(btnAnim);
        cardView1.setAnimation(btnAnim);

    }


}