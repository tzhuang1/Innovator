package com.innovator.solve;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.innovator.solve.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ShortAnswerMainActivity extends AppCompatActivity {

    Button continueBtn;
    TextView question;
    EditText editAnswer;

    List<ShortAnswerItem> questions;
    int curQuestions = 0;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_answer_main);

        continueBtn = (Button) findViewById(R.id.continueBtn);
        question = (TextView) findViewById(R.id.question);
        editAnswer = (EditText) findViewById(R.id.editAnswer);

        continueBtn.setVisibility(View.INVISIBLE);

        questions = new ArrayList<>();
        //add questions and answers to the game
        for (int i = 0; i < ShortAnswerDatabase.questions.length; i++){
            questions.add(new ShortAnswerItem(ShortAnswerDatabase.questions[i], ShortAnswerDatabase.answers[i]));
        }
        //shuffle the questions
        Collections.shuffle(questions);

        question.setText(questions.get(curQuestions).getQuestion());

        random = new Random();

        editAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //check if the answer is correct.
                if(editAnswer.getText().toString().equalsIgnoreCase("")) {
                    continueBtn.setVisibility(View.INVISIBLE);
                }
                else{
                    continueBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curQuestions < (ShortAnswerDatabase.questions.length - 1)) {
                    //next question
                    curQuestions++;
                    question.setText(questions.get(curQuestions).getQuestion());
                    continueBtn.setVisibility(View.INVISIBLE);
                    editAnswer.setText("");
                } 
                
            }
        });
    }

}