package com.example.vananaarbreda.Map;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

    private static final String TAG  = MapHandler.class.getSimpleName();
    private static MapHandler instance;
    private Context context;
    private List<Geofence> geofenceList;

    private MapHandler(Context context){
        this.context = context;
        geofenceList = new ArrayList<>();
        GPSHandler.getInstance(this.context).setMapHandler(this);
    }

    public static MapHandler getInstance(Context context){
        Log.d(TAG, "getInstance() called");
        if (instance == null){
            instance = new MapHandler(context);
            Log.d(TAG, "new MapHandler instance created");
        }

        return instance;
    }

    public void buildWaypoints(GoogleMap googleMap, Route route){
        Log.d(TAG, "buildWaypoints() called");
        geofenceList.clear();

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
            Log.d(TAG, "Marker added to map");

            geofenceList.add(new Geofence.Builder()
                .setRequestId(coordinate.getSight().getName())
                .setExpirationDuration(12 * 60 * 60 * 1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(coordinate.getLatitude(), coordinate.getLongitude(), 50)
                .build());

            Log.d(TAG, "geofence added to list with coordinates: (" + coordinate.getLatitude() + "," + coordinate.getLongitude() + ")");
        }

    }

    public void buildRoute(GoogleMap googleMap, Route route){

        ArrayList<LatLng> latLngs = new ArrayList<>();

        for (Coordinate coordinate : route.getCoordinates()){
            latLngs.add(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()));
        }

        googleMap.addPolyline(new PolylineOptions().addAll(latLngs));
    }

    public List<Geofence> getGeofenceList(){
        Log.d(TAG, "getGeofenceList() called");
        return geofenceList;
    }
}
