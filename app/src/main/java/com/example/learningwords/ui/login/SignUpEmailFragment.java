package com.example.learningwords.ui.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningwords.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpEmailFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private Button buttonSingUp;
    TextView tvError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_email, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        buttonSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().isEmpty()){
                    etEmail.setError("Enter an email");
                    etEmail.requestFocus();
                    return;
                }
                if (etPassword.getText().toString().isEmpty()){
                    etPassword.setError("Enter a password");
                    etPassword.requestFocus();
                    return;
                }
                createAccount(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

    }

    private void init(View view){
        mAuth = FirebaseAuth.getInstance();
        etEmail = view.findViewById(R.id.et_sign_up_email);
        etPassword = view.findViewById(R.id.et_password_email);
        buttonSingUp = view.findViewById(R.id.button_sign_up_email2);
        tvError = view.findViewById(R.id.tv_error_email);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Toast.makeText(getContext(), "User not null", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "User null", Toast.LENGTH_SHORT).show();
        }
    }

    public void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            tvError.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Sign Up successful", Toast.LENGTH_SHORT).show();

                            View view = getActivity().getCurrentFocus();
                            if (view != null){
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            NavHostFragment.findNavController(SignUpEmailFragment.this)
                                    .navigate(R.id.action_signUpEmailFragment_to_emailVerificationFragment);
                        }
                        else{
                            tvError.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Sign Up unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}