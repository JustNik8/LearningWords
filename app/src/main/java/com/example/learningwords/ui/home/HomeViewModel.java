package com.example.learningwords.ui.home;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import com.example.learningwords.MainActivity;
import com.example.learningwords.Word;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Word>> wordsList;
    private int wordsAmount;

    public HomeViewModel() {
        wordsList = new MutableLiveData<>();
    }

    public int getWordsAmount() {
        return wordsAmount;
    }

    public void setWordsAmount(int wordsAmount) {
        this.wordsAmount = wordsAmount;
    }
}