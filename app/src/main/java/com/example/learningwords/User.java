package com.example.learningwords;

import java.util.List;

public class User {
    private String name;
    private List<HomeWord> words;
    private String id;

    public User(String name, List<HomeWord> words, String id) {
        this.name = name;
        this.words = words;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<HomeWord> getWords() {
        return words;
    }

    public String getId() {
        return id;
    }
}
