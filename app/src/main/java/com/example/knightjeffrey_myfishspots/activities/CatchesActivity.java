package com.example.knightjeffrey_myfishspots.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.fragments.NewCatchFragment;

public class CatchesActivity extends AppCompatActivity {

    NewCatchFragment newCatchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catches);

        newCatchFragment = NewCatchFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.catches_fragment_container,newCatchFragment,NewCatchFragment.TAG).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_go_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.go_home){
            Intent homeIntent = new Intent(this,MainActivity.class);
            startActivity(homeIntent);

        }

        return super.onOptionsItemSelected(item);
    }
}
