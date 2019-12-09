package com.example.vananaarbreda.Route;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class RouteDB extends SQLiteOpenHelper {

    private static RouteDB instance;
    private static String DATABASE_NAME = "BredaRoutes";
    private static int DATABASE_VERSION = 1;

    private RouteDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static RouteDB getInstance(Context context){
        if (instance == null){
            instance = new RouteDB(context);
        }

        return instance;
    }

    public ArrayList<Route> getRoutes(){
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
