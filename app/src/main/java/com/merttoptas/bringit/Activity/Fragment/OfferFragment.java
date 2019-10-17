package com.merttoptas.bringit.Activity.Fragment;

import android.Manifest;
import android.app.Activity;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.merttoptas.bringit.Activity.Model.Chat;
import com.merttoptas.bringit.Activity.Model.CityList;
import com.merttoptas.bringit.Activity.Model.DistrictList;
import com.merttoptas.bringit.Activity.Model.Offer;
import com.merttoptas.bringit.Activity.Model.User;
import com.merttoptas.bringit.R;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class OfferFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView etTransportMethod, etNumberOfFloors, etToFloors;
    private EditText etOfferTitle, etExplanation;
    private Button btnOfferSave;
    private AutoCompleteTextView etProvince, etDistrict, etTargetProvince, etTargetDistrict;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    public double latitude;
    public double longitude;
    private LocationManager locationManager;
    private ScrollView scrollView;
    private Typeface typeface;
    private SharedPreferences myPrefs;
    SharedPreferences.Editor editor;
    private TextView tvIlanSayisi;
    int offerNumber =0;
    String offerNameSurname  ="";
    private Activity mActivity;
    Animation btnAnim ;



    public OfferFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        etTransportMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTransportMove();
            }
        });

        etNumberOfFloors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNumberOfFloors();
            }
        });

        etToFloors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initToFloors();
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
        etOfferTitle = v.findViewById(R.id.etOfferTitle);
        etTransportMethod = v.findViewById(R.id.etTransportMethod);
        etNumberOfFloors = v.findViewById(R.id.etNumberOfFloors);
        etProvince = v.findViewById(R.id.etProvince);
        etDistrict = v.findViewById(R.id.etDistrict);
        etTargetProvince = v.findViewById(R.id.etTargetProvince);
        etToFloors = v.findViewById(R.id.etToFloors);
        etTargetDistrict = v.findViewById(R.id.etTargetDistrict);
        btnOfferSave = v.findViewById(R.id.btnOfferSave);
        scrollView = v.findViewById(R.id.scrollView);
        etExplanation = v.findViewById(R.id.etExplanation);
        tvIlanSayisi = v.findViewById(R.id.tvIlanSayisi);
        btnAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.button_animation);


        typeface = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/SourceSansPro-Regular.ttf");
        btnOfferSave.setTypeface(typeface);

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(toolbar.getTitle());
        mTitle.setText(R.string.ilan_ver);

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        btnOfferSave.setAnimation(btnAnim);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));

        if (currentUser != null) {

            connectUser();
        }
        //Firestore database db
        FirebaseStorage db = FirebaseStorage.getInstance();

        //City and district get a name
        initTransportMove();
        districtList();
        cityList();

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

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    private void initTransportMove(){

        final String [] initTransportMove = getResources().getStringArray(R.array.tasinmaSekli);

        final  ArrayList<String> transportList = new ArrayList<String>(Arrays.asList(initTransportMove));

        final  MyOptionsPickerView<String>  pTransport = new MyOptionsPickerView<>(getActivity());


        pTransport.setPicker(transportList);
        pTransport.setSubmitButtonText(R.string.tamam);
        pTransport.setCancelButtonText(R.string.kapat);
        pTransport.setTitle(getString(R.string.kat_sayisi));
        pTransport.setCyclic(false);
        pTransport.setSelectOptions(0);
        pTransport.setSelectOptions(0);


        pTransport.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                try {
                    etTransportMethod.setText(transportList.get(options1));

                }catch (Exception e){
                    e.fillInStackTrace();
                }
            }
        });

        etTransportMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pTransport.show();
            }
        });

    }


    private void initNumberOfFloors(){

        final String [] initNumberOfFloors = getResources().getStringArray(R.array.katSayisi);

        final  ArrayList<String> katList = new ArrayList<String>(Arrays.asList(initNumberOfFloors));

        final  MyOptionsPickerView<String>  pNumberOfFloors = new MyOptionsPickerView<>(getActivity());

        pNumberOfFloors.setPicker(katList);
        pNumberOfFloors.setSubmitButtonText(R.string.tamam);
        pNumberOfFloors.setCancelButtonText(R.string.kapat);
        pNumberOfFloors.setTitle(getString(R.string.kat_sayisi));
        pNumberOfFloors.setCyclic(false);
        pNumberOfFloors.setSelectOptions(0);
        pNumberOfFloors.setSelectOptions(0);

        pNumberOfFloors.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                try{
                    etNumberOfFloors.setText(katList.get(options1));

                }catch (Exception e){
                    e.fillInStackTrace();
                }
            }
        });

        etNumberOfFloors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pNumberOfFloors.show();
            }
        });
    }

    private void initToFloors( ){


        final String [] toFloors = getResources().getStringArray(R.array.katSayisi);

        final ArrayList<String> odaList = new ArrayList<String>(Arrays.asList(toFloors));

        final  MyOptionsPickerView <String> pToFloors = new MyOptionsPickerView<>(getActivity());


        pToFloors.setPicker(odaList);
        pToFloors.setSubmitButtonText(R.string.tamam);
        pToFloors.setCancelButtonText(R.string.kapat);
        pToFloors.setTitle(getString(R.string.kat_sayisi));
        pToFloors.setCyclic(false);
        pToFloors.setSelectOptions(0);
        pToFloors.setSelectOptions(0);

        pToFloors.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                try{

                    etToFloors.setText(odaList.get(options1));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        etToFloors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pToFloors.show();
            }
        });

    }


    private void OfferSave() {
        String controlTitle =etOfferTitle.getText().toString();
        String controlTransportMethod = etTransportMethod.getText().toString();
        String controlNumberFloors = etNumberOfFloors.getText().toString();
        String controlProvince =  etProvince.getText().toString();
        String controlDistrict = etDistrict.getText().toString();
        String controlToProvince =etTargetProvince.getText().toString();
        String controlToDistrict =etTargetDistrict.getText().toString();
        String controlFloor =  etToFloors.getText().toString();
        String controlExplanation =etExplanation.getText().toString();

        if(TextUtils.isEmpty(controlTitle) || TextUtils.isEmpty(controlTransportMethod)
                || TextUtils.isEmpty(controlNumberFloors) || TextUtils.isEmpty(controlProvince)
                || TextUtils.isEmpty(controlDistrict) || TextUtils.isEmpty(controlToProvince)
                || TextUtils.isEmpty(controlToDistrict) || TextUtils.isEmpty(controlFloor)
                || TextUtils.isEmpty(controlExplanation))
        {
            Toast.makeText(getActivity(), getString(R.string.eksik_bilgiler), Toast.LENGTH_SHORT).show();

        }else {

            try {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Offers");

                LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        return;
                    }
                }
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                latitude =location.getLatitude();
                String userid = currentUser.getUid();
                longitude = location .getLongitude();
                offerNameSurname = currentUser.getDisplayName();
                String etdateTime = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                Log.d("CurrentDate", etdateTime.toString());

                dbRef.push().setValue(
                        new Offer(
                                userid,
                                etOfferTitle.getText().toString(),
                                etTransportMethod.getText().toString(),
                                etNumberOfFloors.getText().toString(),
                                etProvince.getText().toString().toUpperCase(),
                                etDistrict.getText().toString(),
                                etTargetProvince.getText().toString().toUpperCase(),
                                etTargetDistrict.getText().toString(),
                                etToFloors.getText().toString(),
                                latitude,
                                longitude,
                                etdateTime,
                                etExplanation.getText().toString(),
                                offerNameSurname
                        )
                );
                offerNumber++;
                textClear();
                Toast.makeText(getActivity(), getString(R.string.basariya_kaydedildi), Toast.LENGTH_SHORT).show();
                System.out.println(offerNumber);
                DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("Users");
                Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            User user =  snapshot.getValue(User.class);
                            if(user.getId().equals(currentUser.getUid())){
                                String leads = String.valueOf(offerNumber);
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("leads", leads);
                                snapshot.getRef().updateChildren(hashMap);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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


    private void districtList(){

        //Distcirt json raw file

        DistrictList districtDetail = new DistrictList();
        try {
            //load File
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.district)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line; (line = jsonReader.readLine()) != null; ) {
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

        etDistrict.setAdapter(adapter1);
        etDistrict.setThreshold(0);

        etTargetDistrict.setAdapter(adapter1);
        etTargetDistrict.setThreshold(0);

    }

    private void cityList(){

        //city json get raw file

        CityList cityList = new CityList();
        try {
            //Load File
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.citys)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line; (line = jsonReader.readLine()) != null; ) {
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

        etProvince.setAdapter(adapter);
        etProvince.setThreshold(0);

        etTargetProvince.setAdapter(adapter);
        etTargetProvince.setThreshold(0);
    }

    private void textClear(){

        etOfferTitle.getText().clear();
        etTransportMethod.setText("");
        etNumberOfFloors.setText("");
        etExplanation.getText().clear();
        etProvince.getText().clear();
        etTargetDistrict.getText().clear();
        etDistrict.getText().clear();
        etTargetProvince.getText().clear();
        etToFloors.setText("");

    }

}
