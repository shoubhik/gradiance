package com.ncsu.gradiance.service;

import com.ncsu.gradiance.domain.User;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;

import static com.ncsu.gradiance.service.QueriesFactory.*;

public class RegisterUser {



    private DataBaseQuery dbQuery;
    private AccessControlList accessControlList;

    public RegisterUser( DataBaseQuery dbQuery, AccessControlList
            accessControlList){
        this.dbQuery = dbQuery;
        this.accessControlList = accessControlList;
    }

    public List<String> getRoles(){
        return Arrays.asList(
                new String[]{AccessControlListImpl
                        .Roles.PROF.roleName(), AccessControlListImpl.Roles.STUDENT.roleName()});
    }

    public void register(User user, Errors errors){
        if(!getRoles().contains(user.getRole().get(0))){
            errors.rejectValue("", "invalid.role", "the role is not valid");
            return;
        }
        dbQuery.executeQuery(getQuery(ADD_USER, user.getUid(), user.getPassword()),
                             errors);
        if(!errors.hasErrors()){
            String query = this.accessControlList.getQueryToPopulateRoles(
                    user.getRole().get(0), getArgs(user));
            dbQuery.executeQuery(query, errors);

        }
    }

    private String[] getArgs(User user ){
        AccessControlListImpl.Roles role =
                AccessControlListImpl.Roles.getRoles(user.getRole().get(0));
        switch(role){
            case PROF:
                return new String[] {user.getUid(), user.getUname()};
            case STUDENT:
                return new String[] {user.getUid(),user.getUname(),
                        String.valueOf(Math.round(Math.random() * 100000))};
        }
        throw new IllegalStateException() ;

    }
}
