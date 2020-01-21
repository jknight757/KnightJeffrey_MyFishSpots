package com.example.knightjeffrey_myfishspots.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.knightjeffrey_myfishspots.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainMapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener, View.OnClickListener {


    private final double mStartLat = 36.898835;
    private final double mStartLong = -76.090170;

    private static final int HOME_SCREEN_STATE = 0005;
    private static final int ADD_NEW_STATE = 0006;
    private static final String STATE_KEY = "STATE_KEY";
    private int currentState;


    private GoogleMap mMap;

    public static MainMapFragment newInstance(int state) {

        Bundle args = new Bundle();
        MainMapFragment fragment = new MainMapFragment();
        args.putInt(STATE_KEY,state);
        fragment.setArguments(args);
        return fragment;
    }

    public static MainMapFragment newInstance(int state, double _lat, double _long) {

        Bundle args = new Bundle();

        MainMapFragment fragment = new MainMapFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getMapAsync(this);

        if(getArguments() != null){
            switch (getArguments().getInt(STATE_KEY)){
                case HOME_SCREEN_STATE:
                    currentState = HOME_SCREEN_STATE;
                    zoomInCamera();
                    break;
                case ADD_NEW_STATE:
                    currentState = ADD_NEW_STATE;
                    break;
            }



        }
    }

    // do initial map set up when the map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowClickListener(this);

        if(currentState == HOME_SCREEN_STATE){
            addMapMarker(mStartLat, mStartLong);
            zoomInCamera();
        }

    }

    // TODO: change method to accept user input
    // add marker to the map ...
    private void addMapMarker(double searchLat, double searchLong){
        if(mMap == null){
            return;
        }

        MarkerOptions options = new MarkerOptions();
        options.title("Grassy Island");
        options.snippet("Lynnhaven River");

        LatLng officalLocation = new LatLng(searchLat,searchLong);
        options.position(officalLocation);

        mMap.addMarker(options);
    }

    // TODO: change method to zoom on list view click
    // zoom in a specific map location..
    private void zoomInCamera(){
        if(mMap == null){
            return;
        }

        LatLng officalLocation = new LatLng(mStartLat,mStartLong);
        CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(officalLocation, 16);
        mMap.animateCamera(cameraMovement);
    }

    @Override
    public View getInfoWindow(Marker marker) { return null;}

    // inflate info window layout set text views to marker properties
    @Override
    public View getInfoContents(Marker marker) {
        View contents = LayoutInflater.from(getActivity()).inflate(R.layout.info_window, null);

        ((TextView)contents.findViewById(R.id.title)).setText(marker.getTitle());
        ((TextView)contents.findViewById(R.id.snippet)).setText(marker.getSnippet());
        return contents;
    }

    // handle marker/ info window click click
    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onClick(View v) {

    }
}
