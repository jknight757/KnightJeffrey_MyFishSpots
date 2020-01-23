package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import com.example.knightjeffrey_myfishspots.R;
import com.example.knightjeffrey_myfishspots.activities.AddAndViewActivity;
import com.example.knightjeffrey_myfishspots.activities.MainActivity;
import com.example.knightjeffrey_myfishspots.models.DataBaseAdapter;
import com.example.knightjeffrey_myfishspots.models.DataBaseHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainListFragment extends ListFragment {

    public static final String TAG = "MainListFragment.TAG";

    MenuClickListener listener;
    Cursor cursor;

    public MainListFragment() {
        // Required empty public constructor
    }

    public static MainListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MainListFragment fragment = new MainListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public interface MenuClickListener{
        void addClicked();
        void listItemClicked(int _id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MenuClickListener){
            listener = (MenuClickListener) context;
        }
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

    // menu bar items for homescreen are handled here
    // this is because other fragments that share the same activity as this fragment might not be a
    // screen that should have the menu bar items
    // this fragment is only used on the homescreen so handling menu bar interactions here is valid
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.add_new){
            listener.addClicked();
        }
        return super.onOptionsItemSelected(item);
    }


    // create list view, retrieve all items from database
    //  pass cursor to adapter and set adapter to list view
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getContext() != null){
            ListView listView = getView().findViewById(android.R.id.list);

            DataBaseHelper dbh = DataBaseHelper.getInstance(getContext());
            cursor = dbh.getAll();

            if(cursor.getCount() > 0){
                DataBaseAdapter adapter = new DataBaseAdapter(getContext(), cursor);
                listView.setAdapter(adapter);
            }else{
                Toast.makeText(getContext(),"No Stored Spots", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // get database row at the position of the item clicked in the list
    // get id from selected row and pass id back to activity
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

          DataBaseHelper dbh = DataBaseHelper.getInstance(getContext());
          cursor = dbh.getAll();

          if(cursor.getPosition() < 0){
             cursor.moveToFirst();
             cursor.move(position);
          }

          int idIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_ID);
          int _id = cursor.getInt(idIndex);
          listener.listItemClicked(_id);

    }
}
