package com.example.learningwords;

public class HomeWord {
    private String original;
    private String translated;
    private String level;

    public HomeWord(String original, String translated, String level) {
        this.original = original;
        this.translated = translated;
        this.level = level;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
