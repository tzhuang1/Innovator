package com.example.solve;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.hoang8f.widget.FButton;

public class ReadingQuestions extends AppCompatActivity {

    View loadingScreen;

    Typeface tb;

    View questionPicLayout;
    TextView questionPicText;
    ImageView questionPic;
    TextView questionText;

    View textAnswersLayout;
    View picAnswersLayout;
    FButton buttonA, buttonB, buttonC, buttonD;
    Button btnA, btnB, btnC, btnD;

    ImageView explanationPic;

    Question currentQuestion;
    UserData currentUser;
    Topic currentTopic;
    List<Question> questionsList;
    List<AnsweredQuestionData> answeredQuestionList;
    int qid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_questions);
    }
}