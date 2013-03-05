package com.ncsu.gradiance.domain;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * User: shoubhik Date: 3/3/13 Time: 12:37 AM
 */
@Component
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "uid", "required.name",
                                      "username is required.");
        ValidationUtils.rejectIfEmpty(errors, "password", "required.password",
                                      "password is required.");
    }
}
