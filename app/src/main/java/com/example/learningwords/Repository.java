package com.example.learningwords;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    Context context;
    private List<Word> allWords;
    public Repository(Context context){
        this.context = context;
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

    public void deleteAllWordsByType(String type){
        Thread deleteThread = new Thread(){
            @Override
            public void run() {
                DBClient.getInstance(context)
                        .getAppDatabase()
                        .wordDao()
                        .deleteAllWordsByType(type);
            }
        };
        deleteThread.start();
    }

}
