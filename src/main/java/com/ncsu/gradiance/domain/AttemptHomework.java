package com.ncsu.gradiance.domain;

import java.util.List;

public class AttemptHomework {

    private Homework homework;
    private List<Question> questions;
    private User user;
    private int id;
    private int score;

    public AttemptHomework(){
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Homework getHomework() {
        return homework;
    }

    public User getUser() {
        return user;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
