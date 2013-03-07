package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.*;
import com.ncsu.gradiance.service.CourseService;
import com.ncsu.gradiance.service.HomeworkService;
import com.ncsu.gradiance.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/editHomework")
@SessionAttributes({"user", "selectedCourse"})
public class EditHomeworkController {

    private CourseService courseService;
    private QuestionService questionService;
    private HomeworkService homeworkService;

    @Autowired
    public EditHomeworkController( CourseService courseService,
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
        homework.setNewHomework(false);
        selectedCourse.setHomework(homework);
        selectedCourse.setEditHomework(true);
        selectedCourse.setAttemptHomework(false);
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

    @ModelAttribute("homeworks")
    public List<String> getHomeworks(@ModelAttribute("selectedCourse") SelectedCourse
                                                  selectedCourse, BindingResult result){
        if(selectedCourse.getHomework() == null) return Collections
                .singletonList("");
        List<String> homeworks = new ArrayList<>();
        for(Homework homework : this.homeworkService.
                getHomework(selectedCourse.getCourse(),
                            selectedCourse.getHomework().getTopic(), result) )
            homeworks.add(homework.toString());
        return homeworks;
    }

    @InitBinder
    public void bindCourseWithId(WebDataBinder binder){
        binder.registerCustomEditor(Topic.class, new TopicTypeEditor());
        binder.registerCustomEditor(java.sql.Date.class, new SqlDateEditor());
        binder.registerCustomEditor(Homework.class, new HomeworkTypeEditor(
                this.homeworkService));
        binder.registerCustomEditor(ScoreSelectionScheme.class, new
                ScoringSchemeTypeEditor());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("selectedCourse") SelectedCourse selectedCourse,
                             BindingResult result,
                             @RequestParam("_page") int currentPage,
                             HttpServletRequest request){
        Map<Integer, String> pageForms = new HashMap<>();
        pageForms.put(0,"selectHomeworkTopic");
        pageForms.put(1,"selectHomework");
        pageForms.put(2,"submitHomework");
        if(request.getParameter("_cancel") != null){
            selectedCourse.getCreateQuestion().setTopic(new Topic());
            return pageForms.get(0);
        }
        else if(request.getParameter("_finish") != null){
            // handle finish
            new EditHomeworkValidator(currentPage, this.questionService).
                    validate(selectedCourse.getHomework(), result);
            if(result.hasErrors())
                return  pageForms.get(currentPage);
            this.homeworkService.updateHomework(selectedCourse.getHomework(),
                                                result);
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
            // before going to next page validate input
            new EditHomeworkValidator(currentPage, this.questionService).
                    validate(selectedCourse.getHomework(), result);
            if(result.hasErrors())
                return  pageForms.get(currentPage);
            return pageForms.get(targetPage);
        }
    }
}
