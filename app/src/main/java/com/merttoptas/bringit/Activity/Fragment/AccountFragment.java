package com.merttoptas.bringit.Activity.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.merttoptas.bringit.R;


public class AccountFragment extends Fragment {

    private TextView tvTasima;
    private TextView tvIlanSayisi;
    private TextView tvTasimaSayisi;
    private TextView tvMail, tvIlanlar;
    private TextView tvPhone;
    private TextView tvWebSite;
    private ImageView navUserPhoto;
    private TextView tvAdSoyad;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    ImageView ivMail,ivPhone;
    SharedPreferences myPrefs;
    SharedPreferences.Editor editor;


    //TODO dialog interface


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_account, container, false);

        tvAdSoyad = (TextView) v.findViewById(R.id.tvAdSoyad);
        tvIlanlar = v.findViewById(R.id.tvIlanlar);
        tvTasima =v.findViewById(R.id.tvTasima);
        tvIlanSayisi =v.findViewById(R.id.tvIlanSayisi);
        tvTasimaSayisi=v.findViewById(R.id.tvTasimaSayisi);
        tvMail =(TextView) v.findViewById(R.id.tvMail);
        tvPhone=v.findViewById(R.id.tvPhone);
        tvWebSite=v.findViewById(R.id.tvWebSite);
        ivMail = v.findViewById(R.id.ivMail);
        ivPhone = v.findViewById(R.id.ivPhone);
        navUserPhoto = v.findViewById(R.id.navUserPhoto);
        Button btnSetAccount = v.findViewById(R.id.btnSetAccount);


        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        tvMail.setText(myPrefs.getString("email", ""));
        tvPhone.setText(myPrefs.getString("phone", ""));
        tvWebSite.setText(myPrefs.getString("webSite", ""));


        btnSetAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogUret();
            }
        });


        return v;


    }


    private void dialogUret(){
        final Dialog d = new Dialog(getActivity());
        d.setContentView(R.layout.custom_dialog_style1);

        final EditText etMail = d.findViewById(R.id.etMailAdress);

        final EditText etPhoneNo = d.findViewById(R.id.etPhoneNo);

        final EditText etWebSite = d.findViewById(R.id.etUserWebSite);



        Button btnOlumlu = d.findViewById(R.id.btnSave_1);
        Button btnOlumsuz = d.findViewById(R.id.btnCikis_2);
        btnOlumlu.setText("Kaydet");
        btnOlumsuz.setText("Çıkış");


        btnOlumlu.setOnClickListener(new View.OnClickListener() {
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
        btnOlumsuz.setOnClickListener(new View.OnClickListener() {
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
            if (currentUser == null) {

                Toast.makeText(getActivity(), "Veriler Alınamadı!", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {

            e.fillInStackTrace();

        }
    }


}
