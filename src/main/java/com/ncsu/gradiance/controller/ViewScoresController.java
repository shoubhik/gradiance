package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.Homework;
import com.ncsu.gradiance.domain.SelectedCourse;
import com.ncsu.gradiance.domain.User;
import com.ncsu.gradiance.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@RequestMapping("/viewScores")
@SessionAttributes({"user", "selectedCourse"})
public class ViewScoresController {

    private HomeworkService homeworkService;
    @Autowired
    public ViewScoresController(HomeworkService homeworkService){
        this.homeworkService = homeworkService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showScores(@ModelAttribute("user") User user,
                             @ModelAttribute("selectedCourse") SelectedCourse
                                     selectedCourse, BindingResult result,
                             Model model){
        List<Homework> homeworks = this.homeworkService.
                getAttemptedHomewroksSortedByDate(user, selectedCourse.getCourse(),
                                                  result);
        model.addAttribute("homeworks", homeworks);
        return "viewScores";

    }
}
