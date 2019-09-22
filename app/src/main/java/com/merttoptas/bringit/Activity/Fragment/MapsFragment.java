package com.merttoptas.bringit.Activity.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.merttoptas.bringit.Activity.Activity.MainActivity;
import com.merttoptas.bringit.Activity.Activity.MapsActivity;
import com.merttoptas.bringit.Activity.Adapter.RecyclerViewAdapter;
import com.merttoptas.bringit.Activity.Model.Offer;
import com.merttoptas.bringit.Activity.Model.Reklam;
import com.merttoptas.bringit.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private RecyclerView recyclerView;
    GoogleMap mMap;
    private Marker marker;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private PlaceAutocompleteFragment placeAutocomplete;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private GeoDataClient mGeoDataClient;
    private List<Object> offerlist = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private FirebaseStorage mDb;
    private double radius = 10000;
    private GeoQuery geoQuery;
    private Circle mapCircle;
    private GeoFire geoFire;
    private DatabaseReference ref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_maps, container, false);

        recyclerView = v.findViewById(R.id.recyclerview);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDb = FirebaseStorage.getInstance();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        mGeoDataClient = Places.getGeoDataClient(this.getActivity(),null);

        adapter = new RecyclerViewAdapter(offerlist, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(currentUser !=null){

            connectUser();
        }
        getData();

        ref = FirebaseDatabase.getInstance().getReference("offerAvailable").child("offers").child("location");
        geoFire = new GeoFire(ref);


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity()!=null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

        }
        if(currentUser !=null){

            connectUser();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mlocationRequest();

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);

            }
            else{
                checkLocationPermission();

            }


        }

    }

    private void connectUser(){
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        checkLocationPermission();

    }


    @Override
    public void onStart() {
        super.onStart();


        if (currentUser == null){
            FirebaseUser currentUser = mAuth.getCurrentUser();
        }
        if(this.mGoogleApiClient !=null){

            this.mGoogleApiClient.connect();
        }

    }

    private LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for(Location location : locationResult.getLocations()){

                if(getActivity() !=null){

                    mAuth = FirebaseAuth.getInstance();
                    currentUser = mAuth.getCurrentUser();

                    mLastLocation = location;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("offerAvailable");
                    GeoFire geoFire = new GeoFire(ref);

                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    if(mLastLocation !=null){

                        final double latitude = mLastLocation.getLatitude();
                        final double longitude = mLastLocation.getLongitude();
                        geoFire.setLocation(currentUser.getUid(),new GeoLocation(latitude, longitude),
                                new GeoFire.CompletionListener() {
                                    @Override
                                    public void onComplete(String key, DatabaseError error) {

                                        //ad marker
                                        if(marker !=null){
                                        marker.remove();
                                        }
                                        marker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude,longitude))
                                                .title(currentUser.getDisplayName()));

                                        //move camera
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 10.0f));

                                    }
                                });
                    }

                    setCircle(location);
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        @Override
                        public void onKeyEntered(String key, GeoLocation location) {

                            marker= mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).draggable(true)
                                    .title(currentUser.getDisplayName()));
                            //list the all users location in firebase database

                            Log.d("DbLocation", key +" :"  + location.latitude + " " +location.longitude);

                        }

                        @Override
                        public void onKeyExited(String key) {

                        }

                        @Override
                        public void onKeyMoved(String key, GeoLocation location) {

                        }

                        @Override
                        public void onGeoQueryReady() {

                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {

                        }
                    });

                }

            }
        }
    };
    private void checkLocationPermission() {

        if(ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){

                //for permissions user
                new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("İzin Ver")
                        .setMessage("Lokasyon Kullanıma İzin Ver")
                        .setPositiveButton(getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }
            } else {

                Toast.makeText(getActivity(), getString(R.string.location_izin_Ver), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private void getData() {

        final int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.scheduleLayoutAnimation();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("offersLocation").child("offers").child("location");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                offerlist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Offer o = snapshot.getValue(Offer.class);
                    offerlist.add(o);
                    //offerlist.add(new Reklam(
                     //       "54544"
                   // ));

                }

                adapter = new RecyclerViewAdapter(offerlist, getActivity());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), getString(R.string.veriler_alinamadi), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void mlocationRequest(){

        mLocationRequest = new LocationRequest();
        //i nterval for active location updates, in milliseconds
        mLocationRequest.setInterval(15000);
        //This controls the fastest rate at which your application will receive location updates,
        mLocationRequest.setFastestInterval(15000);
        //the most acccurate locations available
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }


    private void setCircle(Location location){

        mLastLocation = location;

        final double latitude = mLastLocation.getLatitude();
        final double longitude = mLastLocation.getLongitude();

        // Lists the last location in firebase.
            Log.d("mlastLocation", "location: " + location.getLatitude() + " " + location.getLongitude());

        //current location
        LatLng currentLocation = new LatLng(latitude,longitude);

        //old circle is deleted as the location refreshed.
        if(mapCircle!=null)
        {
            mapCircle.remove();
        }
        //Create a new circle.
        mapCircle=mMap.addCircle(new CircleOptions()
                .center(currentLocation)
                .radius(radius)  //500 metres
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5.0f));

        //0.9f = 0.9 km = 900m
        //In this code, it sends the location that will search within the radius range.
        geoQuery = geoFire.queryAtLocation(new GeoLocation(currentLocation.latitude, currentLocation.longitude), 10.0);

    }
}
