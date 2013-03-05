package com.ncsu.gradiance.domain;

public class ScoreSelectionScheme {
    private int schemeId;
    private String name;

    public int getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(int schemeId) {
        this.schemeId = schemeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return this.schemeId + "-" + this.name;
    }
}
