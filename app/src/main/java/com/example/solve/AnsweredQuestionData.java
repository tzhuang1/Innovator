package com.example.solve;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AnsweredQuestionData {
    private Question question;
    private String userAnswer;
    private boolean isAnswerCorrect;

    public AnsweredQuestionData(Question question, String answer)
    {
        this.question = question;
        this.userAnswer = answer;
        this.isAnswerCorrect = (question.getAnswer().equals(answer) || ("Option "+question.getAnswer()).equals(answer));
    }
    public Question getQuestion()
    {
        return question;
    }
    public String getAnswer()
    {
        return userAnswer;
    }

    public void setQuestion(Question question)
    {
        this.question = question;
    }
    public void setAnswer(String answer)
    {
        this.userAnswer = answer;
    }
    public boolean getIsAnswerCorrect()
    {
        return question.getAnswer().equals(userAnswer);
    }

}
