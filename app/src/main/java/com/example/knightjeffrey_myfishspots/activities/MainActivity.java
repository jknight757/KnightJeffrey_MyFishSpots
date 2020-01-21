package com.example.knightjeffrey_myfishspots.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.fragments.LoginFragment;
import com.example.knightjeffrey_myfishspots.fragments.MainListFragment;
import com.example.knightjeffrey_myfishspots.fragments.MainMapFragment;
import com.example.knightjeffrey_myfishspots.fragments.SignUpFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener {

    LoginFragment fragmentLogin;
    SignUpFragment fragmentSignUp;
    private FirebaseAuth mAuth;
    private static final int HOME_SCREEN_STATE = 0005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("TAG", "onCreate: ");
        //TODO:
        mAuth = FirebaseAuth.getInstance();

        fragmentLogin = LoginFragment.newInstance();
        if(!checkUserLoginState()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_fragment_container, fragmentLogin, LoginFragment.TAG).commit();
        }else{
            // add new fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map_fragment_container, MainMapFragment.newInstance(HOME_SCREEN_STATE),null).commit();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_fragment_container, MainListFragment.newInstance(),null).commit();
        }


    }

    public boolean checkUserLoginState(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
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
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container, MainMapFragment.newInstance(HOME_SCREEN_STATE),null).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.list_fragment_container, MainListFragment.newInstance(),null).commit();
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
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container, MainMapFragment.newInstance(HOME_SCREEN_STATE),null).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.list_fragment_container, MainListFragment.newInstance(),null).commit();
    }
}
