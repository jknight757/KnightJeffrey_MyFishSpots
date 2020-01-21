package com.example.knightjeffrey_myfishspots.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.fragments.MainMapFragment;

public class AddAndViewActivity extends AppCompatActivity implements View.OnClickListener {

    EditText latInput;
    EditText longInput;

    private static final int ADD_STATE_START = 0006;
    private static final int ADD_STATE_SEARCH = 0007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_view);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container, MainMapFragment.newInstance(ADD_STATE_START),null).commit();

        findViewById(R.id.search_btn).setOnClickListener(this);
        latInput = findViewById(R.id.lat_input);
        longInput = findViewById(R.id.long_input);

    }

    // when search btn is clicked the edit text inputs are passed to the fragment as coordinates
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.search_btn){
            String latStr = latInput.getText().toString();
            String longStr = longInput.getText().toString();

            if(!latStr.isEmpty() && !longStr.isEmpty()){
                Double latD = Double.parseDouble(latStr);
                Double longD = Double.parseDouble(longStr);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.map_fragment_container, MainMapFragment.newInstance(ADD_STATE_SEARCH, latD, longD),null).commit();

            }


        }
    }
}
