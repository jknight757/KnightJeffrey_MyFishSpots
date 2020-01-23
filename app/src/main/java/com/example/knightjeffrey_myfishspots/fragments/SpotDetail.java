package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.models.DataBaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpotDetail extends Fragment {

    public static final String TAG = "SpotDetail.TAG";
    private static final String ID_KEY = "ID_KEY";

    Cursor cursor;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    TextView nameTV;
    TextView descripTV;
    TextView coodinateTV;

    String name;
    String description;
    Double latitude;
    Double longitude;

    public SpotDetail() {
        // Required empty public constructor
    }

    public static SpotDetail newInstance(int _id) {

        Bundle args = new Bundle();
        args.putInt(ID_KEY,_id);
        SpotDetail fragment = new SpotDetail();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spot_detail, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null && getView() != null){
            int id = getArguments().getInt(ID_KEY);
            queryDatabase(id);
        }
    }

    public void queryDatabase(int id){

        DataBaseHelper dbh = DataBaseHelper.getInstance(getContext());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String Uid = currentUser.getUid();
        cursor = dbh.getLocationByID(id,Uid);
        cursor.moveToFirst();

        name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_NAME));
        description = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_DESCRIPTION));
        String latStr = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LATITUDE));
        String longStr = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LONGITUDE));
        latitude = Double.parseDouble(latStr);
        longitude = Double.parseDouble(longStr);
        updateUI();

    }

    public void updateUI(){

        if(getView() != null){
            nameTV = getView().findViewById(R.id.spot_name_lbl);
            descripTV = getView().findViewById(R.id.description_lbl);
            coodinateTV = getView().findViewById(R.id.coordinate_lbl);

            nameTV.setText(name);
            descripTV.setText(description);
            String coordinateStr = latitude + " ," + longitude;
            coodinateTV.setText(coordinateStr);

        }

    }
}
