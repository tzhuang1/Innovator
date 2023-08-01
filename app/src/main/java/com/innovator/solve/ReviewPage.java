package com.innovator.solve;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReviewPage {
    static int i = 0;
    static int numberquestion = 1;
    static String[] question = {"MATH QUESTION: WHAT IS 1+1!!!!!! OMGGGGG YAYAYAYAYAYAYYAY","What is 2+2"};
    static String[] answer = {"Well, 1+1 is equal to 2. You know why? Well, it is because if Jeff and Bob put 1 cookie each on a table, then in total, there will be 2 cookies. So, 1+1 = 2", "What the hell"};

    public void question(TextView x){
        String a = Integer.toString(numberquestion);
        x.setText("Question" + " " + "#" + a + ":" + " " + question[i]);
    }

    public void answer (TextView x){
        x.setText("Answer" + ":" + " " + answer[i]);
    }

    public void Click (Button x, TextView question, TextView answer){
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i += 1;
                numberquestion += 1;
                question(question);
                answer(answer);
            }
        });
    }
    public void back (Button x, TextView question, TextView answer){
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i -= 1;
                numberquestion -= 1;
                question(question);
                answer(answer);
            }
        });
    }
}