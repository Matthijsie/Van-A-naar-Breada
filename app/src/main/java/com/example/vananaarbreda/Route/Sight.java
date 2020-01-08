package com.example.vananaarbreda.Route;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sight implements Serializable {

    private int ID;
    private String name;
    private String description;
    private String descriptionEN;
    private String information;
    private ArrayList<Bitmap> bitmapImages;
    private List<String> stringImageNames;
    private boolean isVisited;

    public Sight(String name, String description){
        this.name = name;
        this.description = description;
        this.isVisited = false;
        this.bitmapImages = new ArrayList<>();
        this.stringImageNames = new ArrayList<>();
    }

    public Sight(int ID, String name, String description, String descriptionEN, boolean isVisited, String[] photos) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.isVisited = isVisited;
        this.descriptionEN = descriptionEN;
        this.stringImageNames = Arrays.asList(photos);
        this.bitmapImages = new ArrayList<>();
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

    public ArrayList<Bitmap> getBitmapImages() {
        return bitmapImages;
    }

    public void setBitmapImages(ArrayList<Bitmap> bitmapImages) {
        this.bitmapImages = bitmapImages;
    }

    public List<String> getStringImageNames() {
        return stringImageNames;
    }

    public void setStringImageNames(ArrayList<String> stringImageNames) {
        this.stringImageNames = stringImageNames;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public String getDescriptionEN() {
        return descriptionEN;
    }

    public void setDescriptionEN(String descriptionEN) {
        this.descriptionEN = descriptionEN;
    }
}
