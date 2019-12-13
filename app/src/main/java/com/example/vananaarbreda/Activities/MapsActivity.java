package com.example.vananaarbreda.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vananaarbreda.Map.GeofenceTransitionsIntentService;
import com.example.vananaarbreda.Map.MapHandler;
import com.example.vananaarbreda.R;
import com.example.vananaarbreda.Route.Coordinate;
import com.example.vananaarbreda.Route.Route;
import com.example.vananaarbreda.Route.Sight;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Route route;

    //Location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location lastKnownLocation;
    private boolean requestingLocationUpdates;

    //Layout
    private TextView textViewConnectionStatus;

    //Geofencing
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;

    //statics
    private static final LatLng BREDA = new LatLng(51.5719149, 4.768323);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //setting layout
        textViewConnectionStatus = findViewById(R.id.textViewConnectionStatus);
        Button buttonHelp = findViewById(R.id.buttonHelp);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HelpActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        //Define location provider and geofencing client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geofencingClient = LocationServices.getGeofencingClient(this);

        //define location request
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setSmallestDisplacement(10.0f);

        //define callback when location provider gets a new location
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {

                Log.i(TAG, "Got location update: " + locationResult);
                if (locationResult == null){
                    textViewConnectionStatus.setText("no gps connection");
                    return;
                }

                for (Location location : locationResult.getLocations()){
                    if (location != null){
                        lastKnownLocation = location;
                    }
                }
            }
        };

        getLocationPermission();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady() called");

        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLng(BREDA));
        mMap.setMyLocationEnabled(true);
        mMap.setMinZoomPreference(14);

        UiSettings settings = mMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        settings.setTiltGesturesEnabled(true);
        settings.setRotateGesturesEnabled(true);
        settings.setMapToolbarEnabled(true);

        //TODO remove this when data system gets added and make call to database
        Route route = new Route();
        route.addCoordinate(new Coordinate(51.588714, 4.777158, new Sight("VVV", "")));   //VVV Breda
        route.addCoordinate(new Coordinate(51.593278, 4.779388, new Sight("Zuster", "")));   //LiefdesZuster
        route.addCoordinate(new Coordinate(51.5925, 4.779695, new Sight("Nassau", "")));     //Nassau Baronie Monument
        route.addCoordinate(new Coordinate(51.585773, 4.792621, new Sight("AVANS", "")));
        MapHandler.getInstance(this).buildWaypoints(mMap, route);
        MapHandler.getInstance(this).buildRoute(mMap, route);

        geofencingClient.addGeofences(geofencingRequest(), getGeofencePendingIntent());
        textViewConnectionStatus.setText("");
    }

    private GeofencingRequest geofencingRequest(){
        Log.d(TAG, "geofencingRequest() called");

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(MapHandler.getInstance(this).getGeofenceList());
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent(){
        Log.d(TAG, "getGeofencePendingIntent() called");

        if (geofencePendingIntent != null){
            return geofencePendingIntent;
        }

        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult() called");

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    onLocationPermission();
                }else {
                    textViewConnectionStatus.setText(R.string.Location_not_allowed);
                }
                break;

            }
        }
    }

    //Initializes the check for location permission by user
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission() called");

        //Permission is not granted = ask user for permission
        if (!checkIfAlreadyHavePermission(Manifest.permission.ACCESS_FINE_LOCATION)) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }

        //Permission is granted
        }else {
            onLocationPermission();
        }
    }

    //Checks if given permission was given previously
    private boolean checkIfAlreadyHavePermission(String permission) {
        int result = ContextCompat.checkSelfPermission(this, permission);
        boolean hasPermission = result == PackageManager.PERMISSION_GRANTED;

        Log.d(TAG, "checkIfAlreadyHavePermission() called with result: " + hasPermission);

        return hasPermission;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    //starts continuous location updates
    private void startLocationUpdates(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    //stops continuous location updates
    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    //Gets called when the user has given permission
    private void onLocationPermission(){
        Log.d(TAG, "onLocaionPermission() called");
        requestingLocationUpdates = true;
        textViewConnectionStatus.setText(R.string.loading_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        startLocationUpdates();
    }
}
