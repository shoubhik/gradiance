package com.ncsu.gradiance.domain;

import com.ncsu.gradiance.service.QuestionService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * User: shoubhik Date: 4/3/13 Time: 6:58 PM
 */
public class AddAnswerValidator implements Validator {

    private int pageNum;
    private QuestionService questionService;
    public AddAnswerValidator(int pageNum, QuestionService questionService){
        this.pageNum = pageNum;
        this.questionService = questionService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateQuestion.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(!(target instanceof CreateQuestion))
            throw new IllegalArgumentException("target is wrong");
        CreateQuestion createQuestion = (CreateQuestion)target;
        switch(this.pageNum){
            case 0:
                validateTopicHasQuestions(createQuestion, errors);
                break;
            case 2:
                validateNumberOfQuestions(createQuestion, errors);
                break;
            case 3:
                validateAnswerFields(createQuestion, errors);
        }

    }

    private void validateTopicHasQuestions(CreateQuestion createQuestion,
                                           Errors errors){
        int count = this.questionService.getNumberOfQuestionsInTheTopic(createQuestion.
                getTopic(), errors);
        if(count == 0)
            errors.rejectValue("", "invalid.answers", "topic has no answers. select a topic that has one");

    }

    private void validateAnswerFields(CreateQuestion createQuestion,
                                      Errors errors){
        Question question = createQuestion.getQuestion();
        if(question.getAnswers().size() !=
                createQuestion.getNumCorrect() + createQuestion.getNumIncorrect())
            errors.rejectValue("", "invalid.answers", "all answers are mandatory");
        for(Answer answer : question.getAnswers()){
            if(!answer.isValid()){
                errors.rejectValue("", "invalid.answers",
                                   "all answers and their hints are mandatory and cannot be left blank");
                return;
            }
        }
    }

    private void validateNumberOfQuestions(CreateQuestion createQuestion,
                                           Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "createQuestion.numCorrect",
                                      "required.numCorrect",
                                      "Correct answers have to be minimum 1");
        ValidationUtils.rejectIfEmpty(errors, "createQuestion.numIncorrect", "required.numIncorrect",
                                      "Incorrect answers have to be minimum 3");
        if(createQuestion.getNumCorrect() + createQuestion.getNumIncorrect() <= 0)
            errors.rejectValue("", "invalid.answer.number", "you need to add at lest one answer");
    }
}
