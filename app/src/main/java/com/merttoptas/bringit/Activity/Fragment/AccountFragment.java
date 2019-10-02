package com.merttoptas.bringit.Activity.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
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


public class AccountFragment extends Fragment {

    private TextView tvIlanSayisi;
    private TextView tvMail, tvIlanlar;
    private TextView tvPhone;
    private TextView tvWebSite;
    private CircleImageView navUserPhoto;
    private TextView tvUserNameSurname;
    private ImageView ivNightMode;
    private Switch mySwitch;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ImageView ivMail,ivPhone;
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor editor;
    Context mContext;
    DatabaseReference dbRef;
    private Activity mActivity;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAccountUser();

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
        TextView tvTasima = v.findViewById(R.id.tvTasima);
        tvIlanSayisi =v.findViewById(R.id.tvIlanSayisi);
        TextView tvTasimaSayisi = v.findViewById(R.id.tvTasimaSayisi);
        tvMail = v.findViewById(R.id.tvMail);
        tvPhone=v.findViewById(R.id.tvPhone);
        tvWebSite=v.findViewById(R.id.tvWebSite);
        ivMail = v.findViewById(R.id.ivMail);
        ivPhone = v.findViewById(R.id.ivPhone);
        navUserPhoto = v.findViewById(R.id.navOfferPhoto);
        ivNightMode = v.findViewById(R.id.ivNightMode);
        mySwitch = v.findViewById(R.id.mySwitch);
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

        updateUser();
        btnSetAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createDialog();
            }
        });

        dbRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        try {
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(isAdded()){
                        User user = dataSnapshot.getValue(User.class);
                        assert user != null;
                        tvUserNameSurname.setText(user.getUsername());
                        if(user.getImageURL().isEmpty()){
                            Log.d("ImageUrl", "ImgURl: " + user.getImageURL());

                        }else {
                            if(user.getImageURL().equals("default")){
                                navUserPhoto.setImageResource(R.drawable.userphoto);
                            }else{
                                String userImg=user.getImageURL();
                                Log.d("ImageUrl", "ImgURl: " + userImg);
                                Glide.with(mActivity).load(userImg).centerCrop().into(navUserPhoto);
                            }
                        }
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
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

        }
        catch (Exception e){
            e.printStackTrace();
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

        mContext =null;
        mActivity = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();


    }

}