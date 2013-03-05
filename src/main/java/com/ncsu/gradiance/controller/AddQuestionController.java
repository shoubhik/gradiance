package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.*;
import com.ncsu.gradiance.service.CourseService;
import com.ncsu.gradiance.service.DataBaseQuery;
import com.ncsu.gradiance.service.QuestionService;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/addQuestion")
@SessionAttributes({"user", "selectedCourse"})
public class AddQuestionController {

    private CourseService courseService;
    private QuestionService questionService;
    private DataBaseQuery dbQuery;
    @Autowired
    public AddQuestionController(DataBaseQuery dbQuery, CourseService
            courseService, QuestionService questionService){
        this.dbQuery= dbQuery;
        this.questionService = questionService;
        this.courseService = courseService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showWizard(@ModelAttribute("selectedCourse") SelectedCourse
                                         selectedCourse){
        CreateQuestion createQuestion = new CreateQuestion();
        Topic topic = new Topic();
        createQuestion.setTopic(topic);
        createQuestion.setNumCorrect(0);
        createQuestion.setNumIncorrect(0);
        createQuestion.setQuestion(new Question());
        createQuestion.setNewQuestion(true);
        selectedCourse.setCreateQuestion(createQuestion);
        return "selectTopic";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("selectedCourse") SelectedCourse selectedCourse,
                             BindingResult result,
                             @RequestParam("_page") int currentPage,
                             HttpServletRequest request){
        Map<Integer, String> pageForms = new HashMap<>();
        pageForms.put(0,"selectTopic");
        pageForms.put(1,"numAnswers");
        pageForms.put(2,"submitQuestions");
        if(request.getParameter("_cancel") != null){
            selectedCourse.getCreateQuestion().setTopic(new Topic());
            return pageForms.get(0);
        }
        else if(request.getParameter("_finish") != null){
            // handle finish
            CreateQuestion createQuestion = selectedCourse.getCreateQuestion();
            createQuestion.getQuestion().setAnswers(extractAnswersAndHints(
                    request, createQuestion));
            new AddQuestionValidator(currentPage).validate(
                    selectedCourse.getCreateQuestion(), result);
            if(result.hasErrors())
                return  pageForms.get(currentPage);

            this.questionService.createQuestion(selectedCourse.
                    getCreateQuestion().getQuestion(), result);
            if(result.hasErrors())
                return  pageForms.get(currentPage);
            return "questionAddedSummary";
        }
        else{
            int targetPage = WebUtils
                    .getTargetPage(request, "_target", currentPage);
            if(targetPage < currentPage){
                return pageForms.get(targetPage);
            }
            // before going to next page validate input
            new AddQuestionValidator(currentPage).validate(
                    selectedCourse.getCreateQuestion(), result);
            if(result.hasErrors())
                return  pageForms.get(currentPage);
            preFillModelForNextPage(selectedCourse.getCreateQuestion(),
                                    targetPage);
            return pageForms.get(targetPage);
        }
    }

    private List<Answer> extractAnswersAndHints(HttpServletRequest request,
                                        CreateQuestion createQuestion){
        List<Answer> answers = new ArrayList<>();
        for(int i = 1; i <= createQuestion.getNumCorrect(); i++){
            String ansText = request.getParameter("correct_ans"+i);
            String hintText = request.getParameter("correct_hint"+i);
            answers.add(createAnswer(ansText, hintText, 1));
        }
        for(int i = 1; i <= createQuestion.getNumIncorrect(); i++){
            String ansText = request.getParameter("incorrect_ans"+i);
            String hintText = request.getParameter("incorrect_hint"+i);
            answers.add(createAnswer(ansText, hintText, 0));
        }
        return answers;
    }

    private Answer createAnswer(String ansText, String hintText, int correct){
        Hint hint = new Hint();
        hint.setText(hintText.trim());
        Answer answer = new Answer();
        answer.setText(ansText.trim());
        answer.setCorrect(correct);
        answer.setHint(hint);
        return answer;
    }

    private void preFillModelForNextPage(CreateQuestion createQuestion, int nextPage){
        switch (nextPage){
            case 2:
                Hint hint = new Hint();
                Question question = new Question();
                question.setHint(hint);
                question.setTopicId(createQuestion.getTopic().getTopicId());
                createQuestion.setQuestion(question);
                break;
        }
    }


    @ModelAttribute("topics")
    public List<String> getTopics(@ModelAttribute("selectedCourse") SelectedCourse
                                              selectedCourse, BindingResult result){
        List<String> topics = new ArrayList<>();
        for(Topic topic : this.courseService.getTopics(selectedCourse.getCourse(), result))
            topics.add(topic.toString());
        return topics;
    }

    @InitBinder
    public void bindCourseWithId(WebDataBinder binder){
        binder.registerCustomEditor(Topic.class, new TopicTypeEditor());
    }
}
