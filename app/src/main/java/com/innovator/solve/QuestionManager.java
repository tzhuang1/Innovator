package com.innovator.solve;

import android.content.Intent;
import android.os.Bundle;

public class QuestionManager {
    public QuestionManager() {

    }

    public static Bundle compileData(Bundle i, Question q) {
        i.putString("TEXT", q.getQuestion());
        i.putString("OPTA", q.getOptA());
        i.putString("OPTB", q.getOptB());
        i.putString("OPTC", q.getOptC());
        i.putString("OPTD", q.getOptD());
        i.putInt("ID", q.getId());
        return i;
    }

    public static Intent compileData(Intent i, Question q) {
        i.putExtra("TEXT", q.getQuestion());
        i.putExtra("OPTA", q.getOptA());
        i.putExtra("OPTB", q.getOptB());
        i.putExtra("OPTC", q.getOptC());
        i.putExtra("OPTD", q.getOptD());
        i.putExtra("ID", q.getId());
        return i;
    }

    public static Question decompileData(Bundle i) {
        Question q = new Question();
        try {
            q.setQuestion((String) i.getString("TEXT"));
            q.setOptA((String) i.getString("OPTA"));
            q.setOptB((String) i.getString("OPTB"));
            q.setOptC((String) i.getString("OPTC"));
            q.setOptD((String) i.getString("OPTD"));
            q.setId((Integer) i.getInt("ID"));
        } catch (Exception e) {

        }
        return q;
    }

    public static Question decompileData(Intent i) {
        Question q = new Question();
        try {
            q.setQuestion((String) i.getExtras().get("TEXT"));
            q.setOptA((String) i.getExtras().get("OPTA"));
            q.setOptB((String) i.getExtras().get("OPTB"));
            q.setOptC((String) i.getExtras().get("OPTC"));
            q.setOptD((String) i.getExtras().get("OPTD"));
            q.setId((Integer) i.getExtras().get("ID"));
        } catch (Exception e) {

        }
        return q;
    }
}
