package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.ScoreSelectionScheme;

import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * User: shoubhik Date: 4/3/13 Time: 10:15 PM
 */
public class ScoringSchemeTypeEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(text != null){
            String tokens[] = text.split("-");
            ScoreSelectionScheme scoreSelectionScheme = new
                    ScoreSelectionScheme();
            scoreSelectionScheme.setSchemeId(Integer.parseInt(tokens[0]));
            scoreSelectionScheme.setName(tokens[1]);
            setValue(scoreSelectionScheme);
        }
    }

    @Override
    public String getAsText() {
        ScoreSelectionScheme scheme = (ScoreSelectionScheme)getValue();
        return scheme == null ? "" : scheme.toString();

    }

}
