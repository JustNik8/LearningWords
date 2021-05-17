package com.example.learningwords.ui.home;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import com.example.learningwords.Constants;
import com.example.learningwords.DBClient;
import com.example.learningwords.MainActivity;
import com.example.learningwords.Word;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {


    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Word>> getWords(){
        return DBClient.getInstance(getApplication())
                .getAppDatabase()
                .wordDao()
                .getWordsByType(Constants.WORD_TYPE_HOME);
    }

}