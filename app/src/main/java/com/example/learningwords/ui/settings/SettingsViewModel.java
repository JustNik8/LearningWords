package com.example.learningwords.ui.settings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learningwords.Repository;

import java.util.ArrayList;

public class SettingsViewModel extends AndroidViewModel {

    Repository repository;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}