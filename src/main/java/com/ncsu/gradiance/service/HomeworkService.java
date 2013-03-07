package com.ncsu.gradiance.service;

import com.ncsu.gradiance.domain.*;
import org.springframework.validation.Errors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.ncsu.gradiance.service.QueriesFactory.*;

/**
 * User: shoubhik Date: 4/3/13 Time: 9:56 PM
 */
public class HomeworkService extends  BaseService{

    private CourseService courseService;
    private QuestionService questionService;

    public HomeworkService(DataBaseQuery dataBaseQuery) {
        super(dataBaseQuery);
    }

    public List<ScoreSelectionScheme> getSchemes(Errors errors){
        ResultSet rs = this.dataBaseQuery.executeQuery(SCORING_SCHEMES, errors);
        List<ScoreSelectionScheme> scoreSelectionSchemes = new ArrayList<>();
        try {
            while(rs.next()){
                ScoreSelectionScheme scoreSelectionScheme = new
                        ScoreSelectionScheme();
                scoreSelectionScheme.setSchemeId(rs.getInt(1));
                scoreSelectionScheme.setName(rs.getString(2));
                scoreSelectionSchemes.add(scoreSelectionScheme);
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return scoreSelectionSchemes;
    }

    public void createHomework(Homework homework, Errors errors){
        try {
            String homeWorkId = getIntValueForSql(
                    getNextRowCount("homeworks", errors, ""));
            String courseId = getStringValueForSql(homework.getCourse().
                    getCourseId());
            String selectionScheme = getIntValueForSql(homework.
                    getScoreSelectionScheme().getSchemeId());
            String name = getStringValueForSql(homework.getName());
            String startDate = getDateInOracleStandard(homework.getStartDate());
            String endDate = getDateInOracleStandard(homework.getEndDate());
            String numAttempts = getIntValueForSql(homework.getNumAttempts());
            String correctPts = getIntValueForSql(homework.getCorrectPts());
            String incorrectPts = getIntValueForSql(homework.getIncorrectPts());
            String topicId = getStringValueForSql(homework.getTopic().getTopicId());
            String numQuestions = getIntValueForSql(homework.getNumQuestions());
            String query = getQuery(INSERT_HOMEWORK, homeWorkId, courseId,
                                    selectionScheme, name, startDate, endDate,
                                    numAttempts, correctPts, incorrectPts,
                                    topicId, numQuestions);
            this.dataBaseQuery.executeQuery(query, errors, "");


        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }

    }

    public List<Homework> getHomework(Course course, Topic topic, Errors errors){
        String query = getQuery(GET_HOMEWORK, getStringValueForSql(
                course.getCourseId()), getStringValueForSql(topic.getTopicId()));
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors, "");
        List<Homework> homeworks = new ArrayList<>();
        try {
            while(rs.next()){
                Homework homework = new Homework();
                homework.setCourse(course);
                homework.setTopic(topic);
                homework.setId(rs.getInt(1));
                homework.setScoreSelectionScheme(getScheme(rs.getInt(3), errors));
                homework.setName(rs.getString(4));
                homework.setStartDate(rs.getDate(5));
                homework.setEndDate(rs.getDate(6));
                homework.setNumAttempts(rs.getInt(7));
                homework.setCorrectPts(rs.getInt(8));
                homework.setIncorrectPts(rs.getInt(9));
                homework.setNumQuestions(rs.getInt(11));
                homeworks.add(homework);
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return homeworks;
    }

    public List<Homework> getAttemptableHomeworks(Course course, User user,
                                                  Errors errors){
        List<Homework> homeworks = getActiveHomeworks(course, errors);
        ListIterator<Homework> it = homeworks.listIterator();
        while(it.hasNext()){
            Homework homework = it.next();
            if(getNumberOfAttempts(homework, user, errors) >= homework.getNumAttempts())
                it.remove();
        }
        return homeworks;
    }

    public int getNumberOfAttempts(Homework homework, User user, Errors errors){
        String query = getQuery(GET_NUM_HOMEWORK_ATTEMPTS,
                                getIntValueForSql(homework.getId()),
                                getStringValueForSql(user.getUid()));
        int attempts = 0;
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        try {
            while(rs.next()){
                attempts = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use
            // File | Settings | File Templates.
        }
        return  attempts;
    }

    public List<Homework> getActiveHomeworks(Course course, Errors errors){
        String query = getQuery(GET_ACTIVE_HOMEWORKS, getStringValueForSql(
                course.getCourseId()), getCurrentDateInCOracleStandard());
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors, "");
        List<Homework> homeworks = new ArrayList<>();
        try {
            while(rs.next()){
                homeworks.add(getHomework(rs, errors)) ;

            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return homeworks;
    }



    public Homework getHomework(int hwId, Errors errors){
        String query = getQuery(GET_HOMEWORK_WITH_ID, getIntValueForSql(hwId));
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors, "");
        Homework homework = null;
        try {
            while(rs.next()){
                homework = getHomework(rs, errors);
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return homework;
    }

    private Homework getHomework(ResultSet rs, Errors errors) throws SQLException {
        Homework homework = new Homework();
        homework.setId(rs.getInt(1));
        homework.setCourse(
                this.courseService.getCourse(rs.getString(2), errors));
        homework.setTopic(
                this.courseService.getTopic(rs.getString(10), errors));
        homework.setScoreSelectionScheme(getScheme(rs.getInt(3), errors));
        homework.setName(rs.getString(4));
        homework.setStartDate(rs.getDate(5));
        homework.setEndDate(rs.getDate(6));
        homework.setNumAttempts(rs.getInt(7));
        homework.setCorrectPts(rs.getInt(8));
        homework.setIncorrectPts(rs.getInt(9));
        homework.setNumQuestions(rs.getInt(11));
        return homework;
    }

    public ScoreSelectionScheme getScheme(int id, Errors errors){
        String query = getQuery(GET_SCORE_SCHEME, getIntValueForSql(id));
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors, "");
        ScoreSelectionScheme scoreSelectionScheme = new ScoreSelectionScheme();
        try {
            while(rs.next()){
                scoreSelectionScheme.setName(rs.getString(2));
                scoreSelectionScheme.setSchemeId(rs.getInt(1));
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return scoreSelectionScheme;
    }

    public CourseService getCourseService() {
        return courseService;
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public void updateHomework(Homework homework, Errors errors){
        String startDate = getDateInOracleStandard(homework.getStartDate());
        String endDate =  getDateInOracleStandard(homework.getEndDate());
        String numAttempts = getIntValueForSql(homework.getNumAttempts());
        String scheme = getIntValueForSql(homework.getScoreSelectionScheme().
                getSchemeId());
        String correctPts = getIntValueForSql(homework.getCorrectPts());
        String incorrectPts = getIntValueForSql(homework.getIncorrectPts());
        String numQuestion = getIntValueForSql(homework.getNumQuestions());
        String hwId = getIntValueForSql(homework.getId());
        String query = getQuery(UPDATE_HOMEWORK, startDate, endDate, numAttempts,
                                scheme, correctPts, incorrectPts, numQuestion, hwId);
        this.dataBaseQuery.executeQuery(query, errors);
    }

    public AttemptHomework getAttemptHomework(int attemptId, Errors errors){
        AttemptHomework attemptHomework = new AttemptHomework();
        attemptHomework.setId(attemptId);
        List<Question> questions = getQuestionsForHomeworkAttempt(attemptId,
                                                                  errors);
        attemptHomework.setQuestions(questions);
        String query = getQuery(GET_ATTEMPT_SCORE, getIntValueForSql(attemptId));
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        try {
            while(rs.next()){
                attemptHomework.setScore(rs.getInt(1));

            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return attemptHomework;
    }

    public void fillAttemptHomework(AttemptHomework attemptHomework, Errors
            errors){
        try {
            ChooseRandomQuestionAndAnswers  chooseRandomQuestionAndAnswers =
                    new ChooseRandomQuestionAndAnswers(attemptHomework, this.dataBaseQuery,
                                               this.questionService, errors);
            // if there is a pending homework return that
            int attemptId = isHomeworkPending(attemptHomework.getHomework(),
                                              attemptHomework.getUser(), errors);
            if(attemptId != 0){
                List<Question> questions = getQuestionsForHomeworkAttempt(attemptId,
                                                                          errors);
                attemptHomework.setQuestions(questions);
                attemptHomework.setId(attemptId);
                return;
            }
            chooseRandomQuestionAndAnswers.choose();
            int attempQuesId = getNextRowCount("attempt", errors, "");
            String query;
            for(Question question : attemptHomework.getQuestions()){
                int attemptAnsId = getNextRowCount("attempt_ans", errors, "");
                for(Answer answer : question.getAnswers()) {
                    query = getQuery(ADD_ATTEMPT_ANS, getIntValueForSql(attemptAnsId),
                                     getIntValueForSql(answer.getId()));
                    this.dataBaseQuery.executeQuery(query, errors);
                }
                query = getQuery(ADD_ATTEMPT, getIntValueForSql(attempQuesId),
                                 getIntValueForSql(question.getId()),
                                 getIntValueForSql(attemptAnsId),
                                 "null", "null");
                this.dataBaseQuery.executeQuery(query, errors);
            }
            query = getQuery(ADD_HW_STUDENT, getIntValueForSql(
                    attemptHomework.getHomework().getId()),
                    getStringValueForSql(attemptHomework.getUser().getUid()),
                    getIntValueForSql(attempQuesId), "null",
                    "null");
            attemptHomework.setId(attempQuesId);
            this.dataBaseQuery.executeQuery(query, errors);

        } catch (SQLException e) {
            errors.rejectValue("" , "sql.exception", e.getMessage());
        }
    }

    public int isHomeworkPending(Homework homework, User user, Errors errors){
        String query = getQuery(GET_PENDING_HOMEWORK, getIntValueForSql(homework.getId()),
                                getStringValueForSql(user.getUid()));
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        try {
            while(rs.next()){
                return rs.getInt(1);

            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return 0;
    }

    public List<Question> getQuestionsForHomeworkAttempt(int attemptId,
                                                         Errors errors){
        String query = getQuery(GET_ATTEMPT_QUESTIONS, getIntValueForSql(attemptId));
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        List<Question> questions = new ArrayList<>();
        try {
            while(rs.next()){
                int qId = rs.getInt(1);
                int attemptAnsId = rs.getInt(2);
                Question question = this.questionService.getQuestion(qId, errors);
                question.setAnswers(getAnswersForHomeworkAttempt(attemptAnsId, errors));
                int responseId = rs.getInt(3);
                if(!rs.wasNull()){
                    question.setResponse(this.questionService.getAnswer(
                            responseId, errors));
                }
                questions.add(question);
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return questions;
    }

    public List<Answer> getAnswersForHomeworkAttempt(int attemptAnsId,
                                                     Errors errors){
        String query = getQuery(GET_ATTEMPT_HOMEWORK_ANS, getIntValueForSql(attemptAnsId));
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        List<Answer> answers = new ArrayList<>();
        try {
            while (rs.next()){
                int ansId = rs.getInt(1);
                answers.add(this.questionService.getAnswer(ansId, errors));
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return answers;
    }

    public QuestionService getQuestionService() {
        return questionService;
    }

    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public void updateAttempt(int attemptId, int qId, String responseId,
                              String responseExp, Errors errors){
        String query = getQuery(UPDATE_ATTEMPT, responseId, responseExp,
                                getIntValueForSql(attemptId), getIntValueForSql(qId));
        this.dataBaseQuery.executeQuery(query, errors);


    }

    public void updateHwStudent(int hwId, String studentId, int attemptId, int score,
                                Errors errors){
        String query = getQuery(UPDATE_HW_STUDENT,
                                getCurrentDateInCOracleStandard(),
                                getIntValueForSql(score), getIntValueForSql(hwId),
                                getStringValueForSql(studentId), getIntValueForSql(attemptId));
        this.dataBaseQuery.executeQuery(query, errors);
    }

    public List<Homework> getAttemptedHomewroksSortedByDate(User user,Course course,
                                                            Errors errors){
        String query = getQuery(GET_STUDENT_PAST_ATTEMPT,
                                getStringValueForSql(course.getCourseId()),
                                getStringValueForSql(user.getUid()));
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        List<Homework> homeworks = new ArrayList<>();
        int prevId = -1;
        int count  = 0;
        try {
            while (rs.next()) {
                Homework homework = this.getHomework(rs.getInt(1), errors);
                if(prevId != homework.getId()){
                    count = 1;
                }
                prevId = homework.getId();
                homework.setAttemptCount(count);
                count++;
                homework.setAttemptId(rs.getInt(3));
                homework.setScore(rs.getInt(5));
                homeworks.add(homework);

            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return homeworks;


    }
}
