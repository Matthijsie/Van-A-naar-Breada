package com.example.vananaarbreda.Map;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.vananaarbreda.Activities.SightActivity;
import com.example.vananaarbreda.Route.Coordinate;
import com.example.vananaarbreda.Route.DatasetChangedListener;
import com.example.vananaarbreda.Route.Route;
import com.example.vananaarbreda.Route.RouteDB;
import com.example.vananaarbreda.Route.Sight;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapHandler implements DatasetChangedListener {

    //statics
    private static final String TAG  = MapHandler.class.getSimpleName();
    private static MapHandler instance;

    //Attributes
    private Context context;
    private Route route;
    private GoogleMap googleMap;
    private List<Marker> markers;

    private MapHandler(Context context){
        this.context = context;
        RouteDB.getInstance(this.context).setDataSetChangedListener(this);
        GPSHandler.getInstance(this.context).setMapHandler(this);
        this.markers = new ArrayList<>();
    }

    public static MapHandler getInstance(Context context){
        Log.d(TAG, "getInstance() called");
        if (instance == null){
            instance = new MapHandler(context);
            Log.d(TAG, "new MapHandler instance created");
        }

        return instance;
    }

    public void buildWaypoints(){
        clearMarkers();

        Log.d(TAG, "buildWaypoints() called");

        for(final Coordinate coordinate : route.getCoordinates()){

            //Adding marker to map
            Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(coordinate.getLatitude(), coordinate.getLongitude())));
            marker.setTag(coordinate.getSight());

            if (coordinate.getSight().isVisited()) {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
            
            this.markers.add(marker);

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
            Log.d(TAG, "Marker added to map on coordinates: (" + coordinate.getLatitude() + " , " + coordinate.getLongitude() + ")");

        }
    }

    public void clearMarkers(){
        Log.d(TAG, "clearMarkers() called");
        for (Marker marker : markers){
            marker.remove();
        }
        this.markers.clear();
        Log.d(TAG, "Cleared all markers from map and local List");
    }

    public void setRoute(Route route){
        this.route = route;
    }

    public void setMap(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

    public Route getRoute(){
        return this.route;
    }

    public void buildRoute() {

        //TODO make a volley call to the direction API
        ArrayList<LatLng> latLngs = new ArrayList<>();

        for (Coordinate coordinate : route.getCoordinates()) {
            latLngs.add(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()));
        }

        googleMap.addPolyline(new PolylineOptions().addAll(latLngs));

        GPSHandler.getInstance(context).startLocationUpdating();

    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged() called");
        Route route = new Route(this.route.getName());
        for (Coordinate coordinate : RouteDB.getInstance(this.context).readValues()){
            route.addCoordinate(coordinate);
        }
        this.route = route;
        buildWaypoints();
    }
}
