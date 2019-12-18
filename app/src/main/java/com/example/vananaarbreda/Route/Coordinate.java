package com.example.vananaarbreda.Route;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Coordinate {

    private double longitude;
    private double latitude;
    private Sight sight;

    public Coordinate(){

    }

    public Coordinate(LatLng coords, String name, String desc, int ID, boolean isVisited, String[] photos) {
        this.longitude = coords.longitude;
        this.latitude = coords.latitude;
        this.sight = new Sight(ID, name, desc, isVisited);
    }

    public Coordinate(double longitude, double latitude, Sight sight) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.sight = sight;
    }

    public Sight getSight(){
        return null;
    }

    public void setSight(Sight s) {this.sight = s;}

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", sightID=" + sight.getID() +
                ", sightName=" + sight.getName()+
                ", sightDesc=" + sight.getDescription() +
                ", sightSeen=" + sight.isVisited() +
                '}';
    }
}
