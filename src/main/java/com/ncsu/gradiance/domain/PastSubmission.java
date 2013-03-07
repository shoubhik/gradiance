package com.ncsu.gradiance.domain;

import java.util.List;

/**
 * User: shoubhik Date: 6/3/13 Time: 10:33 PM
 */
public class PastSubmission {
    public List<AttemptHomework> getAttemptHomeworks() {
        return attemptHomeworks;
    }

    public void setAttemptHomeworks(List<AttemptHomework> attemptHomeworks) {
        this.attemptHomeworks = attemptHomeworks;
    }

    List<AttemptHomework>  attemptHomeworks;
}
