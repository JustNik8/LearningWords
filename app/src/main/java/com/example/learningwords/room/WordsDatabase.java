package com.example.learningwords.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.learningwords.Word;

@Database(entities = {Word.class}, version = 4, exportSchema = false)
public abstract class WordsDatabase extends RoomDatabase {
    public abstract WordDao wordDao();
}
