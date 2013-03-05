package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.Course;
import com.ncsu.gradiance.domain.SelectedCourse;
import com.ncsu.gradiance.domain.User;
import com.ncsu.gradiance.service.CourseService;
import com.ncsu.gradiance.service.DataBaseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/selectCourse")
@SessionAttributes({"user", "selectedCourse"})
public class SelectCourseController {

    private CourseService courseService;
    private DataBaseQuery dbQuery;
    @Autowired
    public SelectCourseController(DataBaseQuery dbQuery, CourseService courseService){
        this.dbQuery = dbQuery;
        this.courseService = courseService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@ModelAttribute("user") User user, Model model){
        model.addAttribute("selectedCourse", new SelectedCourse());
        return user.getMappedPages().get("selectCourse");
    }

    @RequestMapping(method = RequestMethod.POST)
    public String showSelectedCourseOptions(@ModelAttribute("user") User user,
              @ModelAttribute("selectedCourse") SelectedCourse selectedCourse){
        return user.getMappedPages().get("afterCourseSelected");

    }

    @InitBinder
    public void bindCourseWithId(WebDataBinder binder){
        binder.registerCustomEditor(Course.class, new CourseTypeEditor(
                this.dbQuery));

    }

    @ModelAttribute("course")
    public List<String> getRoles(@ModelAttribute("user") User user,
                                 BindingResult result){
        List<String> courses = new ArrayList<>();
        for(Course course :this.courseService.getAffiliatedCourses(user, result))
            courses.add(course.toString());
        return courses;
    }

}
