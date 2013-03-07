package com.ncsu.gradiance.service;

import com.ncsu.gradiance.domain.Answer;
import com.ncsu.gradiance.domain.AttemptHomework;
import com.ncsu.gradiance.domain.Question;
import org.springframework.validation.Errors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.ncsu.gradiance.service.QueriesFactory.*;

public class ChooseRandomQuestionAndAnswers {

    private AttemptHomework attemptHomework;
    private DataBaseQuery dataBaseQuery;
    private QuestionService questionService;
    private Errors errors;
    private static int NUM_CORRECT = 1;
    private static int NUM_INCORRECT = 3;

    public ChooseRandomQuestionAndAnswers(AttemptHomework attemptHomework,
                                          DataBaseQuery dataBaseQuery,
                                          QuestionService questionService,
                                          Errors errors){
        this.attemptHomework = attemptHomework;
        this.dataBaseQuery = dataBaseQuery;
        this.questionService = questionService;
        this.errors = errors;
    }

    public AttemptHomework choose() throws SQLException {
        int randomSeed = getSeed();
        this.attemptHomework.setQuestions(getRandomQuestions(randomSeed));
        return this.attemptHomework;
    }



    private int getSeed() throws SQLException {
        String query =  getQuery(GET_RANDOM_SEED, getStringValueForSql(
                attemptHomework.getUser().getUid()));
        ResultSet rs = this.dataBaseQuery.executeQuery(query, errors);
        int seed = 0;
        while(rs.next()){
            seed = rs.getInt(1);
        }
        return seed;
    }

    private List<Question> getRandomQuestions(int seed){
        List<Question> questions =
                this.questionService.getQuestions(this.attemptHomework.
                getHomework().getTopic(), this.errors);
        List<Question> randomQuestions = new ArrayList<>();
        Random random = new Random(seed);
        while(randomQuestions.size() != this.attemptHomework.
                getHomework().getNumQuestions()){
            int rand = Math.abs(random.nextInt()%questions.size());
            Question question = questions.get(rand);
            if(!randomQuestions.contains(question)){
                question.setAnswers(getRandomAnswers(question, random));
                randomQuestions.add(question);
            }
        }
        return randomQuestions;

    }

    private List<Answer> getRandomAnswers(Question q, Random random){
        List<Answer> allAnswers = this.questionService.getAnswers(q, this.errors);
        List<Answer> selectedAnswers = new ArrayList<>();
        while(selectedAnswers.size() != NUM_CORRECT){
            int rand = Math.abs(random.nextInt() % allAnswers.size());
            Answer answer = allAnswers.get(rand);
            if(!selectedAnswers.contains(answer) && answer.isCorrect())
                selectedAnswers.add(answer);
        }

        while(selectedAnswers.size() != NUM_CORRECT + NUM_INCORRECT){
            int rand = Math.abs(random.nextInt() % allAnswers.size());
            Answer answer = allAnswers.get(rand);
            if(!selectedAnswers.contains(answer) && !answer.isCorrect())
                selectedAnswers.add(answer);

        }
        return selectedAnswers;
    }


}
