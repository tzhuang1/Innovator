package com.innovator.solve;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewPage {
    int page = 0;
    ArrayList<Question> qs;
    ArrayList<String> yourAns;
    int numQs;

    public ReviewPage(ArrayList<Question> q, ArrayList<String> ans) {
        qs = q;
        numQs = qs.size();
        yourAns = ans;
    }

    public void question(TextView x){
        String a = Integer.toString(page+1);
        x.setText("Question" + " " + "#" + a + ":" + " " + qs.get(page).getQuestion());
    }

    public void yourAnswer(TextView x){
        x.setText("Your Answer: " + yourAns.get(page) + ": " + getOption(qs.get(page), yourAns.get(page)));
    }

    public void answer (TextView x){
        x.setText("Correct Answer" + ":" + " " + qs.get(page).getAnswer() + "\n" + qs.get(page).getExplanation());
    }

    public String getOption(Question q, String opt) {
        if (opt.equalsIgnoreCase("Option A")) return q.getOptA();
        if (opt.equalsIgnoreCase("Option B")) return q.getOptB();
        if (opt.equalsIgnoreCase("Option C")) return q.getOptC();
        if (opt.equalsIgnoreCase("Option D")) return q.getOptD();
        return "";
    }
    public void updateButtons(Button forward, Button back) {
        if (page == 0)
            back.setVisibility(View.GONE);
        else
            back.setVisibility(View.VISIBLE);
        if (page >= numQs - 1)
            forward.setVisibility(View.GONE);
        else
            forward.setVisibility(View.VISIBLE);
    }
    public void Click (Button forward, Button back, TextView question, TextView yourans, TextView answer){
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page += 1;
                updateButtons(forward, back);
                question(question);
                yourAnswer(yourans);
                answer(answer);
            }
        });
    }
    public void back (Button forward, Button back, TextView question, TextView yourans, TextView answer){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page -= 1;
                updateButtons(forward, back);
                question(question);
                yourAnswer(yourans);
                answer(answer);
            }
        });
    }
}