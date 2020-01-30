package com.example.knightjeffrey_myfishspots.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.fragments.MainMapFragment;
import com.example.knightjeffrey_myfishspots.fragments.NewSpotDetail;
import com.example.knightjeffrey_myfishspots.models.DataBaseHelper;
import com.example.knightjeffrey_myfishspots.models.FishSpots;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddAndViewActivity extends AppCompatActivity implements View.OnClickListener, MainMapFragment.LatLongListener, NewSpotDetail.DetailListener {

    NewSpotDetail newSpotDetailFragment;
    MainMapFragment mapFragment;
    FrameLayout firstLayout;
    Button nextLayout;
    EditText latInput;
    EditText longInput;
    Double latitude;
    Double longitude;

    DataBaseHelper dbh;
    Cursor cursor;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStoreDB;
    FirebaseUser currentUser;

    private static final int ADD_STATE_START = 20;
    private static final int ADD_STATE_SEARCH = 30;
    private static final String EXTRA_ID = "EXTRA_ID";
    private int locationID;

    private boolean editLocation = false;
    private String savedName;
    private String savedDescrip;

    FishSpots spotToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_view);
        //getActionBar().setTitle("New Spot");
        Intent starterIntent = getIntent();

        firstLayout = findViewById(R.id.main_fragment_container);
        nextLayout = findViewById(R.id.btn_next);
        if(starterIntent.hasExtra(EXTRA_ID)){
            locationID = starterIntent.getIntExtra(EXTRA_ID, -1);
            if(locationID != -1) {
                spotToEdit = queryDatabase(locationID);

                if (spotToEdit != null) {

                    firstLayout.setVisibility(View.GONE);
                    nextLayout.setVisibility(View.GONE);
                    newSpotDetailFragment = NewSpotDetail.newInstance(spotToEdit.getName(),
                            spotToEdit.getDescription(), spotToEdit.getCoordinate().latitude,
                            spotToEdit.getCoordinate().longitude);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.top_level_container,newSpotDetailFragment,NewSpotDetail.TAG ).commit();
                } else {
                    Toast.makeText(this, "SPOT NULL", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "ID -1", Toast.LENGTH_SHORT).show();
            }

        }else{
            mapFragment = MainMapFragment.newInstance(ADD_STATE_START);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map_fragment_container, mapFragment,MainMapFragment.TAG).commit();
        }


        findViewById(R.id.search_btn).setOnClickListener(this);

        nextLayout.setOnClickListener(this);
        latInput = findViewById(R.id.lat_input);
        longInput = findViewById(R.id.long_input);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_go_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent resultIntent = new Intent();
        setResult(RESULT_CANCELED, resultIntent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    // when search btn is clicked the edit text inputs are passed to the fragment as coordinates
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.search_btn:
                String latStr = latInput.getText().toString();
                String longStr = longInput.getText().toString();

                if (!latStr.isEmpty() && !longStr.isEmpty()) {
                    latitude = Double.parseDouble(latStr);
                    longitude = Double.parseDouble(longStr);

                    mapFragment = MainMapFragment.newInstance(ADD_STATE_SEARCH,latitude, longitude);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.map_fragment_container, mapFragment,MainMapFragment.TAG).commit();

                }
                break;
            case R.id.btn_next:
                if(latitude != null && longitude != null){
                    firstLayout.setVisibility(View.GONE);
                    nextLayout.setVisibility(View.GONE);

                    if(editLocation){
                        newSpotDetailFragment = NewSpotDetail.newInstance(savedName, savedDescrip,latitude ,longitude);
                        editLocation = false;
                    }else {
                        newSpotDetailFragment = NewSpotDetail.newInstance(latitude,longitude);
                    }
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.top_level_container, newSpotDetailFragment, NewSpotDetail.TAG).commit();
                }
                break;

        }
    }

    @Override
    public void longPress(LatLng location) {
        latitude = location.latitude;
        longitude = location.longitude;
        String latStr = latitude + "";
        String longStr = longitude + "";
        latInput.setText(latStr);
        longInput.setText(longStr);
    }

    @Override
    public void confirmAdd(FishSpots spot) {

        dbh = DataBaseHelper.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userID;
        if (currentUser != null) {
            userID = currentUser.getUid();
            if(spotToEdit != null){
                ContentValues cv = new ContentValues();
                cv.put(DataBaseHelper.COLUMN_USER_ID, userID);
                cv.put(DataBaseHelper.COLUMN_NAME, spot.getName());
                cv.put(DataBaseHelper.COLUMN_LATITUDE, spot.getCoordinate().latitude);
                cv.put(DataBaseHelper.COLUMN_LONGITUDE, spot.getCoordinate().longitude);
                cv.put(DataBaseHelper.COLUMN_DESCRIPTION, spot.getDescription());
                cv.put(DataBaseHelper.COLUMN_DATE, spot.getDate());
                dbh.mDatabase.update(DataBaseHelper.TABLE_NAME_FIRST,cv,"_id="+ locationID,null);


            }else{
                dbh.insertLocation(spot, userID);

            }



            Cursor c = dbh.getAllSpots();
            c.moveToLast();
            int id = c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_ID));
            spot.setLocationId(id);

            fireStoreDB = FirebaseFirestore.getInstance();
            DocumentReference firebaseLocations = fireStoreDB.collection("locations").document(userID.toString());
            firebaseLocations.set(spot).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AddAndViewActivity.this,"Spot Added",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AddAndViewActivity.this,"Unable to add",Toast.LENGTH_SHORT).show();
                }
            });
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                @Override
//                public void onSuccess(DocumentReference documentReference) {
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });

            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_ID, id);
            setResult(RESULT_OK, resultIntent);
            finish();
        }

    }

    @Override
    public void newLatLong(String name, String description) {
        savedName = name;
        savedDescrip = description;
        editLocation = true;

        getSupportFragmentManager().beginTransaction().remove(newSpotDetailFragment).commit();
        firstLayout.setVisibility(View.VISIBLE);
        nextLayout.setVisibility(View.VISIBLE);
    }

    public FishSpots queryDatabase(int id){
        dbh = DataBaseHelper.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String uId = currentUser.getUid();
        cursor = dbh.getLocationByID(id,uId);

        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_NAME));
        String description = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_DESCRIPTION));
        String latStr = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LATITUDE));
        String longStr = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LONGITUDE));
        Double latitude = Double.parseDouble(latStr);
        Double longitude = Double.parseDouble(longStr);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date);

        LatLng coordinate = new LatLng(latitude,longitude);

        return new FishSpots(coordinate,name, description, dateStr);

    }


}
