package com.innovator.solve;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

@IgnoreExtraProperties
public class Question {
    private int id;
    private String question;
    private String optA, optB, optC, optD;
    private int optAPicNumber = -1, optBPicNumber = -1, optCPicNumber = -1, optDPicNumber = -1;

    private String answer;
    private String explanation;
    private String category;
    private int explanationPicNumber;
    private String passage;
    private int questionPicNumber;
    private int questionNumber;

    //ADVANCED SETTINGS - MOCK SPECIFIC
    public boolean mock = false; //is this question a mock question (advanced format)
    private String questionType; //drag drop, mcq, etc.
    //Multiple-Choice-X
    //Multiple-Select-X
    //Drag-Drop-X
    //Short-Answer
    private boolean calcActive = true;
    private boolean formulaActive = true;


    private TreeMap<Character, String> choices; //for questions with more than 4 choices, use this


    public Question(boolean mock, String questionType, String question, TreeMap<Character, String> choices, String answer, String explanation, String category, int picNumber, int exPicNumber) {
        if (!mock) {
            //SHOULD ALWAYS BE TRUE
            return;
        }
        Log.d("YAS", picNumber + " " + questionType + " " + question);
        this.mock = mock;
        this.questionType = questionType;
        this.question = question;
        this.answer = answer;
        this.explanation = explanation;
        this.category = category;
        this.questionPicNumber = picNumber;
        this.explanationPicNumber = exPicNumber;

        this.choices = choices;

    }

    public Question(String q, String oa, String ob, String oc, String od, String ans, String ex, String ca) {
        question = q;
        optA = oa;
        optB = ob;
        optC = oc;
        optD = od;
        answer = ans;
        explanation = ex;
        category = ca;
        questionPicNumber = -1;
        explanationPicNumber = -1;
    }
    public Question(String question, String opta, String optb, String optc, String optd, String answer, String explanation, String category, int picNumber, int exPicNumber) {
        this.question = question;
        this.optA = opta;
        this.optB = optb;
        this.optC = optc;
        this.optD = optd;
        this.answer = answer;
        this.explanation = explanation;
        this.category = category;
        this.questionPicNumber = picNumber;
        this.explanationPicNumber = exPicNumber;
    }

    public Question(String question, String opta, String optb, String optc, String optd, String answer, String explanation, String category, int picNumber, int exPicNumber, String pass) {
        this.question = question;
        this.optA = opta;
        this.optB = optb;
        this.optC = optc;
        this.optD = optd;
        this.answer = answer;
        this.explanation = explanation;
        this.category = category;
        this.questionPicNumber = picNumber;
        this.explanationPicNumber = exPicNumber;
        passage = pass;
    }

    public Question() {
        id = 0;
        question = "";
        optA = "";
        optB = "";
        optC = "";
        optD = "";
        answer = "";
        explanation = "";
        category = "";
        questionPicNumber = -1;
        explanationPicNumber = -1;

    }

    public int getPicNumber() {
        return questionPicNumber;
    }

    public int getExPicNumber() {return explanationPicNumber;}

    public int getOptAPicNumber() {
        return optAPicNumber;
    }

    public int getOptBPicNumber() {
        return optBPicNumber;
    }

    public int getOptCPicNumber() {
        return optCPicNumber;
    }

    public int getOptDPicNumber() {
        return optDPicNumber;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptA() {
        return optA;
    }

    public String getOptB() {
        return optB;
    }

    public String getOptC() {
        return optC;
    }

    public String getOptD() {
        return optD;
    }

    public String getAnswer() {
        return answer;
    }

    public String getExplanation() { return explanation; }

    public String getCategory() { return category; }

    public int getId() { return id; }

    public String getPassage() { return passage; }

    public void setId(int i) {
        id = i;
    }

    public void setQuestion(String q1) {
        question = q1;
    }

    public void setOptA(String o1) {
        optA = o1;
    }

    public void setOptB(String o2) {
        optB = o2;
    }

    public void setOptC(String o3) {
        optC = o3;
    }

    public void setOptD(String o4) {
        optD = o4;
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
    public void setQuestionPicNumber(int pic) {
        questionPicNumber = pic;
    }
    public void setExPicNumber(int expic) {
        explanationPicNumber = expic;
    }

    public void setQuestionNumber(int i){
        questionNumber=i;
    }

    public String getQuestionType() { return this.questionType; }

    public TreeMap<Character, String> getChoices() { return this.choices; }

    public boolean isCalcActive() { return this.calcActive; }
    public boolean isFormulaActive() { return this.formulaActive; }
}