package com.example.vananaarbreda.Map;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.vananaarbreda.Activities.SightActivity;
import com.example.vananaarbreda.Route.Coordinate;
import com.example.vananaarbreda.Route.Route;
import com.example.vananaarbreda.Route.Sight;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapHandler {

    private static MapHandler instance;
    private Context context;
    private ArrayList<Geofence> geofences;

    private MapHandler(Context context){
        this.context = context;

    }

    public static MapHandler getInstance(Context context){
        if (instance == null){
            instance = new MapHandler(context);
        }

        return instance;
    }

    public void buildWaypoints(GoogleMap googleMap, Route route){
        for(final Coordinate coordinate : route.getCoordinates()){

            //Adding marker to map
            Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(coordinate.getLatitude(), coordinate.getLongitude())));
            marker.setTag(coordinate.getSight());

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    try {
                        Intent intent = new Intent(context, SightActivity.class);
                        intent.putExtra("SIGHT", (Sight) marker.getTag());

                        context.startActivity(intent);

                        return true;
                    }catch (Exception e){
                        return false;
                    }
                }
            });

            //Making geofence
            geofences.add(new Geofence.Builder()
                    .setRequestId(coordinate.getSight().getName())                              //Giving geofence an ID
                    .setCircularRegion(coordinate.getLatitude(), coordinate.getLongitude(), 15) //Setting location and radius
                    .setExpirationDuration(6*60*60*1000)                                        //Setting expiration time
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)                     //Setting transition types
                    .build());

        }
    }

    public void buildRoute(GoogleMap googleMap, Route route){

        ArrayList<LatLng> latLngs = new ArrayList<>();

        for (Coordinate coordinate : route.getCoordinates()){
            latLngs.add(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()));
        }

        googleMap.addPolyline(new PolylineOptions().addAll(latLngs));
    }

    public List<Geofence> getGeofences(){
        return this.geofences;
    }

}
