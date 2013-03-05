package com.ncsu.gradiance.service;

import com.ncsu.gradiance.domain.AddCourse;
import com.ncsu.gradiance.domain.Course;
import com.ncsu.gradiance.domain.Topic;
import com.ncsu.gradiance.domain.User;
import org.springframework.validation.Errors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.ncsu.gradiance.service.QueriesFactory.*;

public class CourseService {

    private DataBaseQuery dataBaseQuery;
    private Map<String, String> addCourse;

    public CourseService(DataBaseQuery dataBaseQuery){
        this.dataBaseQuery = dataBaseQuery;
        this.addCourse = new HashMap<String, String>();
        this.addCourse.put(AccessControlListImpl.Roles.PROF.roleName(),
                           PROF_TEACH_COURSE);
        this.addCourse.put(AccessControlListImpl.Roles.STUDENT.roleName(),
                           STUDENT_TAKE_COURSE);
    }

    public List<Course> canAdd(User user, Errors errors) {
        Set<Course> courses = new HashSet<Course>();
        String role = user.getRoleName();
        String query = getQuery(addCourse.get(role), user.getUid());
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        try {
            extractCourse(rs, courses, errors);
        } catch (SQLException e) {
            errors.rejectValue("", "course.error",
                               "unable to add course at this time");
        }
        return new ArrayList<Course>(courses);
    }

    public List<Course> getAffiliatedCourses(User user, Errors errors){
        AccessControlListImpl.Roles role =
                AccessControlListImpl.Roles.getRoles(user.getRoleName());
        switch(role){
            case PROF:
                return taughtBy(user, errors);
            case STUDENT:
                return enrolledIn(user, errors);
        }
        throw new IllegalArgumentException();
    }

    public List<Course> enrolledIn(User user, Errors errors){
        return getAffiliatedCourses(ENROLLED_IN_COURSE, user, errors,
                                    "student does not have a course");

    }

    public List<Course> taughtBy(User user, Errors errors){
        return getAffiliatedCourses(PROF_TEACH_COURSE, user, errors,
                             "teacher does not have a course");

    }

    private List<Course> getAffiliatedCourses(String affiliationQuery, User user, Errors errors,
                                              String errorMsg){
        String query = getQuery(affiliationQuery, user.getUid());
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        List<String> courseIds = new ArrayList<>();
        List<Course> courses = null;
        try {
            while(rs.next())
                courseIds.add(rs.getString(1));
            courses = getCourses(courseIds, errors);
        } catch (SQLException e) {
            courses = new ArrayList<>();
            errors.rejectValue("", "invalid.courses", errorMsg);
        }
        return  courses;
    }

    private List<Course> getCourses(List<String> courseIds, Errors errors)
            throws SQLException {
        List<Course> courses = new ArrayList<>();
        for(String courseId : courseIds){
            String query = getQuery(GET_COURSE, courseId);
            ResultSet courseResult = this.dataBaseQuery.executeQuery(query, errors);
            while (courseResult.next()) {
                courses.add(getCourse(courseResult));
            }

        }
        return courses;
    }

    public Course addCourse(AddCourse addCourse, Errors errors) {
        switch(AccessControlListImpl.Roles.getRoles(addCourse.getUser().getRoleName()))
        {
            case PROF:
                return addTeaches(addCourse, errors);
            case STUDENT:
                return addEnrolled(addCourse, errors);
        }
        throw new IllegalArgumentException();
    }

    public Course addEnrolled(AddCourse addCourse, Errors errors){
        if(addCourse.getToken() == null || addCourse.getToken().trim().equals("")){
            errors.rejectValue("token", "toke.required", "token is required");
            return addCourse.getCourse();
        }
        if(!addCourse.getToken().equals(getCourseToken(addCourse.getCourse(),
                                                       errors))){
            errors.rejectValue("token", "toke.required", "token does not match");
            return addCourse.getCourse();
        }
        String query = getQuery(ADD_ENROLLED, getStringValueForSql(
                addCourse.getCourse().getCourseId()),
                                getStringValueForSql(addCourse.getUser().getUid()));
        this.dataBaseQuery.executeQuery(query, errors);
        return addCourse.getCourse();
    }

    public String getCourseToken(Course course, Errors errors){
        String query = getQuery(COURSE_TOKEN, getStringValueForSql(course.
                getCourseId()));
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        String token = "";
        try {
            while (rs.next()){
                token = rs.getString(1);
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return token;
    }


    public Course addTeaches(AddCourse addCourse, Errors errors){
        this.dataBaseQuery.executeQuery(getQuery(ADD_TEACHES,
                  addCourse.getCourse().getCourseId(),
                  addCourse.getUser().getUid()), errors);
        return addCourse.getCourse();
    }

    private void extractCourse(ResultSet rs, Set<Course> courses,
                               Errors errors) throws SQLException {

        String currDate = getCurrentDateInCOracleStandard();
        ResultSet courseResult = this.dataBaseQuery.executeQuery(
                getQuery(COURSE_NOT_EXPIRED, getExcludedCourse(rs), currDate),
                errors);
        while (courseResult.next()) {
            courses.add(getCourse(courseResult));
        }
    }

    private Course getCourse(ResultSet courseResult) throws SQLException {
        Course course = new Course();
        course.setCourseId(courseResult.getString(1));
        course.setCourseName(courseResult.getString(2));
        course.setFromDate(courseResult.getDate(3));
        course.setToDate(courseResult.getDate(4));
        course.setTokenId(courseResult.getString(5));
        return course;

    }

    private String getExcludedCourse(ResultSet rs) throws SQLException {
        StringBuilder sb = new StringBuilder(" ");
        boolean first = true;
        while(rs.next()) {
            if(first){
                sb.append("(");
                first = false;
            }
            else
            sb.append(" or ");
            sb.append(" course_id<> ");
            sb.append(" '" + rs.getString(1) + "' ");
        }
        if(sb.length() > 1) sb.append(") and ");
        return sb.toString();

    }

    public List<Topic> getTopics(Course course, Errors errors){
        String query = getQuery(GET_TOPICS_FOR_COURSE, course.getCourseId());
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        List<Topic> topics = new ArrayList<>();
        try {
            while(rs.next()){
                topics.add(getTopic(rs));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topics;

    }

    private Topic getTopic(ResultSet rs) throws SQLException {
        Topic topic = new Topic();
        topic.setCourseId(rs.getString(1));
        topic.setTopicId(rs.getString(2));
        topic.setTopicName(rs.getString(3));
        return topic;
    }

    public Course getCourse(String id, Errors errors){
        String query = getQuery(GET_COURSE, id) ;
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        try {
            while(rs.next()){
            return getCourse(rs);
            }
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return null;
    }

    public Topic getTopic(String id, Errors errors){
        String query = getQuery(GET_TOPIC, getStringValueForSql(id));
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        try {
            while(rs.next())
            return getTopic(rs);
        } catch (SQLException e) {
            errors.rejectValue("", "sql.exception", e.getMessage());
        }
        return null;
    }
}
