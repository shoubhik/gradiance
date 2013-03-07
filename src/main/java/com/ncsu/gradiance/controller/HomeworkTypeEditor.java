package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.Homework;
import com.ncsu.gradiance.domain.Question;
import com.ncsu.gradiance.service.HomeworkService;

import java.beans.PropertyEditorSupport;

/**
 * User: shoubhik Date: 5/3/13 Time: 1:19 AM
 */
public class HomeworkTypeEditor extends PropertyEditorSupport {

    private HomeworkService homeworkService;
    public HomeworkTypeEditor(HomeworkService homeworkService){
        this.homeworkService = homeworkService;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(text != null && !text.trim().equals("")){
            String tokens[] = text.split("-");
            Homework homework = this.homeworkService.getHomework(
                    Integer.parseInt(tokens[0]), null);
            homework.setNewHomework(false);
            setValue(homework);
        }
    }

    @Override
    public String getAsText() {
        Homework homework = (Homework)getValue();
        return homework == null ? "" : homework.toString();

    }

}
