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
public class MainMapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    public static final String TAG = "MapFragment.TAG";
    private final double mOfficialLat = 28.590647;
    private final double mOfficialLong = -81.304510;

    private GoogleMap mMap;

    public static MainMapFragment newInstance() {

        Bundle args = new Bundle();
        MainMapFragment fragment = new MainMapFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowClickListener(this);

        zoomInCamera();
        addMapMarker();
    }
    private void addMapMarker(){
        if(mMap == null){
            return;
        }

        MarkerOptions options = new MarkerOptions();
        options.title("Fullsail university");
        options.snippet("Building 4");

        LatLng officalLocation = new LatLng(mOfficialLat,mOfficialLong);
        options.position(officalLocation);

        mMap.addMarker(options);
    }
    private void zoomInCamera(){
        if(mMap == null){
            return;
        }

        LatLng officalLocation = new LatLng(mOfficialLat,mOfficialLong);
        CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(officalLocation, 16);
        mMap.animateCamera(cameraMovement);
    }

    @Override
    public View getInfoWindow(Marker marker) { return null;}

    @Override
    public View getInfoContents(Marker marker) {
        View contents = LayoutInflater.from(getActivity()).inflate(R.layout.info_window, null);

        ((TextView)contents.findViewById(R.id.title)).setText(marker.getTitle());
        ((TextView)contents.findViewById(R.id.snippet)).setText(marker.getSnippet());
        return contents;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
