package com.example.solve;

import android.media.Image;

import java.util.List;

public class Topic {
    private String title;
    private int masteryPct;
    private Image titleImg;
    private List<Questions> questions;

    public Topic(String title, Image titleImg, List<Questions> questionsList) {
        this.title = title;
        this.masteryPct = 0;
        this.titleImg = titleImg;
        this.questions = questionsList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMasteryPct() {
        return masteryPct;
    }

    public void setMasteryPct(int masteryPct) {
        this.masteryPct = masteryPct;
    }

    public Image getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(Image titleImg) {
        this.titleImg = titleImg;
    }

    public List<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }
}
