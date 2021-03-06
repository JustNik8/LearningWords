package com.example.learningwords.ui.dictionary;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.learningwords.Constants;
import com.example.learningwords.DBClient;
import com.example.learningwords.MainActivity;
import com.example.learningwords.R;
import com.example.learningwords.Repository;
import com.example.learningwords.Word;

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

    public LiveData<List<Word>> getDictionary(){
        return DBClient.getInstance(getApplication())
                .getAppDatabase()
                .wordDao()
                .getWordsByType(Constants.WORD_TYPE_DICTIONARY);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public void deleteWord(Word word){
        repository.deleteWord(word);
        Toast.makeText(getApplication().getApplicationContext(), getApplication().getString(R.string.word_deleted_text), Toast.LENGTH_SHORT).show();
    }

    public void update(Word word) {
        repository.update(word);
    }

}