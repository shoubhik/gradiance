package com.ncsu.gradiance.domain;

import javax.validation.constraints.NotNull;

public class Topic {
    @NotNull
    private String courseId;
    @NotNull
    private String topicId;
    @NotNull
    private String topicName;

    public Topic() {
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String toString(){
        return this.courseId + "-" + this.topicId + "-" + this.topicName;
    }
}
