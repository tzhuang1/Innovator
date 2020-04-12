package com.example.solve;

import java.util.List;

public class TopicSelect {
    private String title;
    private int masteryPct;
    private int imageDrawable;
    private List<Question> questions;

    public TopicSelect(String title, int imageDrawable, List<Question> questionsList) {
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
