package com.ncsu.gradiance.service;

import org.springframework.validation.Errors;

import java.sql.*;

/**
 * User: shoubhik Date: 3/3/13 Time: 12:22 AM
 */
public class DataBaseConnectionImpl implements  DataBaseQuery{

    static final String JDBC_URL
            = "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl";
    static final String USER_NAME = "sbhatta9";
    static final String PASSWORD = "001062692";
    private Connection conn;

    public DataBaseConnectionImpl(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(JDBC_URL, USER_NAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseConnectivityError();
        }
    }

    @Override
    public ResultSet executeQuery(String query, Errors errors) {
        return executeQuery(query, errors, "");
    }

    @Override
    public ResultSet executeQuery(String query, Errors errors, String field) {
        ResultSet rs = null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            conn.commit();
        } catch (SQLException e) {
            errors.rejectValue(field, "sql.exception" ,e.getMessage());
        }
        return rs;
    }
}
