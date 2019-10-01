package com.merttoptas.bringit.Activity.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.merttoptas.bringit.Activity.Activity.DetailActivity;
import com.merttoptas.bringit.R;


public class AccountFragment extends Fragment {

    private TextView tvTasima;
    private TextView tvIlanSayisi;
    private TextView tvTasimaSayisi;
    private TextView tvMail, tvIlanlar;
    private TextView tvPhone;
    private TextView tvWebSite;
    private ImageView navUserPhoto;
    private TextView tvUserNameSurname;
    private ImageView ivNightMode;
    private Switch mySwitch;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ImageView ivMail,ivPhone;
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor editor;
    private ConstraintLayout constraintLayout3;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){

            getActivity().setTheme(R.style.darktheme);
        }else {

            getActivity().setTheme(R.style.AppTheme);
        }
        View v= inflater.inflate(R.layout.fragment_account, container, false);

        tvUserNameSurname = v.findViewById(R.id.tvUserNameSurname);
        tvIlanlar = v.findViewById(R.id.tvIlanlar);
        tvTasima =v.findViewById(R.id.tvTasima);
        tvIlanSayisi =v.findViewById(R.id.tvIlanSayisi);
        tvTasimaSayisi=v.findViewById(R.id.tvTasimaSayisi);
        tvMail = v.findViewById(R.id.tvMail);
        tvPhone=v.findViewById(R.id.tvPhone);
        tvWebSite=v.findViewById(R.id.tvWebSite);
        ivMail = v.findViewById(R.id.ivMail);
        ivPhone = v.findViewById(R.id.ivPhone);
        navUserPhoto = v.findViewById(R.id.navOfferPhoto);
        ivNightMode = v.findViewById(R.id.ivNightMode);
        mySwitch = v.findViewById(R.id.mySwitch);
        constraintLayout3 = v.findViewById(R.id.constraintLayout);
        Button btnSetAccount = v.findViewById(R.id.btnSetAccount);

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            mySwitch.setChecked(true);
        }

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        myPrefs =getActivity().getSharedPreferences("nightMode",Context.MODE_PRIVATE );
        editor = myPrefs.edit();
        mySwitch.setChecked(myPrefs.getBoolean("nightOpen", false));
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("nightOpen", true);

                }else{
                    editor.putBoolean("nightOpen", false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                editor.apply();
            }
        });



        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        setAccountUser();
        updateUser();
        btnSetAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createDialog();
            }
        });

        return v;


    }

    private void createDialog(){
        final Dialog d = new Dialog(getActivity());
        d.setContentView(R.layout.custom_dialog_style1);

        final EditText etMail = d.findViewById(R.id.etMailAdress);

        final EditText etPhoneNo = d.findViewById(R.id.etPhoneNo);

        final EditText etWebSite = d.findViewById(R.id.etUserWebSite);

        // dialog u var firebaseden gelen verilerle doldurduk

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(tvMail.getText().toString().matches("")){
            etMail.setText(currentUser.getEmail());

        }else {
            etMail.setText(myPrefs.getString("email",""));
        }
        etPhoneNo.setText(myPrefs.getString("phone",""));
        etWebSite.setText(myPrefs.getString("webSite",""));

        Button btnPositive = d.findViewById(R.id.btnSave_1);
        Button btnNegative = d.findViewById(R.id.btnCikis_2);
        btnPositive.setText(getString(R.string.kaydet));
        btnNegative.setText(getString(R.string.kapat));


        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvMail.setText(etMail.getText().toString());
                tvPhone.setText(etPhoneNo.getText().toString());
                tvWebSite.setText(etWebSite.getText().toString());

                myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                editor =myPrefs.edit();

                editor.putString("email", etMail.getText().toString());
                editor.putString("phone", etPhoneNo.getText().toString());
                editor.putString("webSite", etWebSite.getText().toString());
                editor.apply();

                d.dismiss();

            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.setCancelable(false); // Boş bir kısıma tıklanınca kapatamasın.
        //dismiss ederse kapansın
        d.show();
    }

    @Override
    public void onStart() {
        super.onStart();


        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();

            Log.d("dbCurrentUser", "currentUser" + currentUser.getDisplayName() +currentUser.getEmail());

        } catch (Exception e) {

            e.fillInStackTrace();

        }
    }

    private void updateUser(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());

        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }

        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
            }
        }

        try {

            if(tvMail.getText().toString().matches("")){
                tvMail.setText(currentUser.getEmail());

            }else{
                tvMail.setText(currentUser.getEmail());

            }
            if(tvPhone.getText().toString().matches("")){

                tvPhone.setText(currentUser.getPhoneNumber());

            }else {
                Log.d("tvPhone", tvPhone.getText().toString());
            }

            tvUserNameSurname.setText(currentUser.getDisplayName().toUpperCase());
        }
        catch (Exception e){
            e.printStackTrace();

        }

        String facebookUserId = "";

        for (UserInfo profile : user.getProviderData()) {
            // check if the provider id matches "facebook.com"
            if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {

                facebookUserId = profile.getUid();

                String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
                Log.d("fbİmg", "fbİmgSuccess : " + photoUrl);

                Glide.with(this).load(photoUrl).centerCrop().into(navUserPhoto);
            } else if (GoogleAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                String personPhoto = acct.getPhotoUrl().toString();

                Glide.with(this).load(personPhoto).centerCrop().into(navUserPhoto);

                Log.d("imgUrl", "signInWithCredential:success: " + personPhoto);
                Uri deneme = Uri.parse(personPhoto);


            }

        }

    }

    private void setAccountUser(){

        currentUser = mAuth.getCurrentUser();

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(tvMail.getText().toString().matches("")){

            Log.d("tvMail", tvMail.getText().toString());

        }else {
            tvMail.setText(myPrefs.getString("email", ""));
        }
        if (tvPhone.getText().toString().matches("")){

            tvPhone.setText(currentUser.getPhoneNumber());
        }else {
            tvPhone.setText(myPrefs.getString("phone", ""));
        }

        tvWebSite.setText(myPrefs.getString("webSite", ""));
        tvIlanSayisi.setText(myPrefs.getString("offerNumber","0"));
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    public void onButtonPressed(int i) {

    }

}
