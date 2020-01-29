package com.example.knightjeffrey_myfishspots.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_FILE = "MyFishSpotsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table 1 contract keys, used to specify which data to pull or push
    public static final String TABLE_NAME_FIRST = "locations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_ID = "userid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";

    // Table 2 contract keys, used to specify which data to pull or push
    public static final String TABLE_NAME_SECOND = "catches";
    public static final String COLUMN_CATCH_ID = "catch_id";
    public static final String COLUMN_SPOT_ID = "spot_id";
    public static final String COLUMN_SPECIES = "species";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_LURE = "lure";
    public static final String COLUMN_TIDE = "tide";
    public static final String COLUMN_METHOD = "method";
    public static final String COLUMN_IMG_PATH = "img_path";
    public static final String COLUMN_CATCH_DATE = "date";




    // using SQL syntax to create a string that will be passed to create a database
    private static final String CREATE_TABLE_ONE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME_FIRST +
            " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_ID + " TEXT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_LATITUDE + " REAL, " +
            COLUMN_LONGITUDE + " REAL, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_DATE + " DATETIME"+
            ")";

    // using SQL syntax to create a string that will be passed to create a database
    private static final String CREATE_TABLE_TWO = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME_SECOND +
            " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_ID + " TEXT, " +
            COLUMN_SPOT_ID + " INTEGER, "+
            COLUMN_SPECIES + " TEXT, " +
            COLUMN_WEIGHT + " REAL, " +
            COLUMN_LENGTH + " REAL, " +
            COLUMN_LURE + " TEXT, " +
            COLUMN_TIDE + " TEXT, " +
            COLUMN_METHOD + " TEXT, " +
            COLUMN_IMG_PATH + " TEXT, " +
            COLUMN_CATCH_DATE + " DATETIME"+
            ")";

    ////// Standard for all DataBase helper classes ////////
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_ONE);
        db.execSQL(CREATE_TABLE_TWO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Singleton style instance
    private static DataBaseHelper mInstance = null;

    public static DataBaseHelper getInstance(Context context){

        if(mInstance == null){
            mInstance = new DataBaseHelper(context);
        }
        return mInstance;
    }

    public final SQLiteDatabase mDatabase;

    private DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_FILE, null, DATABASE_VERSION);

        mDatabase = getWritableDatabase();
    }
////// Standard for all DataBase helper classes ////////

    public void insertLocation(FishSpots spot, String userID){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_ID, userID);
        cv.put(COLUMN_NAME, spot.getName());
        cv.put(COLUMN_LATITUDE, spot.getCoordinate().latitude);
        cv.put(COLUMN_LONGITUDE, spot.getCoordinate().longitude);
        cv.put(COLUMN_DESCRIPTION, spot.getDescription());
        cv.put(COLUMN_DATE, spot.getDate());
        mDatabase.insert(TABLE_NAME_FIRST,null,cv);

    }

    ///// TABLE ONE METHODS /////
    // TODO: query with userID as well as locationID
    //gets certain data based on id
    public Cursor getLocationByID(Integer _locationID, String _userID){
        /*
        String selection = COLUMN_ID + " = '?'";
        String[] selectionArgs = { _ID.toString()};

         */
        String selection = COLUMN_ID + " = '" + _locationID.toString() + "'";

        return mDatabase.query(TABLE_NAME_FIRST,
                null,selection, null,
                null, null, null);

    }

    public Cursor getAllSpots(){
        return mDatabase.query(TABLE_NAME_FIRST,
                null,null,
                null,null,
                null,null);
    }

    ///// TABLE TWO METHODS /////
    public void insertCatch(FishCaught fish, String userID){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_ID, userID);
        cv.put(COLUMN_SPOT_ID, fish.getSpotId());
        cv.put(COLUMN_SPECIES, fish.getSpecies());
        cv.put(COLUMN_WEIGHT, fish.getWeight());
        cv.put(COLUMN_LENGTH, fish.getLength());
        cv.put(COLUMN_LURE, fish.getLure());
        cv.put(COLUMN_TIDE, fish.getTide());
        cv.put(COLUMN_METHOD, fish.getMethod());
        cv.put(COLUMN_IMG_PATH, fish.getImgPath());
        cv.put(COLUMN_DATE, fish.getDate());
        mDatabase.insert(TABLE_NAME_SECOND,null,cv);

    }

    public Cursor getCatchByID(Integer _catchID, String _userID){

        String selection = COLUMN_CATCH_ID + " = '" + _catchID.toString() + "'";

        return mDatabase.query(TABLE_NAME_SECOND,
                null,selection, null,
                null, null, null);
    }

    public Cursor getAllForLocation(Integer _locationID, String _userID){

        String selection = COLUMN_SPOT_ID + " = '" + _locationID.toString() + "'";

        return mDatabase.query(TABLE_NAME_SECOND,
                null,selection, null,
                null, null, null);
    }

}
