package com.ncsu.gradiance.domain;

import com.ncsu.gradiance.service.QuestionService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.ncsu.gradiance.service.QueriesFactory.getCurrentDate;

/**
 * User: shoubhik Date: 5/3/13 Time: 3:09 AM
 */
public class EditHomeworkValidator implements Validator {
    private int pageNum;
    private QuestionService questionService;
    public EditHomeworkValidator (int pageNum, QuestionService questionService){
        this.pageNum = pageNum;
        this.questionService = questionService;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return Homework.class.isAssignableFrom(clazz);
    }



    @Override
    public void validate(Object target, Errors errors) {
        if(!(target instanceof Homework))
            throw new IllegalArgumentException("target is wrong");
        Homework homework = (Homework)target;
        switch(pageNum){
            case 0:
                validateTopicHasHomeworks(homework.getTopic(), errors);
                break;
            case 2:
                validateHomework(homework, errors);

        }
    }

    private void validateHomework(Homework homework, Errors errors) {
        if(homework.getStartDate().after(homework.getEndDate()))
            errors.rejectValue("homework.endDate", "date", "Start date cannot be after end date");
        java.sql.Date currDate = getCurrentDate();
        if(currDate.after(homework.getStartDate()))
            errors.rejectValue("homework.startDate", "date", "Start date cannot be before today");
        if(currDate.after(homework.getEndDate()))
            errors.rejectValue("homework.endDate", "date", "End date cannot be before today");
        if(homework.getNumAttempts() < 0 )
            errors.rejectValue("homework.numAttempts", "date", "Attempts should be 0 or more ");
        if(homework.getCorrectPts() <= 0 )
            errors.rejectValue("homework.correctPts", "date", "Points cannot be less than 1");
        if(homework.getIncorrectPts() < 0 )
            errors.rejectValue("homework.incorrectPts", "date", "Points cannot be less than 0");
        int maxQuestions = this.questionService.getNumberOfQuestionsInTheTopic(
                homework.getTopic(), errors);
        if(homework.getNumQuestions() > maxQuestions)
            errors.rejectValue("homework.numQuestions", "date", "This topic has " +
                    maxQuestions + "questions");
    }

    private void validateTopicHasHomeworks(Topic topic,
                                           Errors errors){
        int count = this.questionService.getNumberOfHomeworks(topic,errors);
        if(count == 0)
            errors.rejectValue("", "invalid.answers", "topic has no homeworks. select a topic that has one");

    }
}
