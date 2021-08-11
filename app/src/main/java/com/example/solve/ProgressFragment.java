package com.example.solve;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class ProgressFragment extends Fragment {
//
private int questionsCompletedToday;
    private int questionsCorrectToday = 0;
    protected int questionsGoal;
    private int goalMetPercentage;
    private int dailyAccuracyPercentage = 100;
    //weekly stats
    private int questionsCompletedWeekly;
    private int questionsCorrectWeekly = 0;
    private int weeklyAccuracyPercentage = 100;
    //overall stats
    private int questionsCompletedOverall;
    private int questionsCorrectOverall = 0;
    private int overallAccuracyPercentage = 100;

    //UIs
    private TextView questionsCompletedTodayCount;
    private TextView goalMetDisplay;
    private TextView dailyAccuracyDisplay;
    private TextView questionsCompletedWeeklyCount;
    private TextView weeklyAccuracyDisplay;
    private TextView questionsCompletedOverallCount;
    private TextView overallAccuracyDisplay;



    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.progress_fragment, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //UIs initialization
        questionsCompletedTodayCount = view.findViewById(R.id.questionsCompletedTodayCount);
        goalMetDisplay = view.findViewById(R.id.goalMetDisplay);
        dailyAccuracyDisplay = view.findViewById(R.id.dailyAccuracyDisplay);

        questionsCompletedWeeklyCount = view.findViewById(R.id.questionsCompletedWeeklyCount);
        weeklyAccuracyDisplay = view.findViewById(R.id.questionsCompletedWeeklyCount);

        questionsCompletedOverallCount = view.findViewById(R.id.questionsCompletedOverallCount);
        overallAccuracyDisplay = view.findViewById(R.id.overallAccuracyDisplay);

        super.onViewCreated(view, savedInstanceState);
    }


    /*  adds questions to the statistics, and updates values depending on if it was correct or not.
     *   Correct is list of correctness of all questions entered, each element must be either
     *   "CORRECT" or "INCORRECT"    */
    public void addQuestions(ArrayList<String> questions) {
        int numQuestions = questions.size();
        //updates completed questions
        questionsCompletedToday += numQuestions;
        questionsCompletedWeekly += numQuestions;
        questionsCompletedOverall += numQuestions;

        questionsCompletedTodayCount.setText(questionsCompletedToday);
        questionsCompletedWeeklyCount.setText(questionsCompletedWeekly);
        questionsCompletedOverallCount.setText(questionsCompletedWeekly);

        //calculates and updates the number of correct questions
        int numCorrect = 0;
        for(int i=0; i < numQuestions; i++) {
            if(questions.get(i).equals("CORRECT")) {
                numCorrect ++;
            }
        }
        questionsCorrectToday += numCorrect;
        questionsCorrectWeekly += numCorrect;
        questionsCorrectOverall += numCorrect;

        //updates the percentages
        goalMetPercentage = (int)(100 * questionsCorrectToday/questionsGoal);
        dailyAccuracyPercentage = (int)(100 * questionsCorrectToday/questionsCompletedToday);
        weeklyAccuracyPercentage = (int)(100 * questionsCorrectWeekly/questionsCompletedWeekly);

        goalMetDisplay.setText(goalMetPercentage + "%");
        dailyAccuracyDisplay.setText(dailyAccuracyPercentage + "%");
        weeklyAccuracyDisplay.setText(weeklyAccuracyPercentage + "%");
    }

}
