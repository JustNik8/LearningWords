package com.example.learningwords;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "words")
public class Word implements Serializable {
    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo(name = "original")
    private String original;
    @ColumnInfo(name = "translated")
    private String translated;

    public Word(String original, String translated) {
        this.original = original;
        this.translated = translated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }
}
