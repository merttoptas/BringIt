package com.merttoptas.bringit.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.merttoptas.bringit.Activity.Fragment.MapsFragment;
import com.merttoptas.bringit.R;

public class MapsActivity extends FragmentActivity  {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content,new MapsFragment());
        fragmentTransaction.commit();
    }

}

