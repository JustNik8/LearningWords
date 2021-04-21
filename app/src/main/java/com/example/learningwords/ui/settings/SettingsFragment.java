package com.example.learningwords.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;

import com.example.learningwords.MainActivity;
import com.example.learningwords.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);
    }
}



/*package com.example.learningwords.ui.settings;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.learningwords.R;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

         final String DEFAULT_THEME = getResources().getStringArray(R.array.themes)[0];
         final String LIGHT_THEME = getResources().getStringArray(R.array.themes)[1];
         final String DARK_THEME = getResources().getStringArray(R.array.themes)[2];

         Spinner setThemeSpinner = root.findViewById(R.id.set_theme_spinner);

         ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.themes, android.R.layout.simple_spinner_item);

         spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setThemeSpinner.setAdapter(spinnerAdapter);

        setThemeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String theme = parent.getItemAtPosition(position).toString();
                if (theme.equals(DEFAULT_THEME)){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
                else if (theme.equals(LIGHT_THEME)){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                else if (theme.equals(DARK_THEME)){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //required by interface
            }
        });



        return root;
    }


} */