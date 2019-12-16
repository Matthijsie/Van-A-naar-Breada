package com.example.vananaarbreda.Map;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class GPSHandler {

    private static GPSHandler instance;
    private static final String TAG = GPSHandler.class.getSimpleName();

    private MapHandler mapHandler;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location lastKnownLocation;

    private GPSHandler(Context context){
        this.fusedLocationProviderClient = new FusedLocationProviderClient(context);

        //define location request
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setSmallestDisplacement(10.0f);

        //define callback when location provider gets a new location
        locationCallback = new LocationCallback(){
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
    }

    public static GPSHandler getInstance(Context context){
        if (instance == null){
            instance = new GPSHandler(context);
        }

        return instance;
    }

    public void setMapHandler(MapHandler handler){
        this.mapHandler = handler;
    }

    public Location getLastKnownLocation(){
        return lastKnownLocation;
    }

    public void startLocationUpdating() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdating(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
