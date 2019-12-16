package com.example.vananaarbreda.Route;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Sight implements Serializable {

    private String name;
    private String description;
    private ArrayList<Bitmap> images;
    private boolean isVisited;

    public Sight(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
