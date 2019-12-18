package com.example.vananaarbreda.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.util.Log;

import com.example.vananaarbreda.Activities.SightActivity;
import com.example.vananaarbreda.Route.Coordinate;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vananaarbreda.Route.Coordinate;
import com.example.vananaarbreda.Route.Route;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class GPSHandler {

    //statics
    private static GPSHandler instance;
    private static final String TAG = GPSHandler.class.getSimpleName();
    private static final int MAXIMUM_WAYPOINT_RADIUS = 15;

    //map and context
    private MapHandler mapHandler;
    private GoogleMap maps;
    private Context context;

    //Location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location lastKnownLocation;
    private Coordinate previousCoordinate;
    private RequestQueue requestQueue;

    private GPSHandler(final Context context){
        this.context = context;
        this.fusedLocationProviderClient = new FusedLocationProviderClient(this.context);
        Log.d(TAG, "fusedLocationProviderClient created");
        requestQueue = Volley.newRequestQueue(this.context);

        //define location request
        this.locationRequest = LocationRequest.create();
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationRequest.setInterval(10 * 1000);
        this.locationRequest.setSmallestDisplacement(5.0f);
        Log.d(TAG, "LocationRequest defined");

        //define callback when location provider gets a new location
        this.locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {

                Log.i(TAG, "Got location update: " + "(" + locationResult.getLastLocation().getLatitude() + " , " + locationResult.getLastLocation().getLongitude() + ")");

                //return of location has an error
                if (locationResult == null){
                    return;
                }

                //handle location
                for (Location location : locationResult.getLocations()){
                    if (location != null){
                        lastKnownLocation = location;

                        //Loops through all waypoints to see if one is within the given radius and sends user a notification
                        for(Coordinate coordinate : mapHandler.getRoute().getCoordinates()){
                            Location otherLocation = new Location(coordinate.getSight().getName());
                            otherLocation.setLatitude(coordinate.getLatitude());
                            otherLocation.setLongitude(coordinate.getLongitude());

                            if (lastKnownLocation.distanceTo(otherLocation) <= MAXIMUM_WAYPOINT_RADIUS){
                                Log.d(TAG, "User is withing " + MAXIMUM_WAYPOINT_RADIUS + " metres of a waypoint");

                                //Start new intent if the user hasn't selected this waypoint as already seen
                                if (!coordinate.getSight().isVisited() && !coordinate.equals(previousCoordinate)) {
                                    Intent intent = new Intent(context, SightActivity.class);
                                    intent.putExtra("SIGHT", coordinate.getSight());
                                    context.startActivity(intent);
                                    previousCoordinate = coordinate;
                                }
                            }
                        }
                    }
                }
            }
        };

        Log.d(TAG, "LocationCallback defined");
    }

    //Makes a volleyrequest to the google direction api
    public void requestRoute(Route route) {
        List<Coordinate> coordinates = new ArrayList<>();

        for (Coordinate coordinate : route.getCoordinates()){
            coordinates.add(coordinate);

            if (coordinates.size() > 25){
                doRequest(coordinates);
                coordinates.clear();
            }
        }
        doRequest(coordinates);
    }

    public void doRequest (List<Coordinate> coordinates) {

        Coordinate firstCoordinate = coordinates.get(0);
        final Coordinate lastCoordinate = coordinates.get(coordinates.size() - 1);

        LatLng origin = new LatLng(firstCoordinate.getLatitude(), firstCoordinate.getLongitude());
        LatLng destination = new LatLng(lastCoordinate.getLatitude(), lastCoordinate.getLongitude());

        List<Coordinate> waypoints = new ArrayList<>();

        for (int i = 1; i < coordinates.size() - 1; i++) {
            Coordinate coordinate = coordinates.get(i);
            waypoints.add(coordinate);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                buildUrl(origin, destination, "walking", waypoints), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                List<LatLng> latLngs = new ArrayList<>();

                try {

                    Log.d(TAG, "Parsing JSON...");
                    JSONArray legs = response.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");

                    for (int i = 0; i < legs.length(); i++) {

                        JSONArray steps = response.getJSONArray("routes").getJSONObject(0).getJSONArray("legs")
                                .getJSONObject(i).getJSONArray("steps");

                        for (int j = 0; j < steps.length(); j++) {
                            double beginlat = steps.getJSONObject(j).getJSONObject("start_location").getDouble("lat");
                            double beginlng = steps.getJSONObject(j).getJSONObject("start_location").getDouble("lng");

                            LatLng beginLatLngs = new LatLng(beginlat, beginlng);

                            double endlat = steps.getJSONObject(j).getJSONObject("end_location").getDouble("lat");
                            double endlng = steps.getJSONObject(j).getJSONObject("end_location").getDouble("lng");

                            LatLng endLatLngs = new LatLng(endlat, endlng);

                            latLngs.add(beginLatLngs);
                            latLngs.add(endLatLngs);
                        }
                    }

                    MapHandler.getInstance(context).buildRoute(latLngs);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });
        requestQueue.add(request);
    }


    public static GPSHandler getInstance(Context context){
        Log.d(TAG, "getInstance() called");
        if (instance == null){
            instance = new GPSHandler(context);
            Log.d(TAG, "new GPSHandler instance created");
        }

        return instance;
    }

    public void setMapHandler(final MapHandler handler){
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

    private String buildUrl(LatLng origin, LatLng dest, String directionMode, List<Coordinate> waypoints) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String str_waypoints = "waypoints=";
        for (int i = 0; i < waypoints.size(); i++) {
            Coordinate coordinate = waypoints.get(i);

            str_waypoints += coordinate.getLatitude() + "%2C" + coordinate.getLongitude();

            if (i < waypoints.size() - 1) {
                str_waypoints += "%7C";
            }
        }
        String parameters = str_origin + "&" + str_dest + "&" + mode + "&" + str_waypoints;

        String url = "http://145.48.6.80:3000/directions?" + parameters + "&key=" + "d8170a45-2be1-4e27-86f6-a46502e272ce";

        return url;
    }
}
