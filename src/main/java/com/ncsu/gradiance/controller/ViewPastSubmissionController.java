package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.AttemptHomework;
import com.ncsu.gradiance.domain.Homework;
import com.ncsu.gradiance.domain.SelectedCourse;
import com.ncsu.gradiance.domain.User;
import com.ncsu.gradiance.service.HomeworkService;
import com.ncsu.gradiance.service.SubmitAttemptHomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/viewPastSubmissions")
@SessionAttributes({"user", "selectedCourse"})
public class ViewPastSubmissionController {

    private HomeworkService homeworkService;
    private SubmitAttemptHomeworkService submitAttemptHomeworkService;

    @Autowired
    public ViewPastSubmissionController(HomeworkService homeworkService,
                                        SubmitAttemptHomeworkService
                                                submitAttemptHomeworkService){
        this.homeworkService = homeworkService;
        this.submitAttemptHomeworkService = submitAttemptHomeworkService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String chooseHomework(@ModelAttribute("user") User user,
                                 @ModelAttribute("selectedCourse") SelectedCourse
                                         selectedCourse, BindingResult result,
                                 Model model){
        Homework homework = new Homework();
        AttemptHomework attemptHomework = new AttemptHomework();
        attemptHomework.setUser(user);
        attemptHomework.setHomework(homework);
        selectedCourse.setHomeworkAttempt(attemptHomework);
        return "selectHomework";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String showSubmissionSummary(@ModelAttribute("user") User user,
                                 @ModelAttribute("selectedCourse") SelectedCourse
                                         selectedCourse, BindingResult result,
                                 Model model){
        AttemptHomework attemptHomework = this.homeworkService.getAttemptHomework(
                selectedCourse.getHomework().getAttemptId(), result);
        model.addAttribute("summary",  attemptHomework);
        return "viewHomeworkSubmissionSummary";
    }

    @ModelAttribute("homeworks")
    public List<String> getHomeworks(@ModelAttribute("user") User user,
                                     @ModelAttribute("selectedCourse") SelectedCourse
                                             selectedCourse, BindingResult result){
        List<String> homeworks = new ArrayList<>();
        for(Homework homework : this.homeworkService.
                getAttemptedHomewroksSortedByDate(user, result) )
            homeworks.add(homework.toAttemptString());
        return homeworks;
    }

    @InitBinder
    public void bindCourseWithId(WebDataBinder binder){
        binder.registerCustomEditor(Homework.class,
                                    new AttemptedHomeworkTypeEditor());
    }
}
