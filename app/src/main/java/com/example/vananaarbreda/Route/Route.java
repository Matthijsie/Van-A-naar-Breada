package com.example.vananaarbreda.Route;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private String name;
    private List<Coordinate> coordinates;

    public Route(){
        coordinates = new ArrayList<>();
    }

    public Sight getSights(){
        return null;
    }

    public List<Coordinate> getCoordinates(){
        return coordinates;
    }

    public void addCoordinate(Coordinate coordinate){
        coordinates.add(coordinate);
    }
}
