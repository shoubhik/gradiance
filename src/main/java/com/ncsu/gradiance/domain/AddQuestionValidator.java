package com.ncsu.gradiance.domain;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * User: shoubhik Date: 4/3/13 Time: 11:09 AM
 */
public class AddQuestionValidator implements Validator {

    private int pageNum;
    public AddQuestionValidator(int pageNum){
        this.pageNum = pageNum;

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
            case 1:
                validateNumberOfQuestions(createQuestion, errors);
                break;
            case 2:
                validateQuestionFields(createQuestion, errors);
                validateAnswerFields(createQuestion, errors);
        }
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

    private void validateQuestionFields(CreateQuestion createQuestion,
                                        Errors errors) {
        Question question = createQuestion.getQuestion();
        ValidationUtils.rejectIfEmpty(errors, "createQuestion.question.text",
                                      "required.createQuestion.question.text",
                                      "Question cannot be empty");
        ValidationUtils.rejectIfEmpty(errors, "createQuestion.question.difficultyLevel",
                                      "required.createQuestion.question.difficultyLevel",
                                      "Difficulty level should be between 1 and 5");
        if(question.getDifficultyLevel() < 1 || question.getDifficultyLevel() > 5)
            errors.rejectValue("createQuestion.question.difficultyLevel",
                               "required.createQuestion.question.difficultyLevel", null,
                               "Difficulty level should be between 1 and 5");
        ValidationUtils.rejectIfEmpty(errors, "createQuestion.question.pointCorrect",
                                      "required.createQuestion.question.pointCorrect",
                                      "Correct points should be more than 0");
        ValidationUtils.rejectIfEmpty(errors, "createQuestion.question.pointIncorrect",
                                      "required.createQuestion.question.pointIncorrect",
                                      "Incorrect points should be more than or equal to 0");
        if(question.getPointCorrect() <= 0)
            errors.rejectValue("createQuestion.question.pointCorrect",
                               "required.createQuestion.question.pointCorrect",
                               "Correct points should be more than 0");
        if(question.getPointIncorrect() < 0)
            errors.rejectValue("createQuestion.question.pointCorrect",
                               "required.createQuestion.question.pointCorrect",
                               "Correct points should be more than or equal to 0");


    }

    private void validateNumberOfQuestions(CreateQuestion target, Errors errors){
        ValidationUtils.rejectIfEmpty(errors, "createQuestion.numCorrect", "required.numCorrect",
                                      "Correct answers have to be minimum 1");
        ValidationUtils.rejectIfEmpty(errors, "createQuestion.numIncorrect", "required.numIncorrect",
                                      "Incorrect answers have to be minimum 3");
        if(target.getNumCorrect() < 1)
            errors.rejectValue("createQuestion.numCorrect", "required.numCorrect", null,
                               "Correct answers have to be minimum 1");
        if(target.getNumIncorrect() < 3)
            errors.rejectValue("createQuestion.numIncorrect", "required.numIncorrect", null,
                               "Incorrect answers have to be minimum 3");

    }
}
