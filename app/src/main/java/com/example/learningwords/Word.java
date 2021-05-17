package com.example.learningwords;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "words")
public class Word implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "original")
    private String original;
    @ColumnInfo(name = "translated")
    private String translated;
    @ColumnInfo(name = "type")
    private String type;

    @Ignore
    private boolean expanded;

    public Word(String original, String translated, String type) {
        this.original = original;
        this.translated = translated;
        this.type = type;
        expanded = false;
    }


    public Word() {
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", original='" + original + '\'' +
                ", translated='" + translated + '\'' +
                ", type='" + type + '\'' +
                ", expanded=" + expanded +
                '}';
    }
}
