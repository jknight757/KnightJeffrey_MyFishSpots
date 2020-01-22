package com.example.knightjeffrey_myfishspots.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_FILE = "database.db";
    private static final int DATABASE_VERSION = 1;

    // contract keys, used to specify which data to pull or push
    public static final String TABLE_NAME = "locations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_ID = "userid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";



    // using SQL syntax to create a string that will be passed to create a database
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME +
            " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_ID + " TEXT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_LATITUDE + " REAL, " +
            COLUMN_LONGITUDE + " REAL, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_DATE + " DATETIME"+
            ")";

    ////// Standard for all DataBase helper classes ////////
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
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
        mDatabase.insert(TABLE_NAME,null,cv);

    }

    //gets certain data based on id
    public Cursor getLocationByID(Integer _locationID, Integer _userID){
        /*
        String selection = COLUMN_ID + " = '?'";
        String[] selectionArgs = { _ID.toString()};

         */
        String selection = COLUMN_ID + " = '" + _locationID.toString() + "'";

        return mDatabase.query(TABLE_NAME,
                null,selection, null,
                null, null, null);

    }

    public Cursor getAll(){
        return mDatabase.query(TABLE_NAME,
                null,null,
                null,null,
                null,null);
    }

}
