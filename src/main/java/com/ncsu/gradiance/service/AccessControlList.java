package com.ncsu.gradiance.service;

import com.ncsu.gradiance.domain.User;
import org.springframework.validation.Errors;

/**
 * User: shoubhik Date: 3/3/13 Time: 10:19 AM
 */
public interface AccessControlList {
    public void populateACL(User user, Errors errors);
    public boolean isProfessor(User user);
    public boolean isStudent(User user);
    public boolean isTAForTheCourse(User user, String course_id, Errors errors);
    public String getQueryToPopulateRoles(String roleName, String... args);
}
