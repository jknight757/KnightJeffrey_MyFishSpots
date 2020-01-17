package com.example.knightjeffrey_myfishspots.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.fragments.LoginFragment;
import com.example.knightjeffrey_myfishspots.fragments.MainMapFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, LoginFragment.newInstance(),null).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.map_fragment_container, MainMapFragment.newInstance(),null).commit();


    }
}
