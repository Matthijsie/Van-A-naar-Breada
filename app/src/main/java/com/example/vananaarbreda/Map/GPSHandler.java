package com.example.vananaarbreda.Map;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.example.vananaarbreda.Route.Route;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class GPSHandler {

    //statics
    private static GPSHandler instance;
    private static final String TAG = GPSHandler.class.getSimpleName();

    //map
    private MapHandler mapHandler;

    //Location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location lastKnownLocation;

    private GPSHandler(Context context){
        this.fusedLocationProviderClient = new FusedLocationProviderClient(context);
        Log.d(TAG, "fusedLocationProviderClient created");

        //define location request
        this.locationRequest = LocationRequest.create();
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationRequest.setInterval(10 * 1000);
        this.locationRequest.setSmallestDisplacement(10.0f);
        Log.d(TAG, "LocationRequest defined");

        //define callback when location provider gets a new location
        this.locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {

                Log.i(TAG, "Got location update: " + "(" + locationResult.getLastLocation().getLatitude() + " , " + locationResult.getLastLocation().getLongitude() + ")");

                if (locationResult == null){
                    return;
                }

                for (Location location : locationResult.getLocations()){
                    if (location != null){
                        lastKnownLocation = location;


                    }
                }
            }
        };
        Log.d(TAG, "LocationCallback defined");
    }

    public static GPSHandler getInstance(Context context){
        Log.d(TAG, "getInstance() called");
        if (instance == null){
            instance = new GPSHandler(context);
            Log.d(TAG, "new GPSHandler instance created");
        }

        return instance;
    }

    public void setMapHandler(MapHandler handler){
        Log.d(TAG, "setMapHandler() Called");
        this.mapHandler = handler;
    }

    public Location getLastKnownLocation(){
        Log.d(TAG, "getlastKnownLocation() called");
        return this.lastKnownLocation;
    }

    public void startLocationUpdating() {
        Log.d(TAG, "startLocationUpdating() called");
        this.fusedLocationProviderClient.requestLocationUpdates(this.locationRequest, this.locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdating(){
        Log.d(TAG, "stopLocationUpdating() called");
        this.fusedLocationProviderClient.removeLocationUpdates(this.locationCallback);
    }
}
