package com.example.vananaarbreda.Route;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private String name;
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
}
