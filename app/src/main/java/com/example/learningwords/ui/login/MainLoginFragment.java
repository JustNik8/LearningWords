package com.example.learningwords.ui.login;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.learningwords.MainActivity;
import com.example.learningwords.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class MainLoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private Button buttonSignIn, buttonSignUpEmail, buttonSignInPhone, buttonSignInGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;



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

        buttonSignInPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainLoginFragment.this)
                        .navigate(R.id.action_mainLoginFragment_to_signInPhoneFragment);
            }
        });

        buttonSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    private void init(View view){
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        buttonSignUpEmail = view.findViewById(R.id.button_sign_up_email);
        buttonSignIn = view.findViewById(R.id.button_sign_in);
        buttonSignInPhone = view.findViewById(R.id.button_sign_in_phone_main);
        buttonSignInGoogle = view.findViewById(R.id.button_sign_in_google);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(getContext(), "User not null", Toast.LENGTH_SHORT).show();

            if (currentUser.getEmail().isEmpty()) {
                NavHostFragment.findNavController(MainLoginFragment.this)
                        .navigate(R.id.action_mainLoginFragment_to_mainActivity);
            } else {
                if (currentUser.isEmailVerified()) {
                    NavHostFragment.findNavController(MainLoginFragment.this)
                            .navigate(R.id.action_mainLoginFragment_to_mainActivity);
                }
                else {
                    Bundle args = new Bundle();
                    args.putString("email", currentUser.getEmail());
                    NavHostFragment.findNavController(MainLoginFragment.this)
                            .navigate(R.id.action_mainLoginFragment_to_emailVerificationFragment, args);
                }
            }
        }
        else {
            Toast.makeText(getContext(), "User null", Toast.LENGTH_SHORT).show();
        }
            /*
            if (!currentUser.getPhoneNumber().isEmpty()){
                NavHostFragment.findNavController(MainLoginFragment.this)
                        .navigate(R.id.action_mainLoginFragment_to_mainActivity);
            }

            else if (currentUser.isEmailVerified()){
                NavHostFragment.findNavController(MainLoginFragment.this)
                        .navigate(R.id.action_mainLoginFragment_to_mainActivity);
            }
            else {
                Bundle args = new Bundle();
                args.putString("email", currentUser.getEmail());
                NavHostFragment.findNavController(MainLoginFragment.this)
                        .navigate(R.id.action_mainLoginFragment_to_emailVerificationFragment, args);
            }*/

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if (task.isSuccessful()){
                                NavHostFragment.findNavController(MainLoginFragment.this)
                                        .navigate(R.id.action_mainLoginFragment_to_mainActivity);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getContext(), "Google sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
