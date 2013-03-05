package com.ncsu.gradiance.service;

import com.ncsu.gradiance.domain.Answer;
import com.ncsu.gradiance.domain.Hint;
import com.ncsu.gradiance.domain.Question;
import com.ncsu.gradiance.domain.Topic;
import org.springframework.validation.Errors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ncsu.gradiance.service.QueriesFactory.*;

public class QuestionService extends BaseService{

    public QuestionService(DataBaseQuery dataBaseQuery) {
        super(dataBaseQuery);
    }

    public void saveQuestion(Question question, Errors errors){
        if(question.getHint().getText().trim().length() != 0){
            saveHint(question.getHint(), errors);
            if(!errors.hasErrors())
                question.setHasHint(true);
        }
        if(!errors.hasErrors()){
            try {
                int questionId = getQuestionId(errors);
                question.setId(questionId);
                String qId = getIntValueForSql(questionId);
                String topicId = getStringValueForSql(question.getTopicId());
                String qText = getStringValueForSql(question.getText());
                String diffLevel = getIntValueForSql(question.getDifficultyLevel());
                String ptIncorrect = getIntValueForSql(question.getPointIncorrect());
                String ptCorrect = getIntValueForSql(question.getPointCorrect());
                Hint hint = question.getHint();
                String h_id = question.isHasHint() ?
                        getIntValueForSql(hint.getId()) : String.valueOf(null);
                String query = getQuery(INSERT_QUESTION, qId, topicId, qText,
                                        diffLevel, ptIncorrect, ptCorrect, h_id);
                this.dataBaseQuery.executeQuery(query, errors);

            } catch (SQLException e) {
                errors.rejectValue("", "sql.exception", e.getMessage());
            }

        }
    }

    public void saveAnswers(List<Answer> answers, Errors errors) {
        try {

            for (Answer answer : answers) {
                Hint hint = answer.getHint();
                saveHint(hint, errors);
                if (!errors.hasErrors()) {
                    int ansId = getAnsId(errors);
                    answer.setId(ansId);
                    String query =
                            getQuery(INSERT_ANSWER, getIntValueForSql(ansId),
                                     getStringValueForSql(answer.getText()),
                                     getIntValueForSql(hint.getId()));
                    this.dataBaseQuery.executeQuery(query, errors, "");
                }
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
    }

    public void addAnswersToQuestions(Question question, Errors errors){
        List<Answer> answers = question.getAnswers();
        String qId = getIntValueForSql(question.getId());
        for(Answer answer : answers){
            String ansId  = getIntValueForSql(answer.getId());
            String correct = getIntValueForSql(answer.getCorrect());
            String query = getQuery(ADD_ANSWERS_TO_QUESTION,qId,ansId, correct);
            this.dataBaseQuery.executeQuery(query, errors, "");
        }
    }

    public void createQuestion(Question question, Errors errors){
        this.saveQuestion(question, errors);
        this.saveAnswers(question.getAnswers(), errors);
        this.addAnswersToQuestions(question, errors);
    }

    private int getAnsId(Errors errors) throws SQLException {
        return getNextRowCount("answers", errors, "");
    }

    public void saveHint(Hint hint, Errors errors){
        saveHint(hint, errors, "createQuestion.question.hint.text");
    }

    public void saveHint(Hint hint, Errors errors, String field){
        try {
            int id  = getHintId(errors);
            String query = getQuery(INSERT_HINT, getIntValueForSql(id),
                                    getStringValueForSql(hint.getText()));
            this.dataBaseQuery.executeQuery(query, errors);
            hint.setId(id);
        } catch (SQLException e) {
            errors.rejectValue(field, "sql.exception", e.getMessage());
        }
    }

    public List<Question> getQuestions(Topic topic, Errors errors){
        String query = getQuery(GET_QUESTIONS_OF_A_TOPIC,
                                getStringValueForSql(topic.getTopicId()));
        ResultSet resultSet = this.dataBaseQuery.executeQuery(query, errors);
        List<Question> questions = new ArrayList<>();
        try {
            while(resultSet.next()){
                Question question = new Question();
                question.setId(resultSet.getInt(1));
                question.setTopicId(resultSet.getString(2));
                question.setText(resultSet.getString(3));
                question.setDifficultyLevel(resultSet.getInt(4));
                question.setPointIncorrect(resultSet.getInt(5));
                question.setPointCorrect(resultSet.getInt(6));
                int hintId = resultSet.getInt(7);
                if(hintId > 0 ){
                    String hintQuery = getQuery(GET_HINT, getIntValueForSql(hintId));
                    ResultSet hintResultSet = this.dataBaseQuery.
                            executeQuery(hintQuery, errors);
                    while(hintResultSet.next()) {
                        Hint hint = new Hint();
                        hint.setId(hintResultSet.getInt(1));
                        hint.setText(hintResultSet.getString(2));
                        question.setHint(hint);
                    }

                }
                questions.add(question);
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return questions;
    }

    private int getHintId(Errors errors) throws SQLException {
        return getNextRowCount("hints", errors, "createQuestion.question.hint.text");
    }

    private int getQuestionId(Errors errors) throws SQLException {
        return getNextRowCount("questions", errors, "");

    }

    public int getNumberOfQuestionsInTheTopic(Topic topic, Errors errors){
        String query = getQuery(GET_NUM_QUESTIONS_IN_TOPIC,
                                getStringValueForSql(topic.getTopicId()));
        int count = 0;
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        try {
            while(rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return count;
    }

    public int getNumberOfHomeworks(Topic topic, Errors errors){
        String query = getQuery(NUM_HOMEWORKS_IN_TOPIC,
                                getStringValueForSql(topic.getTopicId()));
        int count = 0;
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        try {
            while(rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return count;

    }
}
