package com.example.learningwords.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.learningwords.MainActivity;
import com.example.learningwords.R;
import com.example.learningwords.ui.home.HomeViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsViewModel settingsViewModel;
    private int sourceAmount;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);

        Preference notificationPreference = findPreference("notification");
        if (notificationPreference != null) {
            Log.d(MainActivity.LOG_TAG, "notificationPreference is not null");
            notificationPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Log.d(MainActivity.LOG_TAG, "INSIDE");
                        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, MainActivity.packageName);
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
        sourceAmount = Integer.parseInt(wordsAmountPreference.getText());
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
                if (amount < 1 || amount > 50){
                    Toast.makeText(getContext(), getString(R.string.invalid_number_error), Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });

    }

}
