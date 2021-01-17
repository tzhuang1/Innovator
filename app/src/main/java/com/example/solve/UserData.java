package com.example.solve;

import java.util.ArrayList;

public class UserData {
    private String id;
    private String email;
    private String displayName;
    private ArrayList<AnsweredQuestionData> answeredQuestionsList;
    private int grade;
    private int activities;


    public UserData( String id, String email, String name) {
        this.id = id;
        this.email = email;
        displayName = name;
        answeredQuestionsList = new ArrayList<AnsweredQuestionData>();


    }
    public UserData( String id, String email, String name, ArrayList<AnsweredQuestionData> list) {
        this.id = id;
        this.email = email;
        displayName = name;
        answeredQuestionsList = list;


    }
    public int getGrade()
    {
        return grade;
    }
    public void setGrade(int g)
    {
        grade = g;
    }
    public int getActivities()
    {
        return activities;
    }
    public void setActivities(int a)
    {
        activities = a;
    }

    public String getId()
    {
       return id;
    }
    public String getEmail()
    {
        return email;
    }
    public String getDisplayName()
    {
        return displayName;
    }
    public ArrayList<AnsweredQuestionData> getListOfQuestions()
    {
        return answeredQuestionsList;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setDisplayName(String name)
    {
        displayName = name;
    }
    public void setList(ArrayList<AnsweredQuestionData> list)
    {
        answeredQuestionsList = list;
    }
    public void addAnsweredQuestions(Question question, String answer)
    {
        answeredQuestionsList.add(new AnsweredQuestionData(question, answer));
    }
    public void deleteAnsweredQuestions(Question question)
    {
        if(answeredQuestionsList.contains(question))
        {
            answeredQuestionsList.remove(question);
        }
    }
    public int getMissedAddSub()
    {
        int count = 0;
        for(AnsweredQuestionData aqd : answeredQuestionsList)
        {
            if((aqd.getQuestion().getCategory().equalsIgnoreCase("addition and subtraction")) && !aqd.getIsAnswerCorrect());
            {
                count++;
            }
        }
        return count;
    }
    public int getMissedCompEst()
    {
        int count = 0;
        for(AnsweredQuestionData aqd : answeredQuestionsList)
        {
            if((aqd.getQuestion().getCategory().equalsIgnoreCase("computation and estimation")) && !aqd.getIsAnswerCorrect());
            {
                count++;
            }
        }
        return count;
    }
    public int getMissedMeasGeo()
    {
        int count = 0;
        for(AnsweredQuestionData aqd : answeredQuestionsList)
        {
            if(((aqd.getQuestion().getCategory().equalsIgnoreCase("Measurement and Geometry")||aqd.getQuestion().getCategory().equalsIgnoreCase("Measurement") )||aqd.getQuestion().getCategory().equalsIgnoreCase("Geometry")) && !aqd.getIsAnswerCorrect());
            {
                count++;
            }
        }
        return count;
    }
    public int getMissedPatFuncAlg()
    {
        int count = 0;
        for(AnsweredQuestionData aqd : answeredQuestionsList)
        {
            if((aqd.getQuestion().getCategory().equalsIgnoreCase("Patterns, Functions, and Algebra")) && !aqd.getIsAnswerCorrect());
            {
                count++;
            }
        }
        return count;
    }
    public int getMissedProbStat()
    {
        int count = 0;
        for(AnsweredQuestionData aqd : answeredQuestionsList)
        {
            if((aqd.getQuestion().getCategory().equalsIgnoreCase("probability and statistics")) && !aqd.getIsAnswerCorrect());
            {
                count++;
            }
        }
        return count;
    }
    public int getMissedNumSense()
    {
        int count = 0;
        for(AnsweredQuestionData aqd : answeredQuestionsList)
        {
            if((aqd.getQuestion().getCategory().equalsIgnoreCase("Numbers and Number Sense")) && !aqd.getIsAnswerCorrect());
            {
                count++;
            }
        }
        return count;
    }
    /*

    public String getQuestion() {
        return question;
    }

    public String getOptA() {
        return opta;
    }

    public String getOptB() {
        return optb;
    }

    public String getOptC() {
        return optc;
    }

    public String getOptD() {
        return optd;
    }

    public String getAnswer() {
        return answer;
    }

    public String getExplanation() { return explanation; }

    public String getCategory() { return category; }

    public void setId(int i) {
        id = i;
    }

    public void setQuestion(String q1) {
        question = q1;
    }

    public void setOptA(String o1) {
        opta = o1;
    }

    public void setOptB(String o2) {
        optb = o2;
    }

    public void setOptC(String o3) {
        optc = o3;
    }

    public void setOptD(String o4) {
        optd = o4;
    }

    public void setAnswer(String ans) {
        answer = ans;
    }

    public void setExplanation(String ex) {
        explanation = ex;
    }

    public void setCategory(String ca) {
        category = ca;
    }

     */
}
