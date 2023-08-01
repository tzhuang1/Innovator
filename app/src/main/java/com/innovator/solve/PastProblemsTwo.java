package com.innovator.solve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PastProblemsTwo extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_page);

        ReviewPage information = new ReviewPage();

        TextView Question = findViewById(R.id.Question);
        TextView Answer = findViewById(R.id.Answer);
        Button button = findViewById(R.id.Next);
        Button back = findViewById(R.id.back);

        information.question(Question);
        information.answer(Answer);
        information.Click(button, Question, Answer);
        information.back(back, Question, Answer);
    }
    public void goBack(View v){
        startActivity(new Intent(this, CongratsPage.class));
    }
}