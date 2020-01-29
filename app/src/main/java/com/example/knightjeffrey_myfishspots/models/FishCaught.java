package com.example.knightjeffrey_myfishspots.models;

import com.google.android.gms.maps.model.Marker;

public class FishCaught {
    private int spotId;
    private int catchId;
    private String species;
    private double weight;
    private double length;
    private String lure;
    private String tide;
    private String method;
    private String imgPath;
    private String date;

    public FishCaught(int spotId,  String species, double weight, double length, String lure, String tide, String method, String imgPath, String date) {
        this.spotId = spotId;
        this.species = species;
        this.weight = weight;
        this.length = length;
        this.lure = lure;
        this.tide = tide;
        this.method = method;
        this.imgPath = imgPath;
        this.date = date;
    }

    public int getSpotId() {
        return spotId;
    }

    public int getCatchId() {
        return catchId;
    }

    public String getSpecies() {
        return species;
    }

    public double getWeight() {
        return weight;
    }

    public double getLength() {
        return length;
    }

    public String getLure() {
        return lure;
    }

    public String getTide() {
        return tide;
    }

    public String getMethod() {
        return method;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getDate() {
        return date;
    }

    public void setCatchId(int catchId) {
        this.catchId = catchId;
    }
}
