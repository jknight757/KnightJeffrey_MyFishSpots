package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.knightjeffrey_myfishspots.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCatchFragment extends Fragment {

    public static final String TAG = "NewCatchFragment.TAG";

    NewCatchListener listener;

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


    }
}
