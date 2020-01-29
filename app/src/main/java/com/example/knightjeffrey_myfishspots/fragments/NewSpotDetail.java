package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.models.FishSpots;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewSpotDetail extends Fragment implements View.OnClickListener {

    public static final String TAG = "NewSpotDetail.TAG";
    private static final String LAT_KEY = "LAT_KEY";
    private static final String LONG_KEY = "LONG_KEY";
    private static final String NAME_KEY = "NAME_KEY";
    private static final String DESCRIP_KEY = "DESCRIP_KEY";

    Double latitude;
    Double longitude;

    EditText spotName;
    EditText spotDescription;
    private String savedName;
    private String savedDescrip;

    DetailListener listener;

    public NewSpotDetail() {
        // Required empty public constructor
    }

    public static NewSpotDetail newInstance(Double latitude, Double longitude) {

        Bundle args = new Bundle();
        args.putDouble(LAT_KEY, latitude);
        args.putDouble(LONG_KEY, longitude);
        NewSpotDetail fragment = new NewSpotDetail();
        fragment.setArguments(args);
        return fragment;
    }

    public static NewSpotDetail newInstance(String name, String description,Double latitude, Double longitude) {

        Bundle args = new Bundle();
        args.putString(NAME_KEY,name);
        args.putString(DESCRIP_KEY,description);
        args.putDouble(LAT_KEY, latitude);
        args.putDouble(LONG_KEY, longitude);
        NewSpotDetail fragment = new NewSpotDetail();
        fragment.setArguments(args);
        return fragment;
    }

    public interface DetailListener{
        void confirmAdd(FishSpots spot);
        void newLatLong(String name, String description);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DetailListener){
            listener = (DetailListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_spot_first, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null && getView() != null){

            savedName = getArguments().getString(NAME_KEY);
            savedDescrip = getArguments().getString(DESCRIP_KEY);
            latitude = getArguments().getDouble(LAT_KEY);
            longitude = getArguments().getDouble(LONG_KEY);
            String latLongStr = latitude + ", "+ longitude;
            ((TextView)getView().findViewById(R.id.coordinate_output))
                    .setText(latLongStr);

            spotName = getView().findViewById(R.id.name_input);
            spotDescription = getView().findViewById(R.id.description_input);
            getView().findViewById(R.id.add_spot_btn).setOnClickListener(this);
            getView().findViewById(R.id.change_location_btn).setOnClickListener(this);

            if(savedName != null){
                spotName.setText(savedName);
            }

            if(savedDescrip != null){
                spotDescription.setText(savedDescrip);
            }
        }
    }


    @Override
    public void onClick(View v) {

        String name = spotName.getText().toString();
        String description = spotDescription.getText().toString();

        switch (v.getId()){
            case R.id.add_spot_btn:
                LatLng coordinate = new LatLng(latitude,longitude);

                 if(!name.isEmpty()){
                     if(description.isEmpty()){
                         description = "No description";
                     }

                     // get date, format date, create new spot, pass new spot
                     Date date = Calendar.getInstance().getTime();
                     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                     String dateStr = dateFormat.format(date);
                     FishSpots newSpot = new FishSpots(coordinate,name, description, dateStr);
                     listener.confirmAdd(newSpot);
                 }else{
                     Toast.makeText(getContext(),"Please enter a name",Toast.LENGTH_SHORT).show();
                 }

                break;
            case R.id.change_location_btn:
                if(name.isEmpty()){
                    name = null;
                }

                if(description.isEmpty()){
                    description = null;
                }
                listener.newLatLong(name, description);
                break;
        }
    }


}
