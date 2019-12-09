package com.example.vananaarbreda.Route;

import java.util.List;

public class Coordinate {

    private double longitude;
    private double latitude;
    private Sight sight;

    public Coordinate(double longitude, double latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Sight getSight(){
        return null;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
