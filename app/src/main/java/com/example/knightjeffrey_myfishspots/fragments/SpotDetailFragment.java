package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.activities.CatchesActivity;
import com.example.knightjeffrey_myfishspots.models.CatchesTableCursorAdapter;
import com.example.knightjeffrey_myfishspots.models.DataBaseAdapter;
import com.example.knightjeffrey_myfishspots.models.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpotDetailFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = "SpotDetailFragment.TAG";
    private static final String ID_KEY = "ID_KEY";

    private Cursor cursor;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private TextView nameTV;
    private TextView descripTV;
    private TextView coodinateTV;
    private TextView numCatchTV;
    FloatingActionButton newCatchFAB;

    // Location variables
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private int locationID;

    // Fish variables


    SpotDetailListener listener;
    ListView listView;


    public SpotDetailFragment() {
        // Required empty public constructor
    }


    public interface SpotDetailListener{
        void returnHomeSD();
        void newCatch(int id);
        void editSpot(int id);
        void clickSpot(int spotId, int catchId);
    }

    public static SpotDetailFragment newInstance(int _id) {

        Bundle args = new Bundle();
        args.putInt(ID_KEY,_id);
        SpotDetailFragment fragment = new SpotDetailFragment();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SpotDetailListener){
            listener = (SpotDetailListener) context;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_go_home,menu);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null && getView() != null){
            newCatchFAB = getView().findViewById(R.id.new_catch_fab);
            newCatchFAB.setOnClickListener(this);
            getView().findViewById(R.id.edit_btn).setOnClickListener(this);
            locationID = getArguments().getInt(ID_KEY);
            queryDatabase(locationID);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.go_home:
                listener.returnHomeSD();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_catch_fab:
                    listener.newCatch(locationID);
                break;
            case R.id.edit_btn:
                listener.editSpot(locationID);
                break;

        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listener.clickSpot(locationID, position);
        Toast.makeText(getContext(), "Item clicked: "+ position, Toast.LENGTH_SHORT).show();
    }


    private void queryDatabase(int id){

        // get Location data
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

        // get Catches for this location

        cursor = dbh.getAllForLocation(id, Uid);
        updateUI();

    }

    public void updateUI(){

        if(getView() != null){
            nameTV = getView().findViewById(R.id.spot_name_lbl);
            descripTV = getView().findViewById(R.id.description_lbl);
            coodinateTV = getView().findViewById(R.id.coordinate_lbl);
            numCatchTV = getView().findViewById(R.id.num_catches_lbl);

            nameTV.setText(name);
            descripTV.setText(description);
            String coordinateStr = latitude + " ," + longitude;
            coodinateTV.setText(coordinateStr);
            String numStr = cursor.getCount() + "";
            numCatchTV.setText(numStr);
            setUpListView();

        }
    }
    public void setUpListView(){
        DataBaseHelper dbh = DataBaseHelper.getInstance(getContext());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String Uid = currentUser.getUid();

        cursor = dbh.getAllForLocation(locationID, Uid);

        if(cursor.getCount() > 0) {

            listView = getView().findViewById(R.id.fish_caught_lv);
            if (listView != null) {

//                new Handler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        CatchesTableCursorAdapter adapter = new CatchesTableCursorAdapter(getContext(), cursor);
//                        listView.setAdapter(adapter);
//                    }
//                });
                CatchesTableCursorAdapter adapter = new CatchesTableCursorAdapter(getContext(), cursor);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(this);

            }
        }else{
            Toast.makeText(getContext(),"No Stored Spots", Toast.LENGTH_SHORT).show();
        }
    }
}
