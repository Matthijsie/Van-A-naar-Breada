package com.example.vananaarbreda.Route;

import androidx.annotation.NonNull;

import java.util.List;

public class Coordinate {

    private double longitude;
    private double latitude;
    private Sight sight;

    public Coordinate(double latitude, double longitude, Sight sight){
        this.latitude = latitude;
        this.longitude = longitude;
        this.sight = sight;
    }

    public Sight getSight(){
        return sight;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
