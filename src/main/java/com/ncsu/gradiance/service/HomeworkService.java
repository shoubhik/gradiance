package com.ncsu.gradiance.service;

import com.ncsu.gradiance.domain.Course;
import com.ncsu.gradiance.domain.Homework;
import com.ncsu.gradiance.domain.ScoreSelectionScheme;
import com.ncsu.gradiance.domain.Topic;
import org.springframework.validation.Errors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ncsu.gradiance.service.QueriesFactory.*;

/**
 * User: shoubhik Date: 4/3/13 Time: 9:56 PM
 */
public class HomeworkService extends  BaseService{

    private CourseService courseService;

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



    public Homework getHomework(int hwId, Errors errors){
        String query = getQuery(GET_HOMEWORK_WITH_ID, getIntValueForSql(hwId));
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors, "");
        Homework homework = new Homework();
        try {
            while(rs.next()){
                homework.setId(rs.getInt(1));
                homework.setCourse(this.courseService.getCourse(rs.getString(2), errors));
                homework.setTopic(this.courseService.getTopic(rs.getString(10), errors));
                homework.setScoreSelectionScheme(getScheme(rs.getInt(3), errors));
                homework.setName(rs.getString(4));
                homework.setStartDate(rs.getDate(5));
                homework.setEndDate(rs.getDate(6));
                homework.setNumAttempts(rs.getInt(7));
                homework.setCorrectPts(rs.getInt(8));
                homework.setIncorrectPts(rs.getInt(9));
                homework.setNumQuestions(rs.getInt(11));
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
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
}
