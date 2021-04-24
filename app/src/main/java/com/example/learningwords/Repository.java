package com.example.learningwords;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {

    Context context;
    private LiveData<List<Word>> allWords;
    public Repository(Context context){
        this.context = context;
        allWords = DBClient.getInstance(context).getAppDatabase()
                .wordDao()
                .getAllWords();
    }

    public void insert(Word word){
        Thread addWordThread = new Thread(){
            @Override
            public void run() {
                DBClient.getInstance(context)
                        .getAppDatabase()
                        .wordDao()
                        .insert(word);
            }
        };
        addWordThread.start();
    }

    public LiveData<List<Word>> getAllWords(){
        return allWords;
    }

    public void deleteAll(){
        Thread deleteAllThread = new Thread(){
            @Override
            public void run() {
                DBClient.getInstance(context).getAppDatabase()
                        .wordDao()
                        .deleteAll();
            }
        };
        deleteAllThread.start();
    }

    public void deleteWord(Word word){
        Thread deleteWordThread = new Thread(){
            @Override
            public void run() {
                DBClient.getInstance(context).getAppDatabase()
                        .wordDao()
                        .delete(word);
            }
        };
        deleteWordThread.start();
    }

    public void update(Word word){
        Log.d(MainActivity.LOG_TAG, "Inside update:" + word.getOriginal() + " " + word.getTranslated());
        Thread updateThread = new Thread(){
            @Override
            public void run() {
                DBClient.getInstance(context).getAppDatabase()
                        .wordDao()
                        .update(word);
            }
        };
        updateThread.start();
    }

}
