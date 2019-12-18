package com.example.vananaarbreda.Map;

import android.content.Context;
import android.location.Location;
import android.util.Log;

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

    private static GPSHandler instance;

    private MapHandler mapHandler;
    private GoogleMap maps;
    private static final String TAG = GPSHandler.class.getSimpleName();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private RequestQueue requestQueue;
    private Context context;

    private GPSHandler(Context context){
        this.fusedLocationProviderClient = new FusedLocationProviderClient(context);
        requestQueue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public static GPSHandler getInstance(Context context){
        if (instance == null){
            instance = new GPSHandler(context);
        }

        return instance;
    }

    public void requestRoute(Route route){

        List<Coordinate> coordinates = route.getCoordinates();

        Coordinate firstCoordinate = coordinates.get(0);
        final Coordinate lastCoordinate = coordinates.get(coordinates.size() -1);

        LatLng origin =  new LatLng(firstCoordinate.getLatitude(),firstCoordinate.getLongitude());
        LatLng destination = new LatLng(lastCoordinate.getLatitude(), lastCoordinate.getLongitude());

        List<Coordinate> waypoints = new ArrayList<>();

        for(int i = 1; i < coordinates.size() - 1; i++){
            Coordinate coordinate = coordinates.get(i);
            waypoints.add(coordinate);
        }

//        "http://145.48.6.80:3000/directions?origin=" + origin + "&destination=" + destination +
//                                "&mode=walking&waypoints=" + waypoints + "&key=d8170a45-2be1-4e27-86f6-a46502e272ce"
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                buildUrl(origin,destination, "walking", waypoints), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                List<LatLng> latLngs = new ArrayList<>();

                try {

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

    private String buildUrl(LatLng origin, LatLng dest, String directionMode, List<Coordinate> waypoints){
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String str_waypoints = "waypoints=";
        for (int i = 0; i < waypoints.size(); i++){
            Coordinate coordinate = waypoints.get(i);

            str_waypoints += coordinate.getLatitude() + "%2C" + coordinate.getLongitude();

            if (i < waypoints.size() - 1){
                str_waypoints += "&7Cvia:";
            }
        }
        String parameters = str_origin + "&" + str_dest + "&" + mode + "&" + str_waypoints;

        String url = "http://145.48.6.80:3000/directions?" + parameters + "&key=" + "d8170a45-2be1-4e27-86f6-a46502e272ce";

        return url;
    }

//    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
//        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].startLocation.lat,results.routes[0].legs[0].startLocation.lng)).title(results.routes[0].legs[0].startAddress));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].endLocation.lat,results.routes[0].legs[0].endLocation.lng)).title(results.routes[0].legs[0].startAddress).snippet(getEndLocationTitle(results)));
//    }


    public void setMapHandler(MapHandler handler){
        this.mapHandler = handler;
    }
}
