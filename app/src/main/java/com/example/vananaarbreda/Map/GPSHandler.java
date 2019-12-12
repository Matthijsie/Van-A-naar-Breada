package com.example.vananaarbreda.Map;

import android.content.Context;
import android.location.Location;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.vananaarbreda.Route.Coordinate;
import com.example.vananaarbreda.Route.Route;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public void requestRoute(Route route){

        List<Coordinate> coordinates = route.getCoordinates();

        Coordinate firstCoordinate = coordinates.get(0);
        Coordinate lastCoordinate = coordinates.get(coordinates.size() -1);

        String origin = firstCoordinate.getLatitude() + "," + firstCoordinate.getLongitude();
        String destination = lastCoordinate.getLatitude() + "," + lastCoordinate.getLongitude();

        String waypoints = "";

        for(int i = 1; i < coordinates.size() - 1; i++){
            Coordinate coordinate = coordinates.get(i);
            waypoints += coordinate.getLatitude() + "%2C" + coordinate.getLongitude() + ",";
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                "http://145.48.6.80:3000/directions?origin=" + origin + "&destination=" + destination +
                        "&mode=walking&waypoints=" + waypoints + "&key=d8170a45-2be1-4e27-86f6-a46502e272ce", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
    }


    public void setMapHandler(MapHandler handler){
        this.mapHandler = handler;
    }
}
