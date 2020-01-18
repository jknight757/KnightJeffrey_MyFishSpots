package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.knightjeffrey_myfishspots.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment  implements View.OnClickListener {

    public static final String TAG = "SignUpFragment.TAG";

    private String email;
    private String email2;
    private String password;

    EditText emailInput;
    EditText emailInput2;
    EditText passwordInput;

    SignUpListener listener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {

        Bundle args = new Bundle();

        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public interface SignUpListener{
        void SignUpGranted();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SignUpListener){
            listener = (SignUpListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null){
            emailInput = getView().findViewById(R.id.username_input);
            emailInput2 = getView().findViewById(R.id.username_input2);
            passwordInput = getView().findViewById(R.id.password_input);
            (getView().findViewById(R.id.btn_signup)).setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        if(validateInput()){
            listener.SignUpGranted();
        }

    }

    // TODO: validate SignUp through firebase
    public boolean validateInput(){
        boolean isValid = false;
        String input1 = emailInput.getText().toString();
        String input2 = emailInput2.getText().toString();

        if(!input1.isEmpty() && !input2.isEmpty()){
            if(input1.equals(input2)){
                if(!passwordInput.getText().toString().isEmpty()){
                    isValid = true;
                }else{
                    Toast.makeText(getContext(),"Enter Password", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(getContext(),"Emails Do Not Match", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getContext(),"Enter Email", Toast.LENGTH_SHORT).show();
        }


        return isValid;
    }
}
