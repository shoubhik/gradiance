package com.ncsu.gradiance.service;

import org.springframework.validation.Errors;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ncsu.gradiance.service.QueriesFactory.*;

/**
 * class to share some common functionality
 */
public class BaseService {

    protected DataBaseQuery dataBaseQuery;

    public BaseService(DataBaseQuery dataBaseQuery) {
        this.dataBaseQuery = dataBaseQuery;
    }

    protected int getNextRowCount(String tableName, Errors errors, String field)
            throws SQLException {
        String query =  getQuery(NUM_ROWS, tableName);
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors, field);
        int id = 0;
        while(rs.next()) {
            id = rs.getInt(1);
        }
        return ++id;

    }

}
