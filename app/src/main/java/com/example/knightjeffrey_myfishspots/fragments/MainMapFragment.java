package com.example.knightjeffrey_myfishspots.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
public class MainMapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener {


    private final double mStartLat = 36.898835;
    private final double mStartLong = -76.090170;

    private Double searchLat;
    private Double searchLong;

    private Marker currentMarker;

    private static final int HOME_SCREEN_STATE = 0005;
    private static final int ADD_STATE_START = 0006;
    private static final int ADD_STATE_SEARCH = 0007;

    private static final String STATE_KEY = "STATE_KEY";
    private static final String LAT_KEY = "LAT_KEY";
    private static final String LONG_KEY = "LONG_KEY";
    private int currentState;


    private GoogleMap mMap;

    public static MainMapFragment newInstance(int state) {

        Bundle args = new Bundle();
        MainMapFragment fragment = new MainMapFragment();
        args.putInt(STATE_KEY,state);

        fragment.setArguments(args);
        return fragment;
    }

    public static MainMapFragment newInstance(int _state, double _lat, double _long) {

        Bundle args = new Bundle();

        MainMapFragment fragment = new MainMapFragment();
        args.putInt(STATE_KEY,_state);
        args.putDouble(LAT_KEY,_lat);
        args.putDouble(LONG_KEY, _long);
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
                    break;
                case ADD_STATE_START:
                    currentState = ADD_STATE_START;
                    break;
                case ADD_STATE_SEARCH:
                    searchLat = getArguments().getDouble(LAT_KEY);
                    searchLong = getArguments().getDouble(LONG_KEY);
                    currentState = ADD_STATE_SEARCH;
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

        switch (currentState){
            case HOME_SCREEN_STATE:
                addMapMarker(mStartLat, mStartLong);
                zoomInCamera(mStartLat,mStartLong);
                break;
            case ADD_STATE_START:
                mMap.setOnMapLongClickListener(this);
                break;
            case ADD_STATE_SEARCH:
                mMap.setOnMapLongClickListener(this);
                addMapMarker(searchLat,searchLong);
                zoomInCamera(searchLat,searchLong);
                break;
        }

    }

    // TODO: change method to accept user input
    // add marker to the map ...
    private void addMapMarker(Double searchLat, Double searchLong){
        if(mMap == null){
            return;
        }

        MarkerOptions options = new MarkerOptions();
        options.title("Grassy Island");
        options.snippet("Lynnhaven River");

        LatLng officalLocation = new LatLng(searchLat,searchLong);
        options.position(officalLocation);

        currentMarker = mMap.addMarker(options);
    }



    // TODO: change method to zoom on list view click
    // zoom in a specific map location..
    private void zoomInCamera(Double searchLat, Double searchLong){
        if(mMap == null){
            return;
        }

        LatLng officalLocation = new LatLng(searchLat,searchLong);
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
    public void onMapLongClick(LatLng latLng) {
        if(currentMarker != null){
           currentMarker.remove();
        }
        addMapMarker(latLng.latitude,latLng.longitude);
        zoomInCamera(latLng.latitude,latLng.longitude);



    }
}
