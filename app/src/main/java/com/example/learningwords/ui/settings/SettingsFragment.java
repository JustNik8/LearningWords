package com.example.learningwords.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.learningwords.Constants;
import com.example.learningwords.FireBaseRef;
import com.example.learningwords.MainActivity;
import com.example.learningwords.R;
import com.example.learningwords.User;
import com.example.learningwords.ui.home.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Set;

import static com.example.learningwords.Constants.USERS_KEY;

public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences shared;
    String userId;
    DatabaseReference dbUserRef;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);

        shared = PreferenceManager.getDefaultSharedPreferences(getContext());
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance(FireBaseRef.ref);
        dbUserRef = database.getReference(USERS_KEY);


        Preference themePreference = findPreference("theme");
        themePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
                startActivity(intent);
                return true;
            }
        });

        Preference notificationPreference = findPreference("notification");
        if (notificationPreference != null) {
            Log.d(MainActivity.LOG_TAG, "notificationPreference is not null");
            notificationPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Log.d(MainActivity.LOG_TAG, "INSIDE");
                        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, Constants.packageName);
                        startActivityForResult(intent, 0);
                        return true;
                    }
                    Log.d(MainActivity.LOG_TAG, "OUTSIDE");
                    return false;
                }
            });
        }

        Preference clearPreference = findPreference("clear_dictionary");
        if (clearPreference != null){
            clearPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SettingsAlertDialog dialog = new SettingsAlertDialog();
                    dialog.show(getActivity().getSupportFragmentManager(), null);
                    return true;
                }
            });
        }

        EditTextPreference wordsAmountPreference = findPreference("words_amount");
        wordsAmountPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String amountString = (String) newValue;
                for (int i = 0; i < amountString.length(); i++){
                    // Checking for a number
                    if ((amountString.charAt(i) < '0' || amountString.charAt(i) > '9') && amountString.charAt(i) != '-'){
                        Toast.makeText(getContext(), getString(R.string.input_number_error), Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

                int amount = Integer.parseInt(amountString);
                // Checking for valid number
                if (amount < 5 || amount > 30){
                    Toast.makeText(getContext(), getString(R.string.invalid_number_error), Toast.LENGTH_LONG).show();
                    return false;
                }

                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("changed", 1);
                editor.apply();
                dbUserRef.child(userId).child(Constants.AMOUNT_KEY).setValue(amount);
                return true;
            }
        });

        ListPreference levelPref = findPreference("level");
        levelPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("changed", 1);
                editor.apply();
                dbUserRef.child(userId).child(Constants.LEVEL_KEY).setValue(((String) newValue).toUpperCase());
                return true;
            }
        });

        Preference signOutPref = findPreference("sign_out");
        if (signOutPref != null){
            signOutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SignOutDialog signOutDialog = new SignOutDialog();
                    signOutDialog.show(getActivity().getSupportFragmentManager(), null);
                    return true;
                }
            });
        }


    }

}
