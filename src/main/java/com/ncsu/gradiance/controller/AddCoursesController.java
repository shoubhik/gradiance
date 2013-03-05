package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.AddCourse;
import com.ncsu.gradiance.domain.Course;
import com.ncsu.gradiance.domain.User;
import com.ncsu.gradiance.service.CourseService;
import com.ncsu.gradiance.service.DataBaseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/addCourse")
@SessionAttributes("user")
public class AddCoursesController {

    private CourseService courseService;
    private DataBaseQuery dbQuery;
    @Autowired
    public AddCoursesController(CourseService courseService,
                                DataBaseQuery dbQuery){
        this.courseService = courseService;
        this.dbQuery = dbQuery;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@ModelAttribute("user") User user, Model model){
        model.addAttribute("addCourse", new AddCourse());
        return user.getMappedPages().get("addCourse");
    }


    @RequestMapping(method = RequestMethod.POST)
    public String addCourse(@ModelAttribute("user" ) User user,
                            @ModelAttribute("addCourse" ) AddCourse addCourse,
                            BindingResult result, Model model){
        // user info not proogated to UI so cannot rebuild.
        // pick up from session. its a hack but not worth the time
        addCourse.setUser(user);
        Course course = this.courseService.addCourse(addCourse, result);
        model.addAttribute("course", course);
        if(result.hasErrors())
            return addCourse.getUser().getMappedPages().get("addCourseFailure");
        return addCourse.getUser().getMappedPages().get("courseAdded");
    }

    @ModelAttribute("course")
    public List<String> getRoles(@ModelAttribute("user") User user,
                                 BindingResult result){
        List<String> courses = new ArrayList<>();
         for(Course course :this.courseService.canAdd(user, result))
             courses.add(course.toString());
        return courses;
    }

    @InitBinder
    public void bindCourseWithId(WebDataBinder binder){
        binder.registerCustomEditor(Course.class, new CourseTypeEditor(
                this.dbQuery));

    }
}
