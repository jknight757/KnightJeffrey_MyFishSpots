package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.models.FishCaught;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.transform.Result;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCatchFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "NewCatchFragment.TAG";
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final int REQUEST_IMAGE = 0x0010;

    NewCatchListener listener;
    ImageButton imageButton;

    EditText speciesInput;
    EditText lengthInput;
    EditText weightInput;
    EditText lureInput;

    Spinner tideSpinner;
    Spinner methodSpinner;

    String species;
    Double length;
    Double weight;
    String lure;
    String tide;
    String method;
    String imgPath;

    int tideIndex;
    int methodIndex;
    int locationID;

    ArrayList<String> tides;
    ArrayList<String> methods;
    boolean imageReceived = false;
    private static final String FOLDER_NAME = "catchImages";
    private static File folderPath;
    private static boolean fileCreated;

    public NewCatchFragment() {
        // Required empty public constructor
    }

    public static NewCatchFragment newInstance() {

        Bundle args = new Bundle();

        NewCatchFragment fragment = new NewCatchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static NewCatchFragment newInstance(int _id) {

        Bundle args = new Bundle();
        args.putInt(EXTRA_ID,_id);
        NewCatchFragment fragment = new NewCatchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface NewCatchListener{
        void addNewCatch(FishCaught fish);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof NewCatchListener){
            listener = (NewCatchListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_catch, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() != null && getArguments() != null){

            locationID = getArguments().getInt(EXTRA_ID);

            speciesInput = getView().findViewById(R.id.species_input);
            lengthInput = getView().findViewById(R.id.length_input);
            weightInput = getView().findViewById(R.id.weight_input);
            lureInput = getView().findViewById(R.id.lure_input);

            tideSpinner = getView().findViewById(R.id.tide_spinner);
            methodSpinner = getView().findViewById(R.id.method_spinner);
            setUpSpinners();
            getView().findViewById(R.id.add_catch_btn).setOnClickListener(this);
            imageButton = getView().findViewById(R.id.retrieve_img_btn);
            imageButton.setOnClickListener(this);

        }

    }

    public void setUpSpinners(){
        tides = new ArrayList<>();
        methods = new ArrayList<>();

        tides.add("Tide");
        tides.add("High Incomming");
        tides.add("High Slack");
        tides.add("High Outgoing");
        tides.add("Low Outgoing");
        tides.add("Low Slack");
        tides.add("Low Incomming");

        methods.add("Method");
        methods.add("Casting");
        methods.add("Drifting");
        methods.add("Trolling");
        methods.add("Bottom");
        methods.add("Other");

        if(tideSpinner != null){
            ArrayAdapter<String> adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, tides);
            tideSpinner.setAdapter(adapter);
        }
        if(methodSpinner != null){
            ArrayAdapter<String> adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, methods);
            methodSpinner.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.add_catch_btn:
                if(validateInput()){

                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateStr = dateFormat.format(date);
                    listener.addNewCatch( new FishCaught(locationID, species, weight, length, lure, tide, method, imgPath,dateStr));

                }else{
                    Toast.makeText(getContext(),"An image or species is needed to add catch",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.retrieve_img_btn:

                Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,REQUEST_IMAGE);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == REQUEST_IMAGE){
            imageButton.setImageURI(data.getData());
            imageReceived = true;
            imgPath = data.getData().toString();
            byte[] byteArray = null;
            try{
                InputStream stream = getContext().getContentResolver().openInputStream(Uri.parse(imgPath));
                byteArray= getBytes(stream);
            }catch (IOException e){
                Log.i(TAG, "onActivityResult: Image Not Converted to Byte[]");
                e.printStackTrace();
            }

            if(byteArray != null){
                try{

                    folderPath = getContext().getExternalFilesDir(FOLDER_NAME +"/"+ System.currentTimeMillis());


//                    FileOutputStream fos = getContext().openFileOutput(FOLDER_NAME,getContext().MODE_PRIVATE);
                    FileOutputStream fos = new FileOutputStream(FOLDER_NAME);
                    fos.write(byteArray);
                    Log.i(TAG, "onActivityResult: img path "+  folderPath.getAbsolutePath());
                    imgPath = folderPath.getAbsolutePath();
                    fos.close();

                }catch (IOException e){
                    Log.i(TAG, "onActivityResult: Image Not Saved");
                    e.printStackTrace();
                }
            }


        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public boolean validateInput(){
        boolean isValid = true;

        species = speciesInput.getText().toString();
        String lengthStr = lengthInput.getText().toString();
        String weightStr = weightInput.getText().toString();
        lure = lureInput.getText().toString();

        tideIndex = tideSpinner.getSelectedItemPosition();
        methodIndex = methodSpinner.getSelectedItemPosition();

        if(!imageReceived && species.isEmpty()){
            isValid = false;
        }
        if(species.isEmpty()){ species = "Null";}
        if(!lengthStr.isEmpty()){ length = Double.parseDouble(lengthStr); }
        else{ length = 0.0;}
        if(!weightStr.isEmpty()){ weight = Double.parseDouble(weightStr); }
        else{ weight = 0.0;}
        if(lure.isEmpty()){ lure = "Null";}
        if(tideIndex != 0){ tide = tides.get(tideIndex);}
        else{ tide = "Null";}
        if(methodIndex != 0){ method = methods.get(methodIndex);}
        else{ method = "Null";}
        if(!imageReceived){imgPath = "Null";}
        return isValid;
    }


}
