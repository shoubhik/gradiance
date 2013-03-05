package com.ncsu.gradiance.service;

import com.ncsu.gradiance.domain.User;
import org.springframework.validation.Errors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static com.ncsu.gradiance.service.QueriesFactory.*;

/**
 * User: shoubhik Date: 3/3/13 Time: 10:21 AM
 */
public class AccessControlListImpl implements AccessControlList {



    public static enum Roles{
        PROF("prof", IS_PROF_QUERY, "profHome", ADD_PROF ),
        TA("ta", IS_TA_QUERY, "taHome", null),
        STUDENT("student", IS_A_STUDENT_QUERY, "profHome", ADD_STUDENT);
        private String role;
        private String query;
        private String homePage;
        private String insertQuery;
        Roles(String role, String query, String homePage, String insertQuery){
            this.role = role;
            this.query = query;
            this.homePage = homePage;
            this.insertQuery = insertQuery;
        }

        public String getQuery(String... args){
            return QueriesFactory.getQuery(this.query, args);
        }

        public String roleName(){
            return this.role;
        }

        public String getInsertQuery(String... args){
            return QueriesFactory.getQuery(this.insertQuery, args);

        }

        public String getHomePage(){
            return this.homePage;
        }

        public static Roles getRoles(String roleName){
            for(Roles role : Roles.values()){
                if(roleName.equals(role.roleName()))
                    return role;
            }
            throw   new IllegalArgumentException("invalid role name");

        }
    }


    private DataBaseQuery dbQuery;
    private MappedPageFactory mappedPageFactory;
    public AccessControlListImpl(DataBaseQuery dbQuery,
                                 MappedPageFactory mappedPageFactory){
        this.dbQuery = dbQuery;
        this.mappedPageFactory = mappedPageFactory;
    }
    @Override
    public void populateACL(User user, Errors errors) {
        Roles roles[] = {Roles.PROF, Roles.STUDENT};
        for(Roles role : roles) {
            try {
                if(hasRole(user, role.getQuery(user.getUid()), errors)){
                    user.setRoleName(role.roleName());
                    user.setHomePage(role.getHomePage());
                    user.setMappedPages(this.mappedPageFactory.getMappedPages(role));
                    return;
                }
            } catch (SQLException e) {
                errors.rejectValue("", "sql.exception", e.getSQLState());
            }
        }
        errors.rejectValue("", "invalid.role", "not a valid role");
    }

    @Override
    public boolean isProfessor(User user) {
        return user.getRole().contains(Roles.PROF.roleName());
    }

    @Override
    public boolean isStudent(User user) {
        return user.getRole().contains(Roles.STUDENT.roleName());
    }

    @Override
    public boolean isTAForTheCourse(User user, String course_id, Errors errors) {
        return false;
    }

    @Override
    public String getQueryToPopulateRoles(String roleName, String... args) {
        return Roles.getRoles(roleName).getInsertQuery(args);
    }

    private boolean hasRole(User user, String query, Errors errors)
            throws SQLException {
        ResultSet rs = dbQuery.executeQuery(query,errors);
        if(errors.hasErrors())
            return false;
        while(rs.next()){
            String name  = rs.getString("name");
            user.setUname(name);
            return true;
        }
        return false;
    }
}
