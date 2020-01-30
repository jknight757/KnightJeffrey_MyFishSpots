package com.example.knightjeffrey_myfishspots.fragments;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.models.DataBaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class CatchDetailFragment extends Fragment {

    public static final String TAG = "CatchDetailFragment.TAG";
    private static final String ID_KEY = "ID_KEY";
    private static final String EXTRA_CATCH_ID = "EXTRA_CATCH_ID";
    private int locationID;
    private int catchId;
    private Cursor cursor;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    ImageView catchImage;
    TextView dateTV;
    TextView speciesTV;
    TextView sizeTV;
    TextView lureTV;
    TextView tideTV;
    TextView methodTV;

    private String species;
    private String length;
    private String weight;
    private String lure;
    private String tide;
    private String method;
    private String imgPath;
    private String date;

    public CatchDetailFragment() {
        // Required empty public constructor
    }

    public static CatchDetailFragment newInstance(int spotId, int catchId) {

        Bundle args = new Bundle();
        args.putInt(ID_KEY,spotId);
        args.putInt(EXTRA_CATCH_ID,catchId);
        CatchDetailFragment fragment = new CatchDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface CatchDetailListener{
        void deleteCatch(int id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catch_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null && getView() != null){
            locationID = getArguments().getInt(ID_KEY);
            catchId = getArguments().getInt(EXTRA_CATCH_ID);
            catchImage =  getView().findViewById(R.id.catch_img_dtl);
            dateTV = getView().findViewById(R.id.date_lbl);
            speciesTV = getView().findViewById(R.id.species_lbl);
            sizeTV = getView().findViewById(R.id.size_lbl);
            lureTV = getView().findViewById(R.id.lure_lbl);
            tideTV = getView().findViewById(R.id.tide_lbl);
            methodTV = getView().findViewById(R.id.method_lbl);
            queryDatabase(locationID,catchId);

        }
    }
    public void queryDatabase(int _spotId, int _catchId){
        DataBaseHelper dbh = DataBaseHelper.getInstance(getContext());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String Uid = currentUser.getUid();

        cursor = dbh.getAllForLocation(_spotId, Uid);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            cursor.move(_catchId);
            species = "Species: ";
            species += cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_SPECIES));

            length = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LENGTH));
            length += " Inches";

            weight = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_WEIGHT));
            weight += " LBS";

            lure = "Lure: ";
            lure += cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_LURE));

            tide = "Tide: ";
            tide += cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_TIDE));

            method = "Method: ";
            method += cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_METHOD));

            imgPath = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_IMG_PATH));
            date = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_CATCH_DATE));


            updateUI();

        }
    }

    public void updateUI(){

        catchImage.setImageURI(Uri.parse(imgPath));
        dateTV.setText(date);
        speciesTV.setText(species);
        String size = length + ", "+ weight;
        sizeTV.setText(size);
        lureTV.setText(lure);
        tideTV.setText(tide);
        methodTV.setText(method);
    }
}
