package com.example.vananaarbreda.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.vananaarbreda.Map.GPSHandler;
import com.example.vananaarbreda.Map.MapHandler;
import com.example.vananaarbreda.R;
import com.example.vananaarbreda.Route.Coordinate;
import com.example.vananaarbreda.Route.Route;
import com.example.vananaarbreda.Route.Sight;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //Layout
    private TextView textViewConnectionStatus;

    //statics
    private static final LatLng BREDA = new LatLng(51.5719149, 4.768323);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //setting layout
        this.textViewConnectionStatus = findViewById(R.id.textViewConnectionStatus);
        Button buttonHelp = findViewById(R.id.buttonHelp);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opens a new screen with information about the app
                Log.d(TAG, "help button pressed");

                Intent intent = new Intent(v.getContext(), HelpActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        Button routesButton = findViewById(R.id.buttonRoutes);
        routesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creates a popupwindow with a listview
                Log.d(TAG, "routes button pressed");

                popupWindowRoutes().showAsDropDown(v);
            }
        });

        getLocationPermission();
    }

    //Creates a popup window with a listview
    public PopupWindow popupWindowRoutes() {
        Log.d(TAG, "popupWindowRoutes() called");

        //TODO MAKE CALL TO DATABASE TO GET ALL ROUTES AND SHOW THEM HERE

        //Mock data Routes
        final List<Route> routes = new ArrayList<>();
        Route route1 = new Route("Breda");
        route1.addCoordinate(new Coordinate(51.588714, 4.777158, new Sight("VVV", "")));      //VVV Breda
        route1.addCoordinate(new Coordinate(51.593278, 4.779388, new Sight("Zuster", "")));   //LiefdesZuster
        route1.addCoordinate(new Coordinate(51.5925, 4.779695, new Sight("Nassau", "")));     //Nassau Baronie Monument
        route1.addCoordinate(new Coordinate(51.585773, 4.792621, new Sight("AVANS", "")));    //Avans
        route1.addCoordinate(new Coordinate(51.788679, 4.662715, new Sight("Mij thuis", "")));//thuis
        routes.add(route1);

        //Creates popupwindow
        PopupWindow popupWindow = new PopupWindow(this);

        //Creates listview
        ListView listViewDogs = new ListView(this);
        listViewDogs.setAdapter(new CustomListViewAdapter(this, routes));

        listViewDogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (checkIfAlreadyHavePermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    buildRoute(routes.get(position));
                }else {
                    textViewConnectionStatus.setText(R.string.location_request_no_permission);
                }
            }
        });

        //some  visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(250);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        //set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
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

        this.mMap = googleMap;

        this.mMap.moveCamera(CameraUpdateFactory.newLatLng(BREDA));
        this.mMap.setMyLocationEnabled(true);
        this.mMap.setMinZoomPreference(14);

        UiSettings settings = this.mMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        settings.setTiltGesturesEnabled(true);
        settings.setRotateGesturesEnabled(true);
        settings.setMapToolbarEnabled(true);

        Log.d(TAG, "map initialised");
        MapHandler.getInstance(this);

        this.textViewConnectionStatus.setText("");
    }

    //Sends a message to the mapHandler to display the currently selected route on the map
    private void buildRoute(Route route){
        MapHandler.getInstance(this).setRoute(route);
        MapHandler.getInstance(this).buildWaypoints(mMap);
        MapHandler.getInstance(this).buildRoute(mMap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult() called");

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    onLocationPermission();
                }else {
                    this.textViewConnectionStatus.setText(R.string.Location_not_allowed);
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

        //Permission is already granted
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

    //Gets called when the user has given permission
    private void onLocationPermission(){
        Log.d(TAG, "onLocaionPermission() called");
        this.textViewConnectionStatus.setText(R.string.loading_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}
