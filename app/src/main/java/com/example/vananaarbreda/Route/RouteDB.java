package com.example.vananaarbreda.Route;

import java.util.ArrayList;

public class RouteDB {

    private static RouteDB instance;

    private RouteDB(){

    }

    public static RouteDB getInstance(){
        if (instance == null){
            instance = new RouteDB();
        }

        return instance;
    }

    public ArrayList<Route> getRoutes(){
        return null;
    }
}
