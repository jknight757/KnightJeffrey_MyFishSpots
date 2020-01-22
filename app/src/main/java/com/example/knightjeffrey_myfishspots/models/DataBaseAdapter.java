package com.example.knightjeffrey_myfishspots.models;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class DataBaseAdapter extends ResourceCursorAdapter {

    public DataBaseAdapter(Context context, Cursor c){
        super(context,android.R.layout.simple_list_item_2,c,0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTV = view.findViewById(android.R.id.text1);
        TextView coordinateTV = view.findViewById(android.R.id.text2);

        int nameIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_NAME);
        int latitudeIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_LATITUDE);
        int longitudeIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_LONGITUDE);

        String name = cursor.getString(nameIndex);
        String latitude = cursor.getString(latitudeIndex);
        String longitude = cursor.getString(longitudeIndex);

        nameTV.setText(name);

        String coordinates = latitude + ", " + longitude;
        coordinateTV.setText(coordinates);
    }
}
