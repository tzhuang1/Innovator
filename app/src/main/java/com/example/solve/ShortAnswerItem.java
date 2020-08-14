package com.example.solve;

public class ShortAnswerItem {
    private String question, answer;

    public ShortAnswerItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }
}
