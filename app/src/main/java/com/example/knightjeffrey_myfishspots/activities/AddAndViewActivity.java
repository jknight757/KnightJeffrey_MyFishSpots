package com.example.knightjeffrey_myfishspots.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.fragments.MainMapFragment;

public class AddAndViewActivity extends AppCompatActivity {

    EditText latInput;
    EditText longInput;
    private static final int ADD_NEW_STATE = 0006;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_view);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container, MainMapFragment.newInstance(),null).commit();

    }
}
