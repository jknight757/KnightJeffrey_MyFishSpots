package com.example.knightjeffrey_myfishspots;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.knightjeffrey_myfishspots.fragments.MainMapFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.map_fragment_container, MainMapFragment.newInstance(),null).commit();
    }
}
