package com.ncsu.gradiance.service;

import java.util.Date;

public class QueriesFactory {
    public QueriesFactory(){}

    public static String IS_PROF_QUERY = "select * from prof where prof_id='%s'";
    public static String IS_TA_QUERY = "select * from course_ta where course_id='%s' and ta_id='%s'";
    public static String IS_A_STUDENT_QUERY = "select * from students where student_id='%s'";
    public static String VALIDATE_USER_QUERY = "select * from users where u_id='%s'";
    public static String ADD_USER = "insert into users values('%s', '%s')";
    public static String ADD_PROF = "insert into prof values('%s', '%s')";
    public static String ADD_STUDENT = "insert into students values('%s', '%s', %s)";
    public static String PROF_TEACH_COURSE =
            "select t.course_id from teaches t where t.course_id in (select t1.course_id from teaches t1 where  t1.prof_id='%s')";
    public static String COURSE_NOT_EXPIRED = "select * from courses where %s (startDate > %s or endDate > %2$s)";
    public static String GET_COURSES_NOT_EXPIRED = "select * from courses where course_id='%s' and (startDate > '%s or endDate > %2$s)'";
    public static String GET_COURSE = "select * from courses where course_id='%s'";
    public static String ADD_TEACHES = "insert into teaches values('%s', '%s')";
    public static String ADD_ENROLLED = "insert into enrolled values(%s, %s)";
    public static String GET_TOPICS_FOR_COURSE = "select * from course_topics where course_id='%s'";
    public static String COUNT_HINTS  = "select count(*) from hints";
    public static String COUNT_QUESTION  = "select count(*) from questions";
    public static String COUNT_ANSWER  = "select count(*) from answers";
    public static String INSERT_HINT = "insert into hints values(%s, %s)";
    public static String INSERT_QUESTION = "insert into questions values(%s, %s, %s, %s, %s, %s, %s)";
    public static String INSERT_ANSWER = "insert into answers values(%s, %s, %s)";
    public static String ADD_ANSWERS_TO_QUESTION = "insert into question_answer values(%s, %s, %s)";
    public static String GET_QUESTIONS_OF_A_TOPIC =
            "select * from questions where topic_id=%s ";
    public static String GET_HINT = "select * from hints where hint_id=%s";
    public static String GET_NUM_QUESTIONS_IN_TOPIC  = "select count(*) from questions where topic_id=%s";
    public static String SCORING_SCHEMES = "select * from score_selection_scheme";
    public static String NUM_ROWS = "select count(*) from %s";
    public static String INSERT_HOMEWORK = "insert into homeworks values(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)";
    public static String GET_HOMEWORK = "select * from homeworks where course_id=%s and topic_id=%s";
    public static String GET_SCORE_SCHEME = "select * from score_selection_scheme where id=%s";
    public static String GET_TOPIC = "select * from course_topics where topic_id=%s";
    public static String GET_HOMEWORK_WITH_ID = "select * from homeworks where hw_id=%s";
    public static String UPDATE_HOMEWORK = "update homeworks set start_date=%s, end_date=%s, num_attempts=%s, score_selection_scheme=%s, correct_ans_pts=%s, incorrect_ans_pts=%s, numQuestions=%s where hw_id=%s";
    public static String NUM_HOMEWORKS_IN_TOPIC = "select count(*) from homeworks where topic_id=%s";
    public static String STUDENT_TAKE_COURSE = "select course_id from enrolled where student_id='%s'";
    public static String COURSE_TOKEN = "select token from courses where course_id=%s";
    public static String ENROLLED_IN_COURSE = "select course_id from enrolled where student_id='%s'";



    public static String getQuery(String query, String... args){
        return String.format(query,args);
    }

    public static String getDateInOracleStandard(java.sql.Date date){
        String dateStr = date.toString();
        return String.format(" to_date('%s', 'yyyy-mm-dd') ", date.toString());
    }

    public static java.sql.Date getCurrentDate(){
        return new java.sql.Date(new java.util.Date().getTime());
    }

    public static String getCurrentDateInCOracleStandard(){
        return getDateInOracleStandard(getCurrentDate());
    }

    public static String getStringValueForSql(String str){
        if(str == null)
            throw new IllegalArgumentException();
        return "'" + str + "'";
    }

    public static String getIntValueForSql(int val){
        return String.valueOf(val);
    }

    
    
}
