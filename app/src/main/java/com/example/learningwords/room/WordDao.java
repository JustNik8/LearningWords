package com.example.learningwords.room;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.learningwords.Word;

import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insert(Word word);

    @Delete
    void delete(Word word);

    @Update
    void update(Word word);

    @Query("SELECT * FROM words WHERE type = :type")
    LiveData<List<Word>> getWordsByType(String type);

    @Query("DELETE FROM words")
    void deleteAll();

    @Query("DELETE FROM words WHERE type = :type")
    void deleteAllWordsByType(String type);
}
