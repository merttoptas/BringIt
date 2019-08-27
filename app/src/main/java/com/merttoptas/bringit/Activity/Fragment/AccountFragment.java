package com.merttoptas.bringit.Activity.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.merttoptas.bringit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private TextView tvTasima;
    private TextView tvIlanSayisi;
    private TextView tvTasimaSayisi;
    private TextView tvMail;
    private TextView tvPhone;
    private TextView tvWebSite;
    private ImageView ivWebSite;

    //TODO dialog interface

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_account, container, false);

        TextView tvAdSoyad = (TextView) v.findViewById(R.id.tvAdSoyad);
        TextView tvIlanlar = v.findViewById(R.id.tvIlanlar);
        tvTasima =v.findViewById(R.id.tvTasima);
        tvIlanSayisi =v.findViewById(R.id.tvIlanSayisi);
        tvTasimaSayisi=v.findViewById(R.id.tvTasimaSayisi);
        tvMail = v.findViewById(R.id.tvMail);
        tvPhone=v.findViewById(R.id.tvPhone);
        tvWebSite=v.findViewById(R.id.tvWebSite);

        ImageView ivMail = v.findViewById(R.id.ivMail);
        ImageView ivPhone = v.findViewById(R.id.ivPhone);
        ivWebSite = v.findViewById(R.id.ivWebSite);
        Button btnSetAccount = v.findViewById(R.id.btnSetAccount);



        return v;


    }


}
