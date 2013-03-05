package com.ncsu.gradiance.service;

import org.springframework.validation.Errors;

import java.sql.ResultSet;

/**
 * User: shoubhik Date: 3/3/13 Time: 9:19 AM
 */
public interface DataBaseQuery {
    public ResultSet executeQuery(String query, Errors errors);
    public ResultSet executeQuery(String query, Errors errors, String field);
}
