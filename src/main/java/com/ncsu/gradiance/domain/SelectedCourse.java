package com.ncsu.gradiance.domain;

/**
 * User: shoubhik Date: 3/3/13 Time: 8:59 PM
 */
public class SelectedCourse {
    private Course course;
    private CreateQuestion createQuestion;
    private Homework homework;
    private Report report;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CreateQuestion getCreateQuestion() {
        return createQuestion;
    }

    public void setCreateQuestion(CreateQuestion createQuestion) {
        this.createQuestion = createQuestion;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
