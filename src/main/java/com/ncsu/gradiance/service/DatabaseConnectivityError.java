package com.ncsu.gradiance.service;

/**
 * User: shoubhik Date: 3/3/13 Time: 9:16 AM
 */
public class DatabaseConnectivityError extends RuntimeException {

    public String getMessage(){
        return "could not connect to database!!";
    }
}
