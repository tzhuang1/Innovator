package com.innovator.solve;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AnswerFormatManager {
    private int numAnswers = 0;
    private int id;
    private HashMap<Integer, Integer> dndAnswer = new HashMap<>();
    public ArrayList<Integer> mcqAnswer = new ArrayList<>();
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

    public void clearMcqAnswers() {
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

    public int evaluateAnswer(Question q, int qId) {
        if (qId == 0)
            return evalMcq(q);
        if (qId == 1)
            return evalDnd(q);
        if (qId == 2)
            return evalFrq(q);
        return 0;
    }

    public int evalMcq(Question q) {
        Log.d("YAS", mcqAnswer.toString() + " " + q.getAnswer());
        if (mcqAnswer.size() != q.getAnswer().split(" ").length)
            return 0;
        for (String s: q.getAnswer().split(" ")) {
            if (!mcqAnswer.contains((int)(s.charAt(0))-65))
                return 0;
        }
        return 1;
    }

    public int evalDnd(Question q) {
        return 1;
    }

    public int evalFrq(Question q) {
        return 1;
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
        b.putInt("ID", id);
        return b;
    }

    public SharedPreferences.Editor bundleUp(SharedPreferences.Editor b, int qNo) {
        b.putInt(qNo + " NUMANSWERS", numAnswers);
        if (numAnswers == 0)
            return b;
        if (id == 1) {
            int i = 0;
            for (int key: dndAnswer.keySet()) {
                b.putInt(qNo + " BOX" + i, key);
                b.putInt(qNo + " BUTTON" + i++, dndAnswer.get(key));
            }
        }
        if (id == 0) {
            int i = 0;
            for (int ans: mcqAnswer) {
                b.putInt(qNo + " ANSWER" + i++, ans);
            }
        }
        if (id == 2) {
            b.putString(qNo + " ANSWER", frqAnswer);
        }
        b.putInt(qNo + " ANSWERID", id);
        b.apply();
        return b;
    }

    public static AnswerFormatManager unBundle(SharedPreferences sp, int qNo, int optID) {
        AnswerFormatManager afm = new AnswerFormatManager(sp.getInt(qNo + " ANSWERID", optID));
        if (afm.id == -1)
            afm.id = optID;
        if (sp.getInt(qNo + " NUMANSWERS", 0) == 0)
            return afm;
        if (afm.id == 1) {
            for (int i = 0; i < sp.getInt(qNo + " NUMANSWERS", 0); i++) {
                afm.addDndAnswer(sp.getInt(qNo + " BOX" + i, -1), sp.getInt(qNo + " BUTTON" + i, -1));
            }
        }
        if (afm.id == 0) {
            for (int i = 0; i < sp.getInt(qNo + " NUMANSWERS", 0); i++) {
                afm.addMcqAnswer(sp.getInt(qNo + " ANSWER" + i, -1));
            }
        }
        if (afm.id == 2) {
            afm.addFrqAnswer(sp.getString(qNo + " ANSWER", ""));
        }
        return afm;
    }
}
