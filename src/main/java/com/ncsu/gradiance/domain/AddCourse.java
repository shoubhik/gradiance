package com.ncsu.gradiance.domain;

/**
 * User: shoubhik Date: 3/3/13 Time: 3:02 PM
 */
public class AddCourse {
    private Course course;
    private User user;
    private String token;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
