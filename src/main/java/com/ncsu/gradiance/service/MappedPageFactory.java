package com.ncsu.gradiance.service;

import java.util.HashMap;
import java.util.Map;

public class MappedPageFactory {
    private Map<String, String> profMappedPages;
    private Map<String, String> studentMappedPages;

    public MappedPageFactory(){
        profMappedPages = new HashMap<String, String>();
        profMappedPages.put("home", "profHome");
        profMappedPages.put("selectCourse", "profSelectCourse");
        profMappedPages.put("afterCourseSelected", "profAfterCourseSelected");
        profMappedPages.put("addCourse", "profAddCourse");
        profMappedPages.put("courseAdded", "profCourseAdded");
        profMappedPages.put("addCourseFailure", "addCourseFailure");

        studentMappedPages = new HashMap<>();
        studentMappedPages.put("home", "profHome");
        studentMappedPages.put("addCourse", "studentAddCourse");
        studentMappedPages.put("courseAdded", "profCourseAdded");
        studentMappedPages.put("addCourseFailure", "addCourseFailure");
        studentMappedPages.put("selectCourse", "profSelectCourse");
        studentMappedPages.put("afterCourseSelected", "studentAfterCourseSelected");

    }

    public Map<String, String> getMappedPages(AccessControlListImpl.Roles role){
        switch (role){
            case PROF:
                return this.profMappedPages;
            case STUDENT:
                return this.studentMappedPages;
        }
        throw new IllegalArgumentException("invalid role");
    }
}
