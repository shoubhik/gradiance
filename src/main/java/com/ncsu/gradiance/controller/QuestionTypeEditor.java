package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.Question;
import com.ncsu.gradiance.domain.Topic;

import java.beans.PropertyEditorSupport;

/**
 * User: shoubhik Date: 4/3/13 Time: 6:38 PM
 */
public class QuestionTypeEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(text != null){
            String tokens[] = text.split("-");
            Question question = new Question();
            question.setId(Integer.parseInt(tokens[0]));
            question.setText(tokens[1]);
            setValue(question);

        }
    }

    @Override
    public String getAsText() {
        Question question = (Question)getValue();
        return question == null ? "" : question.toString();

    }

}
