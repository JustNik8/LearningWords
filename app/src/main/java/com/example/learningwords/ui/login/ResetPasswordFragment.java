package com.example.learningwords.ui.login;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.learningwords.Constants;
import com.example.learningwords.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {

    private EditText etEmail;
    private Button buttonReset, buttonBack;
    private TextView tvInfo;
    String email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            email = getArguments().getString(Constants.EMAIL_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_reset_password, container, false);
        init(root);
        etEmail.setText(email);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String message;
                                if (task.isSuccessful()){
                                    message = getString(R.string.info_message_sent) + " " + email;
                                    tvInfo.setTextColor(Color.GREEN);
                                }
                                else{
                                    message = getString(R.string.info_message_error);
                                    tvInfo.setTextColor(Color.RED);
                                }
                                tvInfo.setText(message);
                                tvInfo.setVisibility(View.VISIBLE);
                            }
                        });
            }
        });

        buttonBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(ResetPasswordFragment.this)
                    .navigate(R.id.action_resetPasswordFragment_to_mainLoginFragment);
        });

        return root;
    }

    private void init(View view){
        etEmail = view.findViewById(R.id.et_reset_email);
        buttonReset = view.findViewById(R.id.button_reset_pass);
        tvInfo = view.findViewById(R.id.tv_info);
        buttonBack = view.findViewById(R.id.button_reset_back);
    }


}