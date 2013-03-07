package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.*;
import com.ncsu.gradiance.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/attemptHomework")
@SessionAttributes({"user", "selectedCourse"})
public class AttemptHomeworkController {

    private HomeworkService homeworkService;
    private QuestionService questionService;
    private SubmitAttemptHomeworkService submitAttemptHomeworkService;

    @Autowired
    public AttemptHomeworkController(HomeworkService
            homeworkService, QuestionService questionService,
                                     SubmitAttemptHomeworkService
                                             submitAttemptHomeworkService){
        this.questionService = questionService;
        this.homeworkService = homeworkService;
        this.submitAttemptHomeworkService = submitAttemptHomeworkService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showHomeworks(@ModelAttribute("user") User user,
                                @ModelAttribute("selectedCourse") SelectedCourse
                                     selectedCourse, BindingResult result,
                                Model model){
        Homework homework = new Homework();
        homework.setTopic(new Topic());
        homework.setCourse(selectedCourse.getCourse());
        homework.setNewHomework(false);
        selectedCourse.setHomework(homework);
        selectedCourse.setEditHomework(false);
        selectedCourse.setAttemptHomework(true);
        AttemptHomework attemptHomework = new AttemptHomework();
        attemptHomework.setUser(user);
        selectedCourse.setHomeworkAttempt(attemptHomework);
        return "selectHomework";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String showForm(@ModelAttribute("user") User user,
                           @ModelAttribute("selectedCourse") SelectedCourse
                           selectedCourse,
                           BindingResult result,
                           @RequestParam("_page") int currentPage,
                           Model model, HttpServletRequest request){
        Map<Integer, String> pageForms = new HashMap<>();
        pageForms.put(1,"selectHomework");
        pageForms.put(2,"submitHomeworkAttempt");
        if(request.getParameter("_cancel") != null){
            return pageForms.get(0);
        }
        else if(request.getParameter("_finish") != null){
            // handle finish
            this.submitAttemptHomeworkService.saveAttemptedHomework(
                    selectedCourse.getHomeworkAttempt(),user, result, request);
            model.addAttribute("summary",  selectedCourse.getHomeworkAttempt());
            return "viewHomeworkSubmissionSummary";
        }
        else{
            int targetPage = WebUtils
                    .getTargetPage(request, "_target", currentPage);
            if(targetPage == 2){
                AttemptHomework attemptHomework =
                        selectedCourse.getHomeworkAttempt();
                attemptHomework.setHomework(selectedCourse.getHomework());
                this.homeworkService.fillAttemptHomework(attemptHomework,
                                                         result);
            }
            return pageForms.get(targetPage);
        }

    }

    @ModelAttribute("homeworks")
    public List<String> getHomeworks(@ModelAttribute("user") User user,
                                     @ModelAttribute("selectedCourse") SelectedCourse
                                             selectedCourse, BindingResult result){
        List<String> homeworks = new ArrayList<>();
        for(Homework homework : this.homeworkService.
                getAttemptableHomeworks(selectedCourse.getCourse(), user,
                                        result) )
            homeworks.add(homework.toStringWithEndDate());
        return homeworks;
    }

    @InitBinder
    public void bindCourseWithId(WebDataBinder binder){
        binder.registerCustomEditor(Homework.class, new HomeworkTypeEditor(
                this.homeworkService));
    }


}
