package com.ncsu.gradiance.domain;

import java.util.List;

public class CreateQuestion {
    private Topic topic;
    private int numCorrect;
    private int numIncorrect;
    private Question question;
    private boolean isNewQuestion;
    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public int getNumCorrect() {
        return numCorrect;
    }

    public void setNumCorrect(int numCorrect) {
        this.numCorrect = numCorrect;
    }

    public int getNumIncorrect() {
        return numIncorrect;
    }

    public void setNumIncorrect(int numIncorrect) {
        this.numIncorrect = numIncorrect;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isNewQuestion() {
        return isNewQuestion;
    }

    public void setNewQuestion(boolean newQuestion) {
        isNewQuestion = newQuestion;
    }
}
