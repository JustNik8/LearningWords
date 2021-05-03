package com.example.learningwords.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningwords.MainActivity;
import com.example.learningwords.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainLoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private Button buttonSignIn, buttonSignUpEmail, buttonSignUpPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_main_login, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        buttonSignUpEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainLoginFragment.this)
                        .navigate(R.id.action_mainLoginFragment_to_signUpEmailFragment);
            }
        });

        buttonSignUpPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainLoginFragment.this)
                        .navigate(R.id.action_mainLoginFragment_to_phoneVerificationFragment);
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().isEmpty()){
                    etEmail.setError("Enter an email");
                    etEmail.requestFocus();
                }
                if (etPassword.getText().toString().isEmpty()){
                    etPassword.setError("Enter a password");
                    etPassword.requestFocus();
                }
                signIn(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

    }

    private void init(View view){
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        buttonSignUpEmail = view.findViewById(R.id.button_sign_up_email);
        buttonSignIn = view.findViewById(R.id.button_sign_in);
        mAuth = FirebaseAuth.getInstance();
        buttonSignUpPhone = view.findViewById(R.id.button_sign_up_phone);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Toast.makeText(getContext(), "User not null", Toast.LENGTH_SHORT).show();
            if (currentUser.isEmailVerified()){
                NavHostFragment.findNavController(MainLoginFragment.this)
                        .navigate(R.id.action_mainLoginFragment_to_mainActivity);
            }
            else {
                NavHostFragment.findNavController(MainLoginFragment.this)
                        .navigate(R.id.action_mainLoginFragment_to_signUpEmailFragment);
            }
        }
        else {
            Toast.makeText(getContext(), "User null", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Sign in successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            if (currentUser != null){
                                Bundle args = new Bundle();
                                args.putString("email", currentUser.getEmail());
                                NavHostFragment.findNavController(MainLoginFragment.this)
                                        .navigate(R.id.action_mainLoginFragment_to_emailVerificationFragment, args);
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "Sign in unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
