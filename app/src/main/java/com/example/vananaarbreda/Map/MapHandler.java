package com.example.vananaarbreda.Map;

import com.example.vananaarbreda.Route.Coordinate;

public class MapHandler {

    private static MapHandler instance;

    private MapHandler(){

    }

    public static MapHandler getInstance(){
        if (instance == null){
            instance = new MapHandler();
        }

        return instance;
    }

    public void setLocation(Coordinate location){

    }
}
