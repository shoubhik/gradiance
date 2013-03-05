package com.ncsu.gradiance.domain;

/**
 * User: shoubhik Date: 5/3/13 Time: 3:29 AM
 */
public class Report {
    private String query;
    private boolean showQuery;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isShowQuery() {
        return showQuery;
    }

    public void setShowQuery(boolean showQuery) {
        this.showQuery = showQuery;
    }
}
