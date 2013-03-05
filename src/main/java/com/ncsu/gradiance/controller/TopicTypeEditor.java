package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.Topic;

import java.beans.PropertyEditorSupport;

public class TopicTypeEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(text != null){
            Topic topic = new Topic();
            String tokens[] = text.split("-");
            topic.setCourseId(tokens[0]);
            topic.setTopicId(tokens[1]);
            topic.setTopicName(tokens[2]);
            setValue(topic);
        }
    }

    @Override
    public String getAsText() {
        Topic topic = (Topic)getValue();
        return topic == null ? "" : topic.toString();

    }

}
