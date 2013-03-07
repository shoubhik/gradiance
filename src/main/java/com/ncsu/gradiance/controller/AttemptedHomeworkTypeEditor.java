package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.Homework;
import com.ncsu.gradiance.domain.ScoreSelectionScheme;

import java.beans.PropertyEditorSupport;

/**
 * User: shoubhik Date: 7/3/13 Time: 1:06 AM
 */
public class AttemptedHomeworkTypeEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(text != null){
            String tokens[] = text.split("-");
            Homework homework = new Homework();
            homework.setAttemptId(Integer.parseInt(tokens[0]));
            setValue(homework);
        }
    }

    @Override
    public String getAsText() {
        Homework homework = (Homework)getValue();
        return homework == null ? "" : homework.toAttemptString();

    }

}
