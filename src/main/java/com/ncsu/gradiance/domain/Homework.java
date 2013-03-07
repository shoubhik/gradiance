package com.ncsu.gradiance.domain;

import java.sql.Date;

public class Homework {

    private Topic topic;
    private Course course;
    private java.sql.Date startDate;
    private java.sql.Date endDate;
    private int numAttempts;
    private ScoreSelectionScheme scoreSelectionScheme;
    private int correctPts;
    private int incorrectPts;
    private int numQuestions;
    private String name;
    private boolean newHomework;
    private int id;
    private int attemptCount;
    private int attemptId;
    private int score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

    public int getIncorrectPts() {
        return incorrectPts;
    }

    public void setIncorrectPts(int incorrectPts) {
        this.incorrectPts = incorrectPts;
    }

    public int getCorrectPts() {
        return correctPts;
    }

    public void setCorrectPts(int correctPts) {
        this.correctPts = correctPts;
    }

    public ScoreSelectionScheme getScoreSelectionScheme() {
        return scoreSelectionScheme;
    }

    public void setScoreSelectionScheme(
            ScoreSelectionScheme scoreSelectionScheme) {
        this.scoreSelectionScheme = scoreSelectionScheme;
    }

    public int getNumAttempts() {
        return numAttempts;
    }

    public void setNumAttempts(int numAttempts) {
        this.numAttempts = numAttempts;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }


    public boolean isNewHomework() {
        return newHomework;
    }

    public void setNewHomework(boolean newHomework) {
        this.newHomework = newHomework;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString(){
        return this.id+"-"+this.name;
    }

    public String toStringWithEndDate(){
        return toString() + "-Last Date:"+endDate.toString();
    }

    public String toAttemptString(){
        return this.attemptId + "-" + this.name + "-" + "Attempt " + attemptCount;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
