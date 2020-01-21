package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private FirebaseAuth mAuth;


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
            mAuth = FirebaseAuth.getInstance();
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

                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if(currentUser == null && getActivity() != null){
                        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(),"Login Successful",Toast.LENGTH_SHORT).show();

                                    listener.LoginGranted();
                                }else{
                                    Toast.makeText(getContext(),"Invalid Email or Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
                break;
            case R.id.btn_signup:
                listener.RegisterClicked();
                break;
        }
    }
    
    // TODO: validate login through firebase
    public boolean validateInput(){
        Toast.makeText(getContext(),"Validating Login...",Toast.LENGTH_SHORT).show();
        boolean isValid = false;
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){
            isValid = true;
            Toast.makeText(getContext(),"Login valid",Toast.LENGTH_SHORT).show();


        }else{
            if(email.isEmpty()){
                Toast.makeText(getContext(),"Enter Email", Toast.LENGTH_SHORT).show(); 
            }else{
                Toast.makeText(getContext(),"Enter Password", Toast.LENGTH_SHORT).show();
            }
            
        }
        
        return isValid;
    }
}
