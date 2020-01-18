package com.example.knightjeffrey_myfishspots.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.fragments.LoginFragment;
import com.example.knightjeffrey_myfishspots.fragments.MainMapFragment;
import com.example.knightjeffrey_myfishspots.fragments.SignUpFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener {

    LoginFragment fragmentLogin;
    SignUpFragment fragmentSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentLogin = LoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_fragment_container, fragmentLogin,LoginFragment.TAG).commit();





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
                .add(R.id.map_fragment_container, MainMapFragment.newInstance(),null).commit();
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
                .add(R.id.map_fragment_container, MainMapFragment.newInstance(),null).commit();
    }
}
