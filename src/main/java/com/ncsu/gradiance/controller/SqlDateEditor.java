package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.Question;

import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SqlDateEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(text != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date udate = dateFormat.parse(text);
                java.sql.Date date = new Date(udate.getTime());
                setValue(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getAsText() {
        java.sql.Date date = (java.sql.Date)getValue();
        return date == null ? "" : date.toString();

    }

}
