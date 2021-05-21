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
import android.widget.Toast;

import com.example.learningwords.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignInPhoneFragment extends Fragment {

    private EditText etPhoneNumber, etCodeField;
    private Button buttonSignIn, buttonVerify, buttonResend, buttonGoBack;
    private TextView tvInfo;

    private FirebaseAuth mAuth;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        if (savedInstanceState != null){
            onViewStateRestored(savedInstanceState);
        }

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhoneNumber.getText().toString().isEmpty()){
                    showInfo(getString(R.string.invalid_phone_number), Color.RED);
                    return;
                }
                etPhoneNumber.setEnabled(false);
                startPhoneNumberVerification(etPhoneNumber.getText().toString());
                buttonSignIn.setEnabled(false);
                hideInfo();

            }
        });

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etCodeField.getText().toString();
                if (code.isEmpty()){
                    etCodeField.setError(getString(R.string.invalid_code));
                    etCodeField.requestFocus();
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, code);
            }
        });

        buttonResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(etPhoneNumber.getText().toString(), mResendToken);
            }
        });

        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SignInPhoneFragment.this)
                        .navigate(R.id.action_signUpPhoneFragment_to_mainLoginFragment);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                mVerificationInProgress = false;
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException){
                    showInfo(getString(R.string.invalid_phone_number), Color.RED);
                }
                else if (e instanceof FirebaseTooManyRequestsException){
                    Snackbar.make(view, getString(R.string.too_many_requests), Snackbar.LENGTH_SHORT).show();
                }
                etPhoneNumber.setEnabled(true);
                buttonSignIn.setEnabled(true);

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                showInfo(getString(R.string.code_sent_to) + " " + etPhoneNumber.getText().toString(), Color.GREEN);
                etCodeField.setVisibility(View.VISIBLE);
                buttonVerify.setVisibility(View.VISIBLE);
                buttonResend.setVisibility(View.VISIBLE);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
        }
    }

    private void init(View view){
        etPhoneNumber = view.findViewById(R.id.et_sign_up_phone);
        buttonSignIn = view.findViewById(R.id.button_sign_in_phone);
        tvInfo = view.findViewById(R.id.tv_info_phone);
        buttonVerify = view.findViewById(R.id.button_verify_phone);
        buttonResend = view.findViewById(R.id.button_resend_code);
        etCodeField = view.findViewById(R.id.et_code);
        buttonGoBack = view.findViewById(R.id.button_go_back);
        mAuth = FirebaseAuth.getInstance();
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(mCallbacks)
                .setForceResendingToken(token)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        showInfo(getString(R.string.resending_text), Color.GREEN);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), getString(R.string.sign_in_successful), Toast.LENGTH_SHORT).show();
                            etPhoneNumber.setEnabled(true);
                            hideInfo();
                            NavHostFragment.findNavController(SignInPhoneFragment.this)
                                    .navigate(R.id.action_signUpPhoneFragment_to_mainActivity);
                        }
                        else{
                            Toast.makeText(getContext(), getString(R.string.sign_in_unsuccessful), Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                etCodeField.setError(getString(R.string.invalid_code));
                            }
                        }
                    }
                });
    }

    private void showInfo(String text, int color){
        tvInfo.setText(text);
        tvInfo.setTextColor(color);
        tvInfo.setVisibility(View.VISIBLE);

    }

    private void hideInfo(){
        tvInfo.setText("");
        tvInfo.setVisibility(View.GONE);
    }
}