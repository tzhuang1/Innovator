package com.innovator.solve;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class PastProblemsTwo extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_page);
        String testID = getIntent().getExtras().getString("TestID");
        ArrayList<String> answers = new ArrayList<>(Arrays.asList(getIntent().getExtras().getString("ANSWERS").split("\n")));
        Log.d("YAS", testID);
        MockTestManager.MockTest m = MockTestManager.getTestByID(testID);
        ArrayList<Question> qs = m.questionList;
        ReviewPage information = new ReviewPage(qs, answers);

        TextView Question = findViewById(R.id.Question);
        TextView YourAnswer = findViewById(R.id.your_answer);
        TextView Answer = findViewById(R.id.answer);
        Button button = findViewById(R.id.Next);
        Button back = findViewById(R.id.back);

        information.updateButtons(button, back);
        information.question(Question);
        information.yourAnswer(YourAnswer);
        information.answer(Answer);
        information.Click(button, back, Question, YourAnswer, Answer);
        information.back(button, back, Question, YourAnswer, Answer);
    }
    public void goBack(View v) {
        finish();
    }
}