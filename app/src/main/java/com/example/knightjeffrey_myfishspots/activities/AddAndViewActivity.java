package com.example.knightjeffrey_myfishspots.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddAndViewActivity extends AppCompatActivity implements View.OnClickListener, MainMapFragment.LatLongListener, NewSpotDetail.DetailListener {

    NewSpotDetail newSpotDetailFragment;
    FrameLayout firstLayout;
    Button nextLayout;
    EditText latInput;
    EditText longInput;
    Double latitude;
    Double longitude;

    private FirebaseAuth mAuth;
    private static final int ADD_STATE_START = 0006;
    private static final int ADD_STATE_SEARCH = 0007;
    private static final String ID_KEY = "ID_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_view);
        //getActionBar().setTitle("New Spot");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container, MainMapFragment.newInstance(ADD_STATE_START),null).commit();

        firstLayout = findViewById(R.id.main_fragment_container);
        nextLayout = findViewById(R.id.btn_next);
        findViewById(R.id.search_btn).setOnClickListener(this);
        nextLayout.setOnClickListener(this);
        latInput = findViewById(R.id.lat_input);
        longInput = findViewById(R.id.long_input);

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

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.map_fragment_container, MainMapFragment.newInstance(ADD_STATE_SEARCH, latitude, longitude), null).commit();

                }
                break;
            case R.id.btn_next:
                if(latitude != null && longitude != null){
                    firstLayout.setVisibility(View.GONE);
                    nextLayout.setVisibility(View.GONE);
                    newSpotDetailFragment = NewSpotDetail.newInstance(latitude,longitude);
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
    }

    @Override
    public void confirmAdd(FishSpots spot) {

        Toast.makeText(this,"2 back at activity",Toast.LENGTH_SHORT).show();

        DataBaseHelper dbh = DataBaseHelper.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            Toast.makeText(this,"3 user Logged In",Toast.LENGTH_SHORT).show();
            String userID = currentUser.getUid();
            dbh.insertLocation(spot, userID);

//            Cursor c = dbh.getAll();
//            c.moveToLast();
//            int id = c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_ID));
//            spot.setLocationId(id);

            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }

    }

    @Override
    public void newLatLong() {
        getSupportFragmentManager().beginTransaction().remove(newSpotDetailFragment).commit();
        firstLayout.setVisibility(View.VISIBLE);
        nextLayout.setVisibility(View.VISIBLE);
    }
}
