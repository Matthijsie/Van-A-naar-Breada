package com.example.vananaarbreda.Map;

public class GPSHandler {

    private static GPSHandler instance;

    private MapHandler mapHandler;

    private GPSHandler(){

    }

    public static GPSHandler getInstance(){
        if (instance == null){
            instance = new GPSHandler();
        }

        return instance;
    }

    public void setMapHandler(MapHandler handler){

    }
}
