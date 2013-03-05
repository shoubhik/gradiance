package com.ncsu.gradiance.domain;

import javax.validation.constraints.NotNull;
import java.sql.Date;

/**
 * User: shoubhik Date: 3/3/13 Time: 2:47 PM
 */
public class Course {
    @NotNull
    private String courseId;
    @NotNull
    private String courseName;
    @NotNull
    private Date fromDate;
    @NotNull
    private Date toDate;

    private String tokenId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String toString(){
        return this.courseId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
