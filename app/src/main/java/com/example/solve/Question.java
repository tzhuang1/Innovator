package com.example.solve;
import android.app.Activity;
import android.media.Image;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Question {
    private int id;
    private String question;
    private String opta, optb, optc, optd;
    private int optAPicNumber = -1, optBPicNumber = -1, optCPicNumber = -1, optDPicNumber = -1;

    private String answer;
    private String explanation;
    private String category;
    private int exPicNumber;

    private int picNumber;


    public Question(String q, String oa, String ob, String oc, String od, String ans, String ex, String ca) {
        question = q;
        opta = oa;
        optb = ob;
        optc = oc;
        optd = od;
        answer = ans;
        explanation = ex;
        category = ca;
        picNumber = -1;
        exPicNumber = -1;
    }

    public Question(String question, String opta, String optb, String optc, String optd, String answer, String explanation, String category, int picNumber, int exPicNumber) {
        this.question = question;
        this.opta = opta;
        this.optb = optb;
        this.optc = optc;
        this.optd = optd;
        this.answer = answer;
        this.explanation = explanation;
        this.category = category;
        this.picNumber = picNumber;
        this.exPicNumber = exPicNumber;
    }



    public Question() {
        id = 0;
        question = "";
        opta = "";
        optb = "";
        optc = "";
        optd = "";
        answer = "";
        explanation = "";
        category = "";
        picNumber = -1;
        exPicNumber = -1;

    }

    public int getPicNumber() {
        return picNumber;
    }

    public int getExPicNumber() {return exPicNumber;}

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

    public int getId() { return id; }

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

    public void setExPicNumber(int expic) {
        exPicNumber = expic;
    }
}
