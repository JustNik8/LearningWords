package com.example.learningwords;

import java.util.HashMap;
import java.util.Map;

public class User {

    private Map<String, Integer> learntWords;

    private int trainingPercent = 100;
    private String userId;

    public User(String userId) {
        learntWords = new HashMap<>();
        this.userId = userId;
    }

    public User() {
        learntWords = new HashMap<>();
    }

    public int getLearntWordsByLevel(String level) {
        int amount;
        try {
            amount = learntWords.get(level);
        }
        catch (NullPointerException e){
            amount = 0;
        }
        return amount;
    }

    public void addLearntWordsByLevel(String level, int amount) {
        this.learntWords.put(level, getLearntWordsByLevel(level) + amount);
    }

    public int getTrainingPercent() {
        return trainingPercent;
    }

    public void setTrainingPercent(int trainingPercent) {
        this.trainingPercent = trainingPercent;
    }

    public Map<String, Integer> getLearntWords() {
        return learntWords;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "learntWords=" + learntWords +
                ", trainingPercent=" + trainingPercent +
                ", userId='" + userId + '\'' +
                '}';
    }
}
