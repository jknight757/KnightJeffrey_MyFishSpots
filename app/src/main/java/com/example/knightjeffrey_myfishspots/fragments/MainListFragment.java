package com.example.knightjeffrey_myfishspots.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.knightjeffrey_myfishspots.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainListFragment extends ListFragment {


    public MainListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_list, container, false);
    }

}
