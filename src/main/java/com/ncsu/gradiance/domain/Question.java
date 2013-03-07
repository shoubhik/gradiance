package com.ncsu.gradiance.domain;

import java.util.List;

public class Question {
    private int id;
    private String topicId;
    private String text;
    private int difficultyLevel;
    private int pointIncorrect;
    private int pointCorrect;
    private Hint hint;
    private boolean hasHint;
    private List<Answer> answers;
    private Answer response;

    public Hint getHint() {
        return hint;
    }

    public void setHint(Hint hint) {
        this.hint = hint;
    }

    public int getPointCorrect() {
        return pointCorrect;
    }

    public void setPointCorrect(int pointCorrect) {
        this.pointCorrect = pointCorrect;
    }

    public int getPointIncorrect() {
        return pointIncorrect;
    }

    public void setPointIncorrect(int pointIncorrect) {
        this.pointIncorrect = pointIncorrect;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isHasHint() {
        return hasHint;
    }

    public void setHasHint(boolean hasHint) {
        this.hasHint = hasHint;
    }


    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String toString(){
        return this.id+"-"+this.text;
    }

    public boolean equals(Object o){
        if(o == null || !(o instanceof Question))
            return false;
        Question that = (Question)o;
        return this.id == that.id;
    }

    public boolean isResponseCorrect(int ansId){
        for(Answer answer : this.answers)
            if(answer.getId() == ansId && answer.isCorrect())
                return true;
        return false;

    }

    public Answer getResponse() {
        return response;
    }

    public void setResponse(Answer response) {
        this.response = response;
    }
}
