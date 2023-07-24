package com.innovator.solve;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class AnswerFormatManager {
    private int numAnswers = 0;
    private int id;
    private HashMap<Integer, Integer> dndAnswer = new HashMap<>();
    private ArrayList<Integer> mcqAnswer = new ArrayList<>();
    private String frqAnswer = "";
    public AnswerFormatManager(int i) {
        id = i;
    }

    public void addDndAnswer(int box, int button) {
        Log.d("DNDANSWER", "" + box + " " + button);
        dndAnswer.put(box, button);
        numAnswers++;
    }

    public void clearDndAnswers() {
        dndAnswer = new HashMap<>();
        numAnswers = 0;
    }

    public void addMcqAnswer(int i) {
        mcqAnswer.add(i);
        numAnswers++;
    }

    public void clearMcqAnswer() {
        mcqAnswer = new ArrayList<>();
        numAnswers = 0;
    }

    public void addFrqAnswer(String s) {
        frqAnswer = s;
        numAnswers = 1;
    }

    public void clearFrqAnswer() {
        frqAnswer = "";
        numAnswers = 0;
    }

    public int getNumAnswers() {
        return numAnswers;
    }

    public Bundle bundleUp(Bundle b) {
        b.putInt("NUMANSWERS", numAnswers);
        if (numAnswers == 0)
            return b;
        if (id == 1) {
            int i = 0;
            for (int key: dndAnswer.keySet()) {
                b.putInt("BOX" + i, key);
                b.putInt("BUTTON" + i++, dndAnswer.get(key));
            }
        }
        if (id == 0) {
            int i = 0;
            for (int ans: mcqAnswer) {
                b.putInt("ANSWER" + i++, ans);
            }
        }
        if (id == 2) {
            b.putString("ANSWER", frqAnswer);
        }
        return b;
    }
}
