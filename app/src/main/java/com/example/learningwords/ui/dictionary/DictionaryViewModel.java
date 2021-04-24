package com.example.learningwords.ui.dictionary;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learningwords.MainActivity;
import com.example.learningwords.Repository;
import com.example.learningwords.Word;

import java.util.ArrayList;
import java.util.List;

public class DictionaryViewModel extends AndroidViewModel {

    private MutableLiveData<Word> word;
    private Repository repository;

    public DictionaryViewModel(@NonNull Application application) {
        super(application);
        word = new MutableLiveData<>();
        repository = new Repository(application);
    }


    public void addWord(Word word){
        this.word.setValue(word);
        Log.d(MainActivity.LOG_TAG, "Inside setWord:" + this.word.getValue().getOriginal() + " "  + this.word.getValue().getTranslated());
    }

    public LiveData<Word> getWord(){
        Log.d(MainActivity.LOG_TAG, "Inside getWord:");
        return this.word;
    }

    public void insert(Word word){
        repository.insert(word);
    }

    public LiveData<List<Word>> getAllWords(){
        return repository.getAllWords();
    }

    public void deleteAll(){
        repository.deleteAll();
    }

}