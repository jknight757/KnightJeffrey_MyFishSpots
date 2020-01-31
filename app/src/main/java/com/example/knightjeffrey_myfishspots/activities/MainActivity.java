package com.example.knightjeffrey_myfishspots.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.fragments.LoginFragment;
import com.example.knightjeffrey_myfishspots.fragments.MainListFragment;
import com.example.knightjeffrey_myfishspots.fragments.MainMapFragment;
import com.example.knightjeffrey_myfishspots.fragments.NewSpotDetail;
import com.example.knightjeffrey_myfishspots.fragments.SignUpFragment;
import com.example.knightjeffrey_myfishspots.fragments.SpotDetailFragment;
import com.example.knightjeffrey_myfishspots.models.DataBaseHelper;
import com.example.knightjeffrey_myfishspots.models.RemoteDataManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener, MainListFragment.MenuClickListener, MainMapFragment.WindowClickListener, SpotDetailFragment.SpotDetailListener {

    LoginFragment fragmentLogin;
    SignUpFragment fragmentSignUp;
    MainMapFragment mapFragment;
    MainListFragment listFragment;
    SpotDetailFragment spotDetailFragment;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private static final int HOME_SCREEN_STATE = 10;
    private static final int ADDED_HOME_STATE = 40;
    private static final int ADD_SPOT_REQUEST_CODE = 99;
    private static final int ADD_CATCH_REQUEST_CODE = 300;
    private static final int VIEW_CATCH_REQUEST_CODE = 355;
    private static final int EDIT_SPOT_REQUEST_CODE = 77;
    private static final int REQUEST_IMAGE = 0x0010;

    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_CATCH_ID = "EXTRA_CATCH_ID";
    private static final String EXTRA_INTENT_CODE = "EXTRA_INTENT_CODE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkInternet()) {
            mAuth = FirebaseAuth.getInstance();
            fragmentLogin = LoginFragment.newInstance();

            if (!checkUserLoginState()) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_fragment_container, fragmentLogin, LoginFragment.TAG).commit();
            } else {
                mapFragment = MainMapFragment.newInstance(HOME_SCREEN_STATE);
                // add new fragment
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.map_fragment_container, mapFragment, MainMapFragment.TAG).commit();

                listFragment = MainListFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.list_fragment_container, listFragment, MainListFragment.TAG).commit();
            }
        }else {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("No Internet");
            alert.setMessage("You must be connected to the internet to use this application");

            alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();


        }

        RemoteDataManager remoteMgr = new RemoteDataManager(getApplicationContext());
        //remoteMgr.getLocalData();
        remoteMgr.getRemoteData();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public boolean checkUserLoginState(){
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) { return true; }

        return false;
    }


    ///// LOGIN FRAGMENT CALLBACK METHODS /////
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
    ///// ^^^ LOGIN FRAGMENT CALLBACK METHODS ^^^ /////

    ///// SIGNUP FRAGMENT CALLBACK METHODS /////
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
    ///// ^^^ SIGNUP FRAGMENT CALLBACK METHODS ^^^ /////

    ///// MAINLIST FRAGMENT CALLBACK METHODS /////
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

    @Override
    public void addClicked() {
        Intent addIntent = new Intent(this, AddAndViewActivity.class);
        startActivityForResult(addIntent, ADD_SPOT_REQUEST_CODE);
    }
    //// ^^^ MAINLIST FRAGMENT CALLBACK METHODS ^^^ /////

    ///// MAINMAP FRAGMENT CALLBACK METHODS /////
    // passes data to and displays SpotDetailFragment fragment
    @Override
    public void windowClicked(int id) {

        // remove map fragment
        getSupportFragmentManager().beginTransaction()
                .remove(mapFragment).commit();
        // remove list fragment
        getSupportFragmentManager().beginTransaction()
                .remove(listFragment).commit();

        // add spot detail fragment
         spotDetailFragment = SpotDetailFragment.newInstance(id);
         getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, spotDetailFragment, SpotDetailFragment.TAG).commit();
    }
    ///// ^^^ MAINMAP FRAGMENT CALLBACK METHODS ^^^ /////

    ///// SPOT DETAIL FRAGMENT CALLBACK METHODS /////
    // tells activity to display homescreen fragments
    @Override
    public void returnHomeSD() {
        // remove spot detail fragment
        getSupportFragmentManager().beginTransaction()
                .remove(spotDetailFragment).commit();

        refreshHomeScreen();
    }

    @Override
    public void newCatch(int id) {
        Intent catchesIntent = new Intent(this, CatchesActivity.class);
        catchesIntent.putExtra(EXTRA_ID,id);
        catchesIntent.putExtra(EXTRA_INTENT_CODE,ADD_CATCH_REQUEST_CODE);
        startActivityForResult(catchesIntent,ADD_CATCH_REQUEST_CODE);
    }

    @Override
    public void editSpot(int id) {
        Intent editIntent = new Intent(this, AddAndViewActivity.class);
        editIntent.putExtra(EXTRA_ID,id);
        startActivityForResult(editIntent,EDIT_SPOT_REQUEST_CODE);

    }

    @Override
    public void clickSpot(int spotId, int catchId) {
        Intent catchesIntent = new Intent(this, CatchesActivity.class);
        catchesIntent.putExtra(EXTRA_ID,spotId);
        catchesIntent.putExtra(EXTRA_CATCH_ID,catchId);
        catchesIntent.putExtra(EXTRA_INTENT_CODE,VIEW_CATCH_REQUEST_CODE);
        startActivityForResult(catchesIntent,VIEW_CATCH_REQUEST_CODE);
    }
    ///// ^^^ SPOT DETAIL FRAGMENT CALLBACK METHODS ^^^ /////

    // invoked when returning from AddAndViewActivity or CatchesActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case RESULT_OK:

                if(requestCode == ADD_SPOT_REQUEST_CODE) {
                    int id = data.getIntExtra(EXTRA_ID, -1);
                    if (id != -1) {
                        mapFragment = MainMapFragment.newInstance(ADDED_HOME_STATE, id);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.map_fragment_container, mapFragment, MainMapFragment.TAG).commit();

                        listFragment = MainListFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.list_fragment_container, listFragment, MainListFragment.TAG).commit();

                    }
                }
                if(requestCode == ADD_CATCH_REQUEST_CODE){
                    int id = data.getIntExtra(EXTRA_ID, -1);
                    if(id != -1){
                        // remove map fragment
                        getSupportFragmentManager().beginTransaction()
                                .remove(mapFragment).commit();
                        // remove list fragment
                        getSupportFragmentManager().beginTransaction()
                                .remove(listFragment).commit();
                        getSupportFragmentManager().beginTransaction()
                                .remove(spotDetailFragment).commit();

                        // add spot detail fragment
                        spotDetailFragment = SpotDetailFragment.newInstance(id);
                        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, spotDetailFragment, SpotDetailFragment.TAG).commit();
                    }
                }
                break;
            case RESULT_CANCELED:
                if(requestCode == ADD_SPOT_REQUEST_CODE){

                }
                if(requestCode == EDIT_SPOT_REQUEST_CODE){
                    getSupportFragmentManager().beginTransaction().remove(spotDetailFragment).commit();
                    refreshHomeScreen();
                }
                if(requestCode == REQUEST_IMAGE){

                }
                break;
        }

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

    public boolean checkInternet(){
        boolean isConnected = false;
        ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(connectMgr != null){
                NetworkCapabilities capabilities =
                        connectMgr.getNetworkCapabilities(connectMgr.getActiveNetwork());
                if(capabilities != null){
                    if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                        isConnected = true;
                    }else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                        isConnected = true;
                    }
                }
            }
        }else{
            if(connectMgr != null){
                NetworkInfo activeNetwork = connectMgr.getActiveNetworkInfo();
                if(activeNetwork != null){
                    if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                        isConnected = true;
                    }else if ( activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                        isConnected = true;
                    }
                }
            }
        }
        return isConnected;


    }
}
