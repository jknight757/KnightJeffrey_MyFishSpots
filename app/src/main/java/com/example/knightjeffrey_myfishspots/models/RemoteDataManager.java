package com.example.knightjeffrey_myfishspots.models;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemoteDataManager {

    Context mContext;
    Cursor localLocations;
    ArrayList<FishSpots> localSpots;
    ArrayList<FishCaught> localCatches;
    ArrayList<Integer> numCatches;
    ArrayList<DocumentSnapshot> remoteLocations;
    ArrayList<DocumentSnapshot> remoteCatches;
    ArrayList<FishCaught> fishCaughtR;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore fireStoreDB;

    DocumentSnapshot userDS;

    public RemoteDataManager(Context mContext) {
        this.mContext = mContext;
    }

    public void getRemoteData(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String Uid = currentUser.getUid();
        remoteLocations = new ArrayList<>();
        remoteCatches = new ArrayList<>();

        fireStoreDB = FirebaseFirestore.getInstance();
//        DocumentReference firebaseLocations = fireStoreDB.collection("locations").document(Uid);
////        DocumentReference mylocations =  firebaseLocations.collection("myLocations").document(locationIdStr);
////        DocumentReference myCatches = mylocations.collection("catches").document(catchIdStr);

        final CollectionReference myLocations = fireStoreDB.collection("locations").document(Uid).collection("myLocations");
        myLocations.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    remoteLocations = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
                    Toast.makeText(mContext, "onSuccess: first"+ remoteLocations.size(), Toast.LENGTH_SHORT).show();

                    for (DocumentSnapshot d: remoteLocations) {
                       // remoteLocations.add(d);
                        Log.i("RemoteDataManager", "onSuccess: "+d.get("name"));
                        Toast.makeText(mContext, "onSuccess: "+d.get("name"), Toast.LENGTH_SHORT).show();





                        final CollectionReference myCatches = myLocations.document(d.get("locationId").toString()).collection("catches");
                        myCatches.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(!queryDocumentSnapshots.isEmpty()){
                                    remoteCatches = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
                                    if(!remoteCatches.isEmpty()) {
                                        fishCaughtR = new ArrayList<>();
                                        for (DocumentSnapshot d : remoteCatches) {
                                            FishCaught fish;
                                            Log.i("RemoteDataManager", "onSuccess: " + d.get("species"));
                                            int spotId = Integer.parseInt(d.getId());
                                            fish = new FishCaught(spotId, d.getString("species"),
                                                    d.getDouble("weight"), d.getDouble("length"),
                                                    d.getString("lure"), d.getString("tide"),
                                                    d.getString("method"), d.getString("imgPath"),
                                                    d.getString("date"));
                                            fishCaughtR.add(fish);
                                        }
                                        compareData();
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext, "onFailure: nothing found", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }
        });





    }


    public void getLocalData(){
        DataBaseHelper dbh = DataBaseHelper.getInstance(mContext);

        localLocations = dbh.getAllSpots();

        if(localLocations.getCount() > 0){
            localCatches = new ArrayList<>();
            numCatches = new ArrayList<>();
            localSpots = new ArrayList<>();
            localLocations.moveToFirst();

            for(int i = 0; i < localLocations.getCount(); i++){
                localLocations.move(i);

                String name = localLocations.getString(localLocations.getColumnIndex(DataBaseHelper.COLUMN_NAME));
                String latitude = localLocations.getString(localLocations.getColumnIndex(DataBaseHelper.COLUMN_LATITUDE));
                String longitude = localLocations.getString(localLocations.getColumnIndex(DataBaseHelper.COLUMN_LONGITUDE));
                String description = localLocations.getString(localLocations.getColumnIndex(DataBaseHelper.COLUMN_DESCRIPTION));
                String date = localLocations.getString(localLocations.getColumnIndex(DataBaseHelper.COLUMN_DATE));
                LatLng coordinate = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                FishSpots spot = new FishSpots(coordinate,name,description,date);
                localSpots.add(spot);

                int idIndex = localLocations.getColumnIndex(DataBaseHelper.COLUMN_ID);
                int _id = localLocations.getInt(idIndex);

                Cursor c =  dbh.getAllForLocation(_id, "1234");
                c.moveToFirst();
                for (int x = 0; i< c.getCount(); i++){
                    c.move(x);
                    String species= c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_SPECIES));
                    String lengthStr = c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_LENGTH));
                    String weightStr = c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_WEIGHT));
                    String lure= c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_LURE));
                    String tide = c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_TIDE));
                    String method = c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_METHOD));
                    String imgPath = c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_IMG_PATH));
                    String date1 = c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_CATCH_DATE));
                    FishCaught fish = new FishCaught(_id,species,Double.parseDouble(weightStr),
                            Double.parseDouble(lengthStr),lure,tide,method,imgPath,date1);

                    localCatches.add(fish);
                }

            }

        }

    }

    public void compareData(){

        getLocalData();
        if(localSpots != null) {

            if (localSpots.size() != remoteLocations.size()) {
                if (localSpots.size() < remoteLocations.size()) {
                    Toast.makeText(mContext, "Local needs update", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Remote needs update", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "Match", Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "local count: " + localSpots.size() + " remote: " + remoteLocations.size(), Toast.LENGTH_SHORT).show();

            }
        }else {
            if(remoteLocations.size()> 0){
//                Toast.makeText(mContext, "Local needs update", Toast.LENGTH_SHORT).show();
                localSpots = new ArrayList<>();
                for (DocumentSnapshot d: remoteLocations) {
                    Toast.makeText(mContext, "Location: "+ d.getId(), Toast.LENGTH_SHORT).show();
                    String name = d.getString("name");
                    String description = d.getString("description");
                    Map<String,Object> coodinateMap = ( Map<String,Object>)d.getData().get("coordinate");
                    Double latitude =  (Double) coodinateMap.get("latitude");
                    Double longitude =  (Double) coodinateMap.get("longitude");
                    Toast.makeText(mContext, "lat: "+ latitude + ", long: " + longitude, Toast.LENGTH_SHORT).show();
                    LatLng latLng =  new LatLng(latitude,longitude);
                    String date = d.getString("date");
                    int locId = Integer.parseInt(d.getId());

                    FishSpots spot = new FishSpots(latLng,name,description,date);
                    localSpots.add(spot);

                }

            updateLocal();
            }
        }
//        if(localCatches.size() == remoteCatches.size())
//        {
//            Toast.makeText(mContext, "Match", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(mContext, "local count: "+ localCatches.size() + " remote: "+ remoteCatches.size(), Toast.LENGTH_SHORT).show();
//        }
    }

    public void updateLocal(){
        // loop through local spots and add to SQLite database
    }

}
