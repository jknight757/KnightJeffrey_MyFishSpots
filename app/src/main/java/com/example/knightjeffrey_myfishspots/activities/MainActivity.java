package com.example.knightjeffrey_myfishspots.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.fragments.LoginFragment;
import com.example.knightjeffrey_myfishspots.fragments.MainListFragment;
import com.example.knightjeffrey_myfishspots.fragments.MainMapFragment;
import com.example.knightjeffrey_myfishspots.fragments.SignUpFragment;
import com.example.knightjeffrey_myfishspots.fragments.SpotDetail;
import com.example.knightjeffrey_myfishspots.models.DataBaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener, MainListFragment.MenuClickListener, MainMapFragment.WindowClickListener, SpotDetail.SpotDetailListener {

    LoginFragment fragmentLogin;
    SignUpFragment fragmentSignUp;
    MainMapFragment mapFragment;
    MainListFragment listFragment;
    SpotDetail spotDetail;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private static final int HOME_SCREEN_STATE = 10;
    private static final int ADDED_HOME_STATE = 40;
    private static final int ADD_SPOT_REQUEST_CODE = 001;
    private static final String EXTRA_ID = "EXTRA_ID";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        fragmentLogin = LoginFragment.newInstance();

        if(!checkUserLoginState()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_fragment_container, fragmentLogin, LoginFragment.TAG).commit();
        }else{
            mapFragment = MainMapFragment.newInstance(HOME_SCREEN_STATE);
            // add new fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map_fragment_container, mapFragment,MainMapFragment.TAG).commit();

            listFragment = MainListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_fragment_container, listFragment,MainListFragment.TAG).commit();
        }



    }

    public boolean checkUserLoginState(){
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) { return true; }

        return false;
    }


    // interface callback method
    // lets the activity know that the log in request has been granted
    @Override
    public void LoginGranted() {
        // remove old fragment
        getSupportFragmentManager().beginTransaction()
                .remove(fragmentLogin).commit();

        // add new fragment
        mapFragment = MainMapFragment.newInstance(HOME_SCREEN_STATE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container, mapFragment, MainMapFragment.TAG).commit();

        listFragment = MainListFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_fragment_container, listFragment, MainListFragment.TAG).commit();
    }

    @Override
    public void RegisterClicked() {
        // remove old fragment
        getSupportFragmentManager().beginTransaction()
                .remove(fragmentLogin).commit();

        // add new fragment
        fragmentSignUp = SignUpFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_fragment_container,fragmentSignUp,SignUpFragment.TAG).commit();
    }

    @Override
    public void SignUpGranted() {
        // remove old fragment
        getSupportFragmentManager().beginTransaction()
                .remove(fragmentSignUp).commit();

        // add new fragment
        mapFragment = MainMapFragment.newInstance(HOME_SCREEN_STATE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container, mapFragment, MainMapFragment.TAG).commit();

        listFragment = MainListFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_fragment_container, listFragment, MainListFragment.TAG).commit();
    }

    @Override
    public void addClicked() {
        Intent addIntent = new Intent(this, AddAndViewActivity.class);
        startActivityForResult(addIntent, ADD_SPOT_REQUEST_CODE);
    }

    // call back method, invoked by MainList fragment
    // passes location id of clicked list item
    @Override
    public void listItemClicked(int _id) {
        DataBaseHelper dbh = DataBaseHelper.getInstance(this);
        currentUser = mAuth.getCurrentUser();
        String uId = currentUser.getUid();
        Cursor cursor = dbh.getLocationByID(_id,uId);

        cursor.moveToFirst();
        String latStr = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LATITUDE));
        String longStr = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LONGITUDE));
        Double latitude = Double.parseDouble(latStr);
        Double longitude = Double.parseDouble(longStr);


        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MainMapFragment.TAG);
        if(fragment instanceof MainMapFragment){
            mapFragment = (MainMapFragment) fragment;
        }
        mapFragment.zoomInCamera(latitude,longitude);



    }

    // invoked when returning from AddAndViewActivity or CatchesActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case RESULT_OK:

                int id = data.getIntExtra(EXTRA_ID, -1);
                if (id != -1) {
                    mapFragment = MainMapFragment.newInstance(ADDED_HOME_STATE, id);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.map_fragment_container, mapFragment, MainMapFragment.TAG).commit();

                    listFragment = MainListFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.list_fragment_container, listFragment, MainListFragment.TAG).commit();

                }
                break;
            case RESULT_CANCELED:
                refreshHomeScreen();
                break;
        }


    }


    // call back method, invoked by the MainMap fragment
    // passes data to and displays SpotDetail fragment
    @Override
    public void windowClicked(int id) {

        // remove map fragment
        getSupportFragmentManager().beginTransaction()
                .remove(mapFragment).commit();
        // remove list fragment
        getSupportFragmentManager().beginTransaction()
                .remove(listFragment).commit();

        // add spot detail fragment
         spotDetail = SpotDetail.newInstance(id);
         getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, spotDetail, SpotDetail.TAG).commit();
    }

    // callback method, invoked by the SpotDetail fragment
    // tells activity to display homescreen fragments
    @Override
    public void returnHomeSD() {
        // remove spot detail fragment
        getSupportFragmentManager().beginTransaction()
                .remove(spotDetail).commit();

        refreshHomeScreen();
    }

    public void refreshHomeScreen(){

        // add home screen fragments
        mapFragment = MainMapFragment.newInstance(HOME_SCREEN_STATE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container, mapFragment, MainMapFragment.TAG).commit();

        listFragment = MainListFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_fragment_container, listFragment, MainListFragment.TAG).commit();
    }
}
