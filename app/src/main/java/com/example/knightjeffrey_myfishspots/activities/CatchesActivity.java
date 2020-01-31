package com.example.knightjeffrey_myfishspots.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.fragments.CatchDetailFragment;
import com.example.knightjeffrey_myfishspots.fragments.NewCatchFragment;
import com.example.knightjeffrey_myfishspots.models.DataBaseHelper;
import com.example.knightjeffrey_myfishspots.models.FishCaught;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CatchesActivity extends AppCompatActivity implements NewCatchFragment.NewCatchListener{

    NewCatchFragment newCatchFragment;
    CatchDetailFragment catchDetailFragment;
    private static final String EXTRA_ID = "EXTRA_ID";
    int locationID;

    private static final String EXTRA_INTENT_CODE = "EXTRA_INTENT_CODE";
    private static final String EXTRA_CATCH_ID = "EXTRA_CATCH_ID";
    private static final int ADD_CATCH_REQUEST_CODE = 300;
    private static final int VIEW_CATCH_REQUEST_CODE = 355;
    private int intentCode;

    DataBaseHelper dbh;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore fireStoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catches);

        Intent starterIntent = getIntent();
        if(starterIntent.hasExtra(EXTRA_ID)){
            locationID = starterIntent.getIntExtra(EXTRA_ID, -1);
            intentCode = starterIntent.getIntExtra(EXTRA_INTENT_CODE, -1);
            if(locationID != -1){
                switch (intentCode){
                    case ADD_CATCH_REQUEST_CODE:
                        newCatchFragment = NewCatchFragment.newInstance(locationID);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.catches_fragment_container,newCatchFragment,NewCatchFragment.TAG).commit();
                        break;
                    case VIEW_CATCH_REQUEST_CODE:
                        int catchId = starterIntent.getIntExtra(EXTRA_CATCH_ID, -1);
                        catchDetailFragment = CatchDetailFragment.newInstance(locationID, catchId);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.catches_fragment_container,catchDetailFragment,CatchDetailFragment.TAG).commit();
                        break;
                }

            }
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
        String userID = "12234";

        // if there is internet add new catch to firestore
        if(checkInternet()){

            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();

            if(currentUser != null){
                userID = currentUser.getUid();
                dbh.insertCatch(fish, userID);

                Cursor c = dbh.getAllForLocation(locationID,userID);
                c.moveToLast();
                int id = c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_ID));
                fish.setCatchId(id);

                String locationIdStr = locationID + "";
                String catchIdStr = fish.getCatchId() + "";
                fireStoreDB = FirebaseFirestore.getInstance();
                DocumentReference firebaseLocations = fireStoreDB.collection("locations").document(userID);
                DocumentReference mylocations =  firebaseLocations.collection("myLocations").document(locationIdStr);
                DocumentReference myCatches = mylocations.collection("catches").document(catchIdStr);

                myCatches.set(fish).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CatchesActivity.this,"Fish Added",Toast.LENGTH_SHORT).show();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CatchesActivity.this,"Fish Added",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }else{
            dbh.insertCatch(fish, userID);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_ID, locationID);
        setResult(RESULT_OK, resultIntent);
        finish();


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
