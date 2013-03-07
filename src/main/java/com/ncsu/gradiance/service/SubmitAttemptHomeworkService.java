package com.ncsu.gradiance.service;

import com.ncsu.gradiance.domain.*;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

/**
 * User: shoubhik Date: 6/3/13 Time: 9:01 PM
 */
public class SubmitAttemptHomeworkService {

    private HomeworkService homeworkService;

    public SubmitAttemptHomeworkService(HomeworkService homeworkService){
        this.homeworkService = homeworkService;
    }

    public void saveAttemptedHomework(AttemptHomework attemptHomework, User user,
                                      Errors errors, HttpServletRequest request){
        int score = 0;
        Homework homework = attemptHomework.getHomework();
        int correctScore = homework.getCorrectPts();
        int incorrectScore = homework.getIncorrectPts();
        int attemptId = attemptHomework.getId();
        for (Question question : attemptHomework.getQuestions()){
            String responseId = request.getParameter(question.getId()+"");
            if(Utils.isEmpty(responseId)) {
                score += (-1 * incorrectScore);
                responseId = "null";
                Hint hint = new Hint();
                hint.setText("");
                Answer response = new Answer();
                response.setText("Question was not attempted");
                response.setHint(hint);
                question.setResponse(response);
            } else {
                int ansId = Integer.parseInt(responseId);
                score += question.isResponseCorrect(ansId) ? correctScore :
                        (-1 * incorrectScore);
                Answer response = this.homeworkService.getQuestionService().
                        getAnswer(ansId, errors);
                question.setResponse(response);
            }
            String exp = request.getParameter(question.getId() + "-Exp");
            if(Utils.isEmpty(exp))
                exp = "null";
            else
                exp = QueriesFactory.getStringValueForSql(exp);
            this.homeworkService.updateAttempt(attemptId, question.getId(),
                                               responseId, exp, errors);


        }
        this.homeworkService.updateHwStudent(homework.getId(), user.getUid(),
                                             attemptId, score, errors);
        attemptHomework.setScore(score);
    }

}
