package com.example.knightjeffrey_myfishspots.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class FishSpots {
    private int locationId;
    private LatLng coordinate;
    private String name;
    private String description;
    private String date;


    public FishSpots(LatLng coordinate, String name, String description, String date) {
        this.coordinate = coordinate;
        this.name = name;
        this.description = description;
        this.date = date;
    }


    public FishSpots(Marker marker, String date) {
        this.coordinate = marker.getPosition();
        this.name = marker.getTitle();
        this.description = marker.getSnippet();
        this.date = date;
    }

    // Getters //
    public int getLocationId() {
        return locationId;
    }

    public LatLng getCoordinate() {
        return coordinate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
    // Getters //

    // Setters //

    public void setCoordinate(LatLng coordinate) {
        this.coordinate = coordinate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setDate(String date) {
        this.date = date;
    }
    // Setters //
}
