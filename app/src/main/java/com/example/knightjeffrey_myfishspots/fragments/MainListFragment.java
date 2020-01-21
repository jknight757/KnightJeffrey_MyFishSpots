package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.activities.AddAndViewActivity;
import com.example.knightjeffrey_myfishspots.activities.MainActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainListFragment extends ListFragment {


    public MainListFragment() {
        // Required empty public constructor
    }

    public static MainListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MainListFragment fragment = new MainListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.add_new){
            Intent addIntent = new Intent(getContext(), AddAndViewActivity.class);
            startActivityForResult(addIntent, 001);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getContext() != null){
            ListView listView = getView().findViewById(android.R.id.list);

            if(listView != null){
                ArrayList<String> mockList = new ArrayList<>();
                mockList.add("Honey Hole");
                mockList.add("Ship Wreck");
                ArrayList<String> mockList2 = new ArrayList<>();
                mockList2.add("12,0457584, -13,047343");
                mockList2.add("75,6475532, -68,756384");

                ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,mockList);
                listView.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


    }
}
