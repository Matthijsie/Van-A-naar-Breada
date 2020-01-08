package com.example.vananaarbreda.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.example.vananaarbreda.Activities.SightActivity;
import com.example.vananaarbreda.Route.Coordinate;
import com.example.vananaarbreda.Route.DatasetChangedListener;
import com.example.vananaarbreda.Route.Route;
import com.example.vananaarbreda.Route.RouteDB;
import com.example.vananaarbreda.Route.Sight;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.JointType;
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

    /**
     * Calls the MapHandler singleton
     * @param context used for initializing certain attributes on first call
     * @return The MapHandler instance object
     */
    public static MapHandler getInstance(Context context){
        Log.d(TAG, "getInstance() called");
        if (instance == null){
            instance = new MapHandler(context);
            Log.d(TAG, "new MapHandler instance created");
        }

        return instance;
    }

    /**
     * Places all markers on the map
     */
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

    /**
     * Removes all markers from the map
     */
    public void clearMarkers(){
        Log.d(TAG, "clearMarkers() called");
        for (Marker marker : markers){
            marker.remove();
        }
        this.markers.clear();
        Log.d(TAG, "Cleared all markers from map and local List");
    }

    /**
     * Sets the route the maphandler needs to use
     * @param route The route to be used by the maphandler
     */
    public void setRoute(Route route){
        this.route = route;
    }

    /**
     * Sets the map the maphandler needs to update
     * @param googleMap the map to be updated
     */
    public void setMap(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

    /**
     * Gets the currently selected route
     * @return the currently used route by the MapHandler
     */
    public Route getRoute(){
        return this.route;
    }

    /**
     * Calls the GPSHandler to make a request with the currently selected route, also starts location updating
     */
    public void buildRoute() {
        GPSHandler.getInstance(this.context).requestRoute(this.route);
        GPSHandler.getInstance(context).startLocationUpdating();
    }

    /**
     * Sets a route on the map
     * @param latLngs All coordinates the route line needs to go through
     */
    protected void setRoute(List<LatLng> latLngs, int color){
        Log.d(TAG, "setRoute() called");
        googleMap.addPolyline(new PolylineOptions().addAll(latLngs).jointType(JointType.ROUND).color(color));
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
