package com.example.vananaarbreda.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.example.vananaarbreda.Activities.SightActivity;
import com.example.vananaarbreda.Route.Coordinate;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vananaarbreda.Route.Route;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GPSHandler {

    //statics
    private static GPSHandler instance;
    private static final String TAG = GPSHandler.class.getSimpleName();
    private static final int MAXIMUM_WAYPOINT_RADIUS = 15;

    //map and context
    private MapHandler mapHandler;
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

                        mapHandler.onLocationUpdate(lastKnownLocation);
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

    /**
     * Tranlates a route object into a list of points and sends this to the direction API
     * @param route The route object which needs to be translated into a list of points to go through
     */
    public void requestRoute(Route route) {
        Log.d(TAG, "requestRoute() called");
        List<Coordinate> coordinates = new ArrayList<>();

        for (Coordinate coordinate : route.getCoordinates()){
            coordinates.add(coordinate);

            if (coordinates.size() > 25){
                doRequest(coordinates);
                coordinates.clear();
            }
        }
        //coordinates.add(coordinates.get(0));
        doRequest(coordinates);
    }

    /**
     * Makes a volley request and sends this to the direction API
     * @param coordinates The list of coordinates which the route needs to go through
     */
    private void doRequest (List<Coordinate> coordinates) {
        Log.d(TAG, "doRequest() called");
        Coordinate firstCoordinate = coordinates.get(0);
        final Coordinate lastCoordinate = coordinates.get(coordinates.size() - 1);

        LatLng origin = new LatLng(firstCoordinate.getLatitude(), firstCoordinate.getLongitude());
        LatLng destination = new LatLng(lastCoordinate.getLatitude(), lastCoordinate.getLongitude());

        List<Coordinate> waypoints = new ArrayList<>();

        for (int i = 1; i < coordinates.size() - 1; i++) {
            Coordinate coordinate = coordinates.get(i);
            waypoints.add(coordinate);
        }

        Log.d(TAG, "Making JSON request");
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                buildUrl(origin, destination, "walking", waypoints),
                null,
                new Response.Listener<JSONObject>() {

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

                    MapHandler.getInstance(context).setRoute(latLngs);
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
                }
                );
        requestQueue.add(request);
    }

    /**
     * Returns the GPSHandler object instance or makes one if not available
     * @param context used for initialization on first call
     * @return The GPSHandler object instance
     */
    public static GPSHandler getInstance(Context context){
        Log.d(TAG, "getInstance() called");
        if (instance == null){
            instance = new GPSHandler(context);
            Log.d(TAG, "new GPSHandler instance created");
        }

        return instance;
    }

    /**
     * Sets the maphandler which the GPSHandler needs to communicate to to change the map
     * @param handler the maphandler to be selected
     */
    public void setMapHandler(final MapHandler handler){
        Log.d(TAG, "setMapHandler() Called");
        this.mapHandler = handler;
    }

    /**
     * Returns the last known user location
     * @return The last location that was found by the GPSHandler
     */
    public Location getLastKnownLocation(){
        Log.d(TAG, "getlastKnownLocation() called");
        return this.lastKnownLocation;
    }

    /**
     * Starts the GPSHandler with updating the user's location
     */
    public void startLocationUpdating() {
        Log.d(TAG, "startLocationUpdating() called");
        this.fusedLocationProviderClient.requestLocationUpdates(this.locationRequest, this.locationCallback, Looper.getMainLooper());
    }

    /**
     * Stops updating the user's location
     */
    public void stopLocationUpdating(){
        Log.d(TAG, "stopLocationUpdating() called");
        this.fusedLocationProviderClient.removeLocationUpdates(this.locationCallback);
    }

    /**
     * Builds the URL used to communicate with the directions API
     * @param origin The first point of the route
     * @param dest The last point of the route
     * @param directionMode What vehicle/routes the user will take
     * @param waypoints Any waypoints the directions api should go through
     * @return A URL containing all information given in the parameters
     */
    private String buildUrl(LatLng origin, LatLng dest, String directionMode, List<Coordinate> waypoints) {
        Log.d(TAG, "buildUrl() called");
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
        Log.d(TAG, "Built URL: " + url);
        return url;
    }
}
