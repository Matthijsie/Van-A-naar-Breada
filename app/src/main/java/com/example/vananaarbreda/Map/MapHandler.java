package com.example.vananaarbreda.Map;

import com.example.vananaarbreda.Route.Coordinate;
import com.example.vananaarbreda.Route.Route;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapHandler {

    private static MapHandler instance;

    private GoogleMap googleMap;

    private MapHandler(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

    public static MapHandler getInstance(GoogleMap googleMap){
        if (instance == null){
            instance = new MapHandler(googleMap);
        }

        return instance;
    }

    public void setLocation(Coordinate location){

    }

    public void buildRoute(Route route){
        for(Coordinate coordinate : route.getCoordinates()){
            googleMap.addMarker(new MarkerOptions().position(new LatLng(coordinate.getLatitude(), coordinate.getLongitude())));
        }
    }
}
