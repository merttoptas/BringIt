package com.merttoptas.bringit.Activity.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.pickerview.MyOptionsPickerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.merttoptas.bringit.Activity.Model.CityList;
import com.merttoptas.bringit.Activity.Model.DistrictList;
import com.merttoptas.bringit.Activity.Model.GlobalBus;
import com.merttoptas.bringit.Activity.Model.Offer;
import com.merttoptas.bringit.R;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class OfferFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    TextView etEsyaSekli, etKatSayisi, etKat;
    EditText etBaslik, etAciklama;
    Button btnOfferSave;
    AutoCompleteTextView etIl, etIlce, etToIl, etToIlce;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    public double latitude;
    public double longitude;
    LocationManager locationManager;
    ScrollView scrollView;
    Toolbar toolbar;
    TextView mTitle;
    Typeface typeface;
    SharedPreferences myPrefs;
    SharedPreferences.Editor editor;
    TextView tvIlanSayisi;
    int offerNumber =0;


    public OfferFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        etEsyaSekli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initEsyaSekli();

            }
        });

        etKatSayisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initKatSayisi();
            }
        });

        etKat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initKat();
            }
        });

        btnOfferSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfferSave();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_offer, container, false);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Resource of Items
        etBaslik = v.findViewById(R.id.mTvBaslik);
        etEsyaSekli = v.findViewById(R.id.mTvEsyaSekli);
        etKatSayisi = v.findViewById(R.id.mTvKatSayisi);
        etIl = v.findViewById(R.id.mTvetIl);
        etIlce = v.findViewById(R.id.mTvIlce);
        etToIl = v.findViewById(R.id.mTvToİl);
        etKat = v.findViewById(R.id.mTvKat);
        etToIlce = v.findViewById(R.id.mTvToIlce);
        btnOfferSave = v.findViewById(R.id.btnSend);
        scrollView = v.findViewById(R.id.scrollView);
        etAciklama = v.findViewById(R.id.mTvAciklama);
        tvIlanSayisi = v.findViewById(R.id.tvIlanSayisi);

        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SourceSansPro-Regular.ttf");
        btnOfferSave.setTypeface(typeface);

        toolbar = v.findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(toolbar.getTitle());
        mTitle.setText(R.string.ilan_ver);

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));

        if (currentUser != null) {

            connectUser();
        }
        //Firestore database db
        FirebaseStorage db = FirebaseStorage.getInstance();

        //City and district get a name
        initEsyaSekli();
        District();
        citylist();



        return  v;
    }


    @Override
    public void onStart() {
        super.onStart();

        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {


                if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this.getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    LocationManager lm = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
                    assert lm != null;
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    return;
                }

            }

        } catch (Exception e) {

            e.fillInStackTrace();

        }
    }
    @Override
    public void onPause() {
        super.onPause();

        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initEsyaSekli(){

        final String [] initTasimaSekli = getResources().getStringArray(R.array.tasinmaSekli);

        final  ArrayList<String> tasimaList = new ArrayList<String>(Arrays.asList(initTasimaSekli));

        final  MyOptionsPickerView<String>  pTasima = new MyOptionsPickerView<>(getActivity());


        pTasima.setPicker(tasimaList);
        pTasima.setSubmitButtonText(R.string.tamam);
        pTasima.setCancelButtonText(R.string.kapat);
        pTasima.setTitle(getString(R.string.kat_sayisi));
        pTasima.setCyclic(false);
        pTasima.setSelectOptions(0);
        pTasima.setSelectOptions(0);


        pTasima.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                try {
                    etEsyaSekli.setText(tasimaList.get(options1).toUpperCase());

                }catch (Exception e){
                    e.fillInStackTrace();
                }
            }
        });

        etEsyaSekli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pTasima.show();
            }
        });

    }


    private void initKatSayisi(){

        final String [] initkatSayisi = getResources().getStringArray(R.array.katSayisi);

        final  ArrayList<String> katList = new ArrayList<String>(Arrays.asList(initkatSayisi));

        final  MyOptionsPickerView<String>  pKatList = new MyOptionsPickerView<>(getActivity());

        pKatList.setPicker(katList);
        pKatList.setSubmitButtonText(R.string.tamam);
        pKatList.setCancelButtonText(R.string.kapat);
        pKatList.setTitle(getString(R.string.kat_sayisi));
        pKatList.setCyclic(false);
        pKatList.setSelectOptions(0);
        pKatList.setSelectOptions(0);

        pKatList.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                try{
                    etKatSayisi.setText(katList.get(options1));

                }catch (Exception e){
                    e.fillInStackTrace();
                }
            }
        });

        etKatSayisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pKatList.show();
            }
        });
    }

    private void initKat( ){


        final String [] katsayisi = getResources().getStringArray(R.array.katSayisi);

        final ArrayList<String> odaList = new ArrayList<String>(Arrays.asList(katsayisi));

        final  MyOptionsPickerView <String> pKatSayisi = new MyOptionsPickerView<>(getActivity());


        pKatSayisi.setPicker(odaList);
        pKatSayisi.setSubmitButtonText(R.string.tamam);
        pKatSayisi.setCancelButtonText(R.string.kapat);
        pKatSayisi.setTitle(getString(R.string.kat_sayisi));
        pKatSayisi.setCyclic(false);
        pKatSayisi.setSelectOptions(0);
        pKatSayisi.setSelectOptions(0);

        pKatSayisi.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                try{

                    etKat.setText(odaList.get(options1));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        etKat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pKatSayisi.show();
            }
        });

    }


    private void OfferSave() {
        if(etBaslik.getText().toString().matches("") & etEsyaSekli.getText().toString().matches("")
                &  etKatSayisi.getText().toString().matches("")
                & etIl.getText().toString().matches("")   & etIlce.getText().toString().matches("")
                & etToIl.getText().toString().matches("") & etToIlce.getText().toString().matches("")
                & etKat.getText().toString().matches("")  & etAciklama.getText().toString().matches(""))
        {
            Toast.makeText(getActivity(), getString(R.string.eksik_bilgiler), Toast.LENGTH_SHORT).show();

            return;

        }else {

            try {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("offersLocation").child("offers").child("location");

                LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        return;
                    }
                }
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                latitude =location.getLatitude();
                longitude = location .getLongitude();

                String etdateTime = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                Log.d("CurrentDate", etdateTime.toString());
                dbRef.push().setValue(
                        new Offer(
                                etBaslik.getText().toString().toUpperCase(),
                                etEsyaSekli.getText().toString(),
                                etKatSayisi.getText().toString(),
                                etIl.getText().toString(),
                                etIlce.getText().toString(),
                                etToIl.getText().toString(),
                                etToIlce.getText().toString(),
                                etKat.getText().toString(),
                                latitude,
                                longitude,
                                etdateTime,
                                etAciklama.getText().toString().toUpperCase()
                        )
                );
                offerNumber++;
                textClear();
                Toast.makeText(getActivity(), getString(R.string.basariya_kaydedildi), Toast.LENGTH_SHORT).show();
                System.out.println(offerNumber);
                myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                editor =myPrefs.edit();

                editor.putString("offerNumber", Integer.toString(offerNumber));
                editor.apply();

            }catch (Exception e){

                e.printStackTrace();

                Toast.makeText(getActivity(), getString(R.string.kayit_basarisiz), Toast.LENGTH_SHORT).show();
            }
        }


    }

    private LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for(Location location : locationResult.getLocations()){
                mLastLocation = location;

            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this.getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    LocationManager lm = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                }

            } else {

                Toast.makeText(getActivity(), "Lütfen izin verin", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void connectUser(){
        checkLocationPermission();
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
    private void checkLocationPermission() {

        if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){

                //for permissions user
                new android.app.AlertDialog.Builder(this.getActivity())
                        .setTitle("İzin Ver")
                        .setMessage("Lokasyon Kullanıma İzin Ver")
                        .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);


                            }
                        })
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }
    }


    private void District(){

        //Distcirt json raw file

        DistrictList districtDetail = new DistrictList();
        try {
            //load File
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.district)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line = null; (line = jsonReader.readLine()) != null; ) {
                jsonBuilder.append(line).append("\n");
            }

            Gson gson = new Gson();
            districtDetail = gson.fromJson(jsonBuilder.toString(), DistrictList.class);

            Log.d("Ilce", districtDetail.getDistrictDetail().get(0).getIlceTitle());

        }catch (FileNotFoundException e){
            Log.e("jsonFİle", "file not found");
        }catch (IOException e){
            Log.e("jsonFile","ioerror");
        }
        List<String> districtData = new ArrayList<>();

        for(int i=0; i<districtDetail.getDistrictDetail().size(); i++){
            districtData.add(districtDetail.getDistrictDetail().get(i).getIlceTitle());
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.simple_list, districtData);

        etIlce.setAdapter(adapter1);
        etIlce.setThreshold(0);
        etToIlce.setAdapter(adapter1);
        etToIlce.setThreshold(0);

    }

    private void citylist(){

        //city json get raw file

        CityList cityList = new CityList();
        try {
            //Load File
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.citys)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line = null; (line = jsonReader.readLine()) != null; ) {
                jsonBuilder.append(line).append("\n");
            }

            Gson gson = new Gson();
            cityList = gson.fromJson(jsonBuilder.toString(),CityList.class);

            Log.d("Deneme",cityList.getCityDetail().get(0).getName());


        } catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (IOException e) {
            Log.e("jsonFile", "ioerror");
        }

        List<String> spinnerData = new ArrayList<>();

        for(int i=0;i<cityList.getCityDetail().size();i++){

            spinnerData.add(cityList.getCityDetail().get(i).getName());
        }

        ArrayAdapter<String> adapter =new ArrayAdapter<>(Objects.requireNonNull(this.getActivity()), R.layout.simple_list, spinnerData);

        etIl.setAdapter(adapter);
        etIl.setThreshold(0);

        etToIl.setAdapter(adapter);
        etToIl.setThreshold(0);
    }

    private void textClear(){

        etBaslik.getText().clear();
        etEsyaSekli.setText("");
        etKatSayisi.setText("");
        etAciklama.getText().clear();
        etIl.getText().clear();
        etToIlce.getText().clear();
        etIlce.getText().clear();
        etToIl.getText().clear();

    }
}
