package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.*;
import com.ncsu.gradiance.service.CourseService;
import com.ncsu.gradiance.service.DataBaseQuery;
import com.ncsu.gradiance.service.HomeworkService;
import com.ncsu.gradiance.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/addHomework")
@SessionAttributes({"user", "selectedCourse"})
public class AddHomeWork {

    private CourseService courseService;
    private QuestionService questionService;
    private HomeworkService homeworkService;

    @Autowired
    public AddHomeWork( CourseService courseService,
                        QuestionService questionService, HomeworkService
            homeworkService){
        this.questionService = questionService;
        this.courseService = courseService;
        this.homeworkService = homeworkService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showWizard(@ModelAttribute("selectedCourse") SelectedCourse
                                     selectedCourse, BindingResult result){
        Homework homework = new Homework();
        homework.setTopic(new Topic());
        homework.setCourse(selectedCourse.getCourse());
        homework.setNewHomework(true);
        selectedCourse.setHomework(homework);
        return "selectHomeworkTopic";
    }

    @ModelAttribute("topics")
    public List<String> getTopics(@ModelAttribute("selectedCourse") SelectedCourse
                                          selectedCourse, BindingResult result){
        List<String> topics = new ArrayList<>();
        for(Topic topic : this.courseService.getTopics(selectedCourse.getCourse(), result))
            topics.add(topic.toString());
        return topics;
    }

    @ModelAttribute("schemes")
    public List<String> getScoringSchemes(@ModelAttribute("selectedCourse") SelectedCourse
                                                      selectedCourse, BindingResult result){
        List<String> schemes = new ArrayList<>();
        for(ScoreSelectionScheme scoreSelectionScheme : this.homeworkService.getSchemes(result))
            schemes.add(scoreSelectionScheme.toString());
        return schemes;
    }


    @RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("selectedCourse") SelectedCourse selectedCourse,
                             BindingResult result,
                             @RequestParam("_page") int currentPage,
                             HttpServletRequest request){
        Map<Integer, String> pageForms = new HashMap<>();
        pageForms.put(0,"selectHomeworkTopic");
        pageForms.put(1,"submitHomework");
        if(request.getParameter("_cancel") != null){
            selectedCourse.getCreateQuestion().setTopic(new Topic());
            return pageForms.get(0);
        }
        else if(request.getParameter("_finish") != null){
            // handle finish
            new CreateHomeworkValidator(currentPage, this.questionService).
                    validate(selectedCourse.getHomework(), result);
            if(result.hasErrors())
                return  pageForms.get(currentPage);
            this.homeworkService.createHomework(selectedCourse.getHomework(), result);
            if(result.hasErrors())
                return  pageForms.get(currentPage);
            selectedCourse.setHomework(new Homework());
            return "homeworkCreated";
        }
        else{
            int targetPage = WebUtils
                    .getTargetPage(request, "_target", currentPage);
            if(targetPage < currentPage){
                return pageForms.get(targetPage);
            }
            new CreateHomeworkValidator(currentPage, this.questionService).
                    validate(selectedCourse.getHomework(), result);
            if(result.hasErrors())
                return  pageForms.get(currentPage);
            return pageForms.get(targetPage);
        }
    }

    @InitBinder
    public void bindCourseWithId(WebDataBinder binder){
        binder.registerCustomEditor(Topic.class, new TopicTypeEditor());
        binder.registerCustomEditor(java.sql.Date.class, new SqlDateEditor());
        binder.registerCustomEditor(ScoreSelectionScheme.class, new
                ScoringSchemeTypeEditor());
    }
}
