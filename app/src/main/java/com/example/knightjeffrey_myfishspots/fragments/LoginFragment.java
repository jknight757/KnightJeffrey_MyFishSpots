package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knightjeffrey_myfishspots.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    
    public static final String TAG = "LoginFragment.TAG";
    private String email;
    private String password;
    EditText emailInput;
    EditText passwordInput;
    
    LoginListener listener;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        
        Bundle args = new Bundle();
        
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public interface LoginListener{
        void LoginGranted();
        void RegisterClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof LoginListener){
            listener = (LoginListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if(getView()!= null) {
            
            // access UI components
            emailInput = getView().findViewById(R.id.username_input);
            passwordInput = getView().findViewById(R.id.password_input);
            (getView().findViewById(R.id.btn_login)).setOnClickListener(this);
            (getView().findViewById(R.id.btn_signup)).setOnClickListener(this);
            email = emailInput.getText().toString();
            password = passwordInput.getText().toString();
        }
        
        
    }

   
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if(validateInput()){
                    listener.LoginGranted();
                }
                break;
            case R.id.btn_signup:
                listener.RegisterClicked();
                break;
        }
    }
    
    // TODO: validate login through firebase
    public boolean validateInput(){
        boolean isValid = false;
        String input1 = emailInput.getText().toString();
        String input2 = passwordInput.getText().toString();

        if(!input1.isEmpty() && !input2.isEmpty()){
            isValid = true;
            
        }else{
            if(input1.isEmpty()){
                Toast.makeText(getContext(),"Enter Email", Toast.LENGTH_SHORT).show(); 
            }else{
                Toast.makeText(getContext(),"Enter Password", Toast.LENGTH_SHORT).show();
            }
            
        }
        
        return isValid;
    }
}
