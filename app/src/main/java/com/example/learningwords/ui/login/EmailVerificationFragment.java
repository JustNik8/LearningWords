package com.example.learningwords.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningwords.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationFragment extends Fragment {

    private String email;
    private TextView tvEmail;
    Button buttonSignOut, buttonVerify;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        email = getArguments().getString("email");
        if (email == null){
            email = "Error";
        }
        return inflater.inflate(R.layout.fragment_email_verification, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()){
            NavHostFragment.findNavController(EmailVerificationFragment.this)
                    .navigate(R.id.action_emailVerificationFragment_to_mainActivity);
        }
        else {
            init(view);
            tvEmail.setText(email);
            user.sendEmailVerification();

            buttonVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.reload().addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (user.isEmailVerified()){
                                Toast.makeText(getContext(), "Verification successful", Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(EmailVerificationFragment.this)
                                        .navigate(R.id.action_emailVerificationFragment_to_mainActivity);
                            }
                            else{
                                Toast.makeText(getContext(), "You haven't verified", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            });

            buttonSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    NavHostFragment.findNavController(EmailVerificationFragment.this)
                            .navigate(R.id.action_emailVerificationFragment_to_mainLoginFragment);
                }
            });
        }


    }

    private void init(View view){
        tvEmail = view.findViewById(R.id.email_in_verification);
        buttonVerify = view.findViewById(R.id.button_verify_email);
        buttonSignOut = view.findViewById(R.id.button_sign_out_email);
    }

}