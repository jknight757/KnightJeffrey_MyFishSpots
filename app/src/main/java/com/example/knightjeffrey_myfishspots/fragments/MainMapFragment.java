package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.models.DataBaseAdapter;
import com.example.knightjeffrey_myfishspots.models.DataBaseHelper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainMapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener {

    public static final String TAG = "MainMapFragment.TAG";
    private Double mStartLat;
    private Double mStartLong ;

    private Double searchLat;
    private Double searchLong;
    private int locationID;
    Cursor cursor;
    String name;
    String latStr;
    String longStr;


    private Marker currentMarker;
    private ArrayList<Marker> markers;

    private static final int HOME_SCREEN_STATE = 10;
    private static final int ADD_STATE_START = 20;
    private static final int ADD_STATE_SEARCH = 30;
    private static final int ADDED_HOME_STATE = 40;

    private static final String STATE_KEY = "STATE_KEY";
    private static final String LAT_KEY = "LAT_KEY";
    private static final String LONG_KEY = "LONG_KEY";
    private static final String ID_KEY = "ID_KEY";
    private int currentState;

    private LatLongListener listener;
    private WindowClickListener windowListener;

    private GoogleMap mMap;

    public static MainMapFragment newInstance(int _state) {

        Bundle args = new Bundle();
        MainMapFragment fragment = new MainMapFragment();
        args.putInt(STATE_KEY,_state);

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

    public static MainMapFragment newInstance(int _state, int _id) {

        Bundle args = new Bundle();
        args.putInt(STATE_KEY,_state);
        args.putInt(ID_KEY,_id);
        MainMapFragment fragment = new MainMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface LatLongListener{
        void longPress(LatLng location);

    }

    public interface WindowClickListener{
        void windowClicked(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof LatLongListener){

            listener = (LatLongListener) context;

        }else if(context instanceof WindowClickListener){

            windowListener = (WindowClickListener)context;
        }
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getMapAsync(this);


        if(getArguments() != null){

            markers = new ArrayList<>();

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
                case ADDED_HOME_STATE:
                    locationID = getArguments().getInt(ID_KEY);
                    currentState = ADDED_HOME_STATE;
                    break;
            }

        }
    }

    public void queryDataBase(){

        DataBaseHelper dbh = DataBaseHelper.getInstance(getContext());
        cursor = dbh.getAll();
        int x = cursor.getCount();
        if(x > 0){
            cursor.moveToFirst();

            name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_NAME));
            latStr = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LATITUDE));
            longStr = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LONGITUDE));
            mStartLat = Double.parseDouble(latStr);
            mStartLong = Double.parseDouble(longStr);

            for(int i =0; i < x; i++){
                name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_NAME));
                latStr = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LATITUDE));
                longStr = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LONGITUDE));
                Double latitude = Double.parseDouble(latStr);
                Double longitude = Double.parseDouble(longStr);
                locationID = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUMN_ID));
                addMapMarker(latitude, longitude, name, locationID);
                cursor.moveToNext();
            }

            if(currentState == HOME_SCREEN_STATE){
                zoomInCamera(mStartLat,mStartLong);
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
        Double latitude;
        Double longitude;


        switch (currentState){
            case HOME_SCREEN_STATE:
                 queryDataBase();
                break;
            case ADD_STATE_START:
                mMap.setOnMapLongClickListener(this);
                break;
            case ADD_STATE_SEARCH:
                mMap.setOnMapLongClickListener(this);
                addMapMarker(searchLat,searchLong);
                zoomInCamera(searchLat,searchLong);
                break;
            case ADDED_HOME_STATE:
                 queryDataBase();
                 latitude = Double.parseDouble(latStr);
                 longitude = Double.parseDouble(longStr);
                 zoomInCamera(latitude,longitude);
                break;
        }

    }

    // add marker to the map ...
    private void addMapMarker(Double searchLat, Double searchLong){
        if(mMap == null){
            return;
        }

        MarkerOptions options = new MarkerOptions();
        String coordinateStr = searchLat+ ", " + searchLong;
        options.title(coordinateStr);

        LatLng officalLocation = new LatLng(searchLat,searchLong);
        options.position(officalLocation);

        currentMarker = mMap.addMarker(options);
    }
    private void addMapMarker(Double storedLat, Double storedLong, String name, int _id){
        if(mMap == null){
            return;
        }
        MarkerOptions options = new MarkerOptions();
        options.title(name);
        LatLng officalLocation = new LatLng(storedLat,storedLong);
        options.position(officalLocation);
        currentMarker = mMap.addMarker(options);
        currentMarker.setTag(_id);
        markers.add(currentMarker);


    }

    // zoom in a specific map location..
    public void zoomInCamera(Double searchLat, Double searchLong){
        if(mMap == null){
            return;
        }

        LatLng officalLocation = new LatLng(searchLat,searchLong);
        CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(officalLocation, 12);
        mMap.animateCamera(cameraMovement);
    }

    @Override
    public View getInfoWindow(Marker marker) { return null;}

    // inflate info window layout set text views to marker properties
    @Override
    public View getInfoContents(Marker marker) {

            View contents = LayoutInflater.from(getActivity()).inflate(R.layout.info_window, null);

            ((TextView) contents.findViewById(R.id.title)).setText(marker.getTitle());
            ((TextView) contents.findViewById(R.id.snippet)).setText(marker.getSnippet());

            return contents;

    }

    // handle marker/ info window click click
    @Override
    public void onInfoWindowClick(Marker marker) {

        switch (currentState){
            case ADD_STATE_SEARCH:
                marker.hideInfoWindow();
                break;
            case ADD_STATE_START:
                marker.hideInfoWindow();
                break;
            case HOME_SCREEN_STATE:
                getMarkerTag(marker);

                break;
            case ADDED_HOME_STATE:
                getMarkerTag(marker);
                break;
        }
    }

    public void getMarkerTag(Marker marker){

        Marker clickedMarker = markers.get(markers.indexOf(marker));

        int id = (int)clickedMarker.getTag();
        Toast.makeText(getContext(), "Marker ID: " + id,Toast.LENGTH_SHORT).show();
        windowListener.windowClicked(id);

    }



    // when the map is long clicked a marker is added at that location
    // previous marker is removed
    // map zooms on new marker
    @Override
    public void onMapLongClick(LatLng latLng) {
        if(currentMarker != null){
           currentMarker.remove();
        }
        listener.longPress(latLng);
        addMapMarker(latLng.latitude,latLng.longitude);
        zoomInCamera(latLng.latitude,latLng.longitude);



    }
}
