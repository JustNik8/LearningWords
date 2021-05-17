package com.example.learningwords.ui.training;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learningwords.Constants;
import com.example.learningwords.DBClient;
import com.example.learningwords.Word;

import java.util.List;

public class TrainingViewModel extends AndroidViewModel {


    public TrainingViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Word>> getWords(){
        return DBClient.getInstance(getApplication())
                .getAppDatabase()
                .wordDao()
                .getWordsByType(Constants.WORD_TYPE_HOME);
    }
}