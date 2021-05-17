package com.example.learningwords;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.learningwords.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String CONFIGURED = "configured";

    FirebaseDatabase database;

    private boolean configured = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dictionary, R.id.navigation_training, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        shared.registerOnSharedPreferenceChangeListener(this);

        if(savedInstanceState == null) {
            configured = true;
            changeTheme(shared.getString("theme", "default"));
        }
        else {
            configured = savedInstanceState.getBoolean(CONFIGURED);
            if (!configured){
                changeTheme(shared.getString("theme", "default"));
            }
        }

        database = FirebaseDatabase.getInstance(FireBaseRef.ref);
        DatabaseReference dbUsersRef = database.getReference(Constants.USERS_KEY);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        User user = new User(userId);

        dbUsersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    dbUsersRef.child(userId).setValue(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Log.d(LOG_TAG, user.toString());

        if (!shared.contains(Constants.CHANGED_KEY)){
            SharedPreferences.Editor editor = shared.edit();
            editor.putInt(Constants.CHANGED_KEY, 1);
            editor.apply();
        }

    }

    private void changeTheme(String theme){
        Log.d(LOG_TAG, "INSIDE changeTheme");
        if (theme.equals(getResources().getStringArray(R.array.themes_values)[0])){ //Default theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        else if (theme.equals(getResources().getStringArray(R.array.themes_values)[1])){ //Light theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if (theme.equals(getResources().getStringArray(R.array.themes_values)[2])){ //Dark theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(LOG_TAG, "onSharedPreferenceChanged");
        if (key.equals("theme")){
            changeTheme(sharedPreferences.getString(key, "default"));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CONFIGURED, configured);
    }

}