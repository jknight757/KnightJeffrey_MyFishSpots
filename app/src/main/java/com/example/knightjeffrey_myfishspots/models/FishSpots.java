package com.example.knightjeffrey_myfishspots.models;

import com.google.android.gms.maps.model.Marker;

public class FishSpots {
    private Marker marker;
    private String name;
    private String description;


    public FishSpots(Marker marker, String name, String description) {
        this.marker = marker;
        this.name = name;
        this.description = description;
    }
    public FishSpots(Marker marker) {
        this.marker = marker;
        this.name = marker.getTitle();
        this.description = marker.getSnippet();
    }
}
