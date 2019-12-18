package com.example.vananaarbreda.Route;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Sight implements Serializable {

    private int ID;
    private String name;
    private String description;
    private String information;
    private ArrayList<Bitmap> images;
    private boolean isVisited;

    public Sight(String name, String description){
        this.name = name;
        this.description = description;
        this.isVisited = false;
    }

    public String getName() {
        return name;
    }

    public Sight(int ID, String name, String description, boolean isVisited) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        //this.images = images;
        this.isVisited = isVisited;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.images = images;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
