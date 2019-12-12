package com.example.vananaarbreda.Map;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class GPSHandler {

    private static GPSHandler instance;

    private MapHandler mapHandler;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    private GPSHandler(Context context){
        this.fusedLocationProviderClient = new FusedLocationProviderClient(context);

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
}
