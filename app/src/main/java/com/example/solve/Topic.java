package com.example.solve;

import android.graphics.drawable.Drawable;
import android.media.Image;

import java.util.List;

public class Topic {
    private String title;
    private int masteryPct;
    private int imageDrawable;
    private List<Questions> questions;

    public Topic(String title, int imageDrawable, List<Questions> questionsList) {
        this.title = title;
        this.masteryPct = 0;
        this.imageDrawable = imageDrawable;
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

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    public List<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }
}
