package com.example.vananaarbreda.Route;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private int ID;
    private String name;
    private String desc;
    private int duration;
    private List<Coordinate> coordinates;

    public Route(String name){
        coordinates = new ArrayList<>();
        this.name = name;
    }

    public List<Sight> getSights(){
        List<Sight> sights = new ArrayList<>();

        for (Coordinate coordinate : coordinates){
            sights.add(coordinate.getSight());
        }
        return sights;
    }

    public List<Coordinate> getCoordinates(){
        return coordinates;
    }

    public void addCoordinate(Coordinate coordinate){
        coordinates.add(coordinate);
    }

    public String getName(){
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
