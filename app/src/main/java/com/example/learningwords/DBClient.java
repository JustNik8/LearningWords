package com.example.learningwords;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.learningwords.room.WordsDatabase;

public class DBClient {
    private Context ctx;
    private static DBClient instance;
    private WordsDatabase appDatabase;

    public DBClient(Context ctx) {
        this.ctx = ctx;
        appDatabase = Room.databaseBuilder(ctx, WordsDatabase.class, "WordDataBase").build();
    }

    public static DBClient getInstance(Context ctx){
        if (instance == null){
            instance = new DBClient(ctx);
        }
        return instance;
    }

    public WordsDatabase getAppDatabase() {
        return appDatabase;
    }
}
