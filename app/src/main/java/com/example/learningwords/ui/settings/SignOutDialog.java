package com.example.learningwords.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.learningwords.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class SignOutDialog extends DialogFragment {

    private GoogleSignInClient mGoogleSignInClient;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.sign_out_message))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Firebase sign out
                        FirebaseAuth.getInstance().signOut();

                        // If the user entered with google account, he signs out with google too
                        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
                        if (signInAccount != null){
                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(getString(R.string.default_web_client_id))
                                    .requestEmail()
                                    .build();

                            mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
                            mGoogleSignInClient.signOut();
                        }
                        NavHostFragment.findNavController(SignOutDialog.this)
                                .navigate(R.id.action_navigation_settings_to_loginActivity);
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                });


        return builder.create();
    }
}
