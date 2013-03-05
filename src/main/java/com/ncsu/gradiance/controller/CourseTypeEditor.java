package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.Course;
import com.ncsu.gradiance.service.DataBaseQuery;
import org.springframework.validation.Errors;

import java.beans.PropertyEditorSupport;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ncsu.gradiance.service.QueriesFactory.*;

public class CourseTypeEditor extends PropertyEditorSupport {
    private DataBaseQuery dataBaseQuery;
    public CourseTypeEditor(DataBaseQuery dataBaseQuery){
        this.dataBaseQuery = dataBaseQuery;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(text == null)
            throw new IllegalArgumentException();
        ResultSet rs = this.dataBaseQuery.executeQuery(getQuery(GET_COURSE, text), null);
        try {
            while(rs.next()){
                Course course = new Course();
                course.setCourseId(rs.getString(1));
                course.setCourseName(rs.getString(2));
                course.setFromDate(rs.getDate(3));
                course.setToDate(rs.getDate(4));
                course.setTokenId(rs.getString(5));
                setValue(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();  
        }

    }

    @Override
    public String getAsText() {
        Course course = (Course)getValue();
        return course == null ? "" :course.toString();
    }
}
