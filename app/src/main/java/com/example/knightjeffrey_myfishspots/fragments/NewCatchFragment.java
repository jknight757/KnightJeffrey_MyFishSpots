package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.knightjeffrey_myfishspots.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCatchFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "NewCatchFragment.TAG";

    NewCatchListener listener;

    EditText speciesInput;
    EditText lengthInput;
    EditText weightInput;
    EditText lureInput;

    Spinner tideSpinner;
    Spinner methodSpinner;

    ArrayList<String> tides;
    ArrayList<String> methods;

    public NewCatchFragment() {
        // Required empty public constructor
    }

    public static NewCatchFragment newInstance() {

        Bundle args = new Bundle();

        NewCatchFragment fragment = new NewCatchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface NewCatchListener{
        void addNewCatch();
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
        if(getView() != null){

            speciesInput = getView().findViewById(R.id.species_input);
            lengthInput = getView().findViewById(R.id.length_input);
            weightInput = getView().findViewById(R.id.weight_input);
            lureInput = getView().findViewById(R.id.lure_input);

            tideSpinner = getView().findViewById(R.id.tide_spinner);
            methodSpinner = getView().findViewById(R.id.method_spinner);
            setUpSpinners();
            getView().findViewById(R.id.add_catch_btn).setOnClickListener(this);

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

    }
}
