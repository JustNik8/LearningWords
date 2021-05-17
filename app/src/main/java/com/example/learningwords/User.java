package com.example.learningwords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private Map<String, List<Integer>> learntWords;
    private List<Integer> wordsInProgress;
    private int trainingPercent = 100;
    private String userId;
    private String level;
    private int wordsAmount;

    public User(String userId) {
        learntWords = new HashMap<>();
        wordsInProgress = new ArrayList<>();
        this.userId = userId;
    }

    public User() {
        learntWords = new HashMap<>();
        wordsInProgress = new ArrayList<>();
    }

    public void addLearntNumberByLevel(String level, int number){
        List<Integer> temp = getLearntNumbersByLevel(level);
        temp.add(number);
        learntWords.put(level, temp);
    }

    public void addLearntNumbersByLevel(String level, List<Integer> numbers){
        List<Integer> temp = getLearntNumbersByLevel(level);
        temp.addAll(numbers);
        learntWords.put(level, temp);
    }

    public List<Integer> getLearntNumbersByLevel(String level){
        if (learntWords.containsKey(level)){
            return learntWords.get(level);
        }
        return new ArrayList<>();
    }

    public void deleteWordInProgress(int index){
        this.wordsInProgress.remove(index);
    }

    public void addWordInProgress(int number){
        this.wordsInProgress.add(number);
    }

    public void clearWordsInProgress(){
        this.wordsInProgress.clear();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getWordsAmount() {
        return wordsAmount;
    }

    public void setWordsAmount(int wordsAmount) {
        this.wordsAmount = wordsAmount;
    }

    public Map<String, List<Integer>> getLearntWords() {
        return learntWords;
    }

    public void setLearntWords(Map<String, List<Integer>> learntWords) {
        this.learntWords = learntWords;
    }

    public List<Integer> getWordsInProgress() {
        return wordsInProgress;
    }

    public void setWordsInProgress(List<Integer> wordsInProgress) {
        this.wordsInProgress = wordsInProgress;
    }

    public int getTrainingPercent() {
        return trainingPercent;
    }

    public void setTrainingPercent(int trainingPercent) {
        this.trainingPercent = trainingPercent;
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
