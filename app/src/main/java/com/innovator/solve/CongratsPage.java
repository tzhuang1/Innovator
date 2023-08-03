package com.innovator.solve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CongratsPage extends AppCompatActivity {
    int numSections;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congrats_page);
        ScoreOfMockTest Test = new ScoreOfMockTest();
        ScoreOfMockTest.refitNumSections();
        numSections = ScoreOfMockTest.numSections;
        TextView MockTest = findViewById(R.id.id_score);
        TextView Score1 = findViewById(R.id.Section1Score);
        TextView Score2 = findViewById(R.id.Section2Score);;
        if (numSections <= 1)
            findViewById(R.id.Section2).setVisibility(View.GONE);
        TextView Score3 = findViewById(R.id.Section3Score);
        if (numSections <= 2)
            findViewById(R.id.Section3).setVisibility(View.GONE);


        String what = Test.Interesting();
        MockTest.setText(what);

        String Scoring1 = Test.SectionScores1();
        Score1.setText(Scoring1);
        if (numSections > 1) {
            String Scoring2 = Test.SectionScores2();
            Score2.setText(Scoring2);
        }
        if (numSections > 2) {
            String Scoring3 = Test.SectionScores3();
            Score3.setText(Scoring3);
        }
    }
    public void returnToHome(View v){
        startActivity(new Intent(this, MainMenuController.class));
    }
    public void goToReviewPage(View v){
        Intent i = new Intent(this, PastProblemsTwo.class);
        i.putExtra("ANSWERS", getIntent().getExtras().getString("ANSWERS"));
        i.putExtra("TestID", getIntent().getExtras().getString("TestID"));
        startActivity(i);
    }

}