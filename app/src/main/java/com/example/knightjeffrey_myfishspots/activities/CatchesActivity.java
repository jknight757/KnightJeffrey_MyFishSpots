package com.example.knightjeffrey_myfishspots.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.fragments.NewCatchFragment;
import com.example.knightjeffrey_myfishspots.models.DataBaseHelper;
import com.example.knightjeffrey_myfishspots.models.FishCaught;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CatchesActivity extends AppCompatActivity implements NewCatchFragment.NewCatchListener{

    NewCatchFragment newCatchFragment;
    private static final String EXTRA_ID = "EXTRA_ID";
    int locationID;

    DataBaseHelper dbh;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catches);

        Intent starterIntent = getIntent();
        if(starterIntent.hasExtra(EXTRA_ID)){
            locationID = starterIntent.getIntExtra(EXTRA_ID, -1);
            if(locationID != -1){
                newCatchFragment = NewCatchFragment.newInstance(locationID);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.catches_fragment_container,newCatchFragment,NewCatchFragment.TAG).commit();
            }
        }else{
            newCatchFragment = NewCatchFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.catches_fragment_container,newCatchFragment,NewCatchFragment.TAG).commit();
        }

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

    @Override
    public void addNewCatch(FishCaught fish) {
        dbh = DataBaseHelper.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            String userID = currentUser.getUid();

            dbh.insertCatch(fish, userID);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_ID, locationID);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
