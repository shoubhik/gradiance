package com.ncsu.gradiance.service;

import com.ncsu.gradiance.domain.User;
import org.springframework.validation.Errors;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ncsu.gradiance.service.QueriesFactory.*;

/**
 * User: shoubhik Date: 3/3/13 Time: 12:32 AM
 */
public class ValidateUser {



    private DataBaseQuery dbQuery;
    public ValidateUser(DataBaseQuery dbQuery){
        this.dbQuery = dbQuery;
    }

    public boolean isUserValid(User user,  Errors errors) throws SQLException {
        ResultSet rs = dbQuery.executeQuery(
                getQuery(VALIDATE_USER_QUERY, user.getUid()), errors);
        while(rs.next()){
            if(user.getPassword().equals(rs.getString("pwd")))
                return true;
        }
        errors.rejectValue("", "invalid.user" ,"user not found!!");
        return false;
    }

}
