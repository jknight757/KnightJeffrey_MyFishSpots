package com.example.knightjeffrey_myfishspots.models;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.knightjeffrey_myfishspots.R;


public class CatchesTableCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;

    public CatchesTableCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
        cursorInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.catch_list_item,parent,false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView speciesTV = view.findViewById(R.id.species_text);
        TextView lengthTV = view.findViewById(R.id.length_text);
        TextView weightTV = view.findViewById(R.id.weight_text);
        TextView lureTV = view.findViewById(R.id.lure_text);
        TextView tideTV = view.findViewById(R.id.tide_text);
        TextView methodTV =  view.findViewById(R.id.method_text);

        ImageView imageView = view.findViewById(R.id.catch_img);

        String species = "Species: ";
        species += cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_SPECIES));

        String lengthStr = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LENGTH));
        lengthStr += " Inches";

        String weightStr = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_WEIGHT));
        weightStr += " LBS";

        String lure = "Lure: ";
        lure += cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LURE));

        String tide = "Tide: ";
        tide += cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_TIDE));

        String method = "Method: ";
        method += cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_METHOD));

        String imgPath = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_IMG_PATH));
        String date = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_CATCH_DATE));

        if(species.equals("Species: Null")){ speciesTV.setVisibility(View.GONE);
        }else{ speciesTV.setText(species); }

        if(lengthStr.equals("0 Inches")){ lengthTV.setVisibility(View.GONE);
        }else { lengthTV.setText(lengthStr); }

        if(weightStr.equals("0 LBS")) { weightTV.setVisibility(View.GONE);
        }else { weightTV.setText(weightStr); }

        if(lure.equals("Lure: Null")){ lureTV.setVisibility(View.GONE);
        }else{ lureTV.setText(lure); }

        if(tide.equals("Tide: Null")){ tideTV.setVisibility(View.GONE);
        }else { tideTV.setText(tide); }

        if(method.equals("Method: Null")){ methodTV.setVisibility(View.GONE);
        }else{ methodTV.setText(method); }

        if(imgPath.equals("Null")){
            imageView.setImageResource(R.drawable.ic_image_black_24dp);
        }else {
            Uri uri = Uri.parse(imgPath);
            Log.i("Image", "bindView: " + imgPath);
            imageView.setImageURI(uri);
        }

    }
}
