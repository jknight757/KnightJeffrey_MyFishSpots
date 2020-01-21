package com.example.knightjeffrey_myfishspots.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
public class SignUpFragment extends Fragment  implements View.OnClickListener {

    public static final String TAG = "SignUpFragment.TAG";

    private String email;
    private String email2;
    private String password;

    EditText emailInput;
    EditText emailInput2;
    EditText passwordInput;

    SignUpListener listener;
    private FirebaseAuth mAuth;

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
            mAuth = FirebaseAuth.getInstance();
            emailInput = getView().findViewById(R.id.username_input);
            emailInput2 = getView().findViewById(R.id.username_input2);
            passwordInput = getView().findViewById(R.id.password_input);
            (getView().findViewById(R.id.btn_signup)).setOnClickListener(this);

        }
    }

    // valitdate user input and authenticate sign up through fire base
    @Override
    public void onClick(View v) {

        // check that input fields are valid
        if(validateInput()){
            Toast.makeText(getContext(),"Input valid", Toast.LENGTH_SHORT).show();
            // check that the user isn't already logged in and the activity isn't null
            FirebaseUser user = mAuth.getCurrentUser();
            if(user == null && getActivity() != null){

                // attempt to create new user passing the validated input
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // handle sign up request result
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(),"onComplete: SignUp Successful", Toast.LENGTH_SHORT).show();

                                    listener.SignUpGranted();
                                }else{
                                    if(task.getResult() != null){
                                        Toast.makeText(getContext(),"SignUp Failed: " + task.getResult().toString(), Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }
                        });
            }


        }

    }



    public boolean validateInput(){
        Toast.makeText(getContext(),"Validating input...", Toast.LENGTH_SHORT).show();

        boolean isValid = false;
        String input1 = emailInput.getText().toString();
        String input2 = emailInput2.getText().toString();
        password = passwordInput.getText().toString();
        if(!input1.isEmpty() && !input2.isEmpty()){
            if(input1.equals(input2)){
                if(!password.isEmpty() && password.length() > 5){
                    isValid = true;
                    email = emailInput.getText().toString();

                }else{
                    Toast.makeText(getContext(),"Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
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
