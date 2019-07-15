package com.example.innovatorsetup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

/*
TODO
 - Finish implementing fragments for central interaction (Grade select & activity select)
    - Switch out fragments when next is pressed
    - Implement back button
 - Implement Persisting data for activities/day & grade
    - Store in local file for now (internal storage or SharedPreferences)
    - Store in database after login is implemented(?)
 - Other tasks(unimportant)
    - Customize buttons
        - Better buttons than just + and -, may need to wait until full app is developed to ensure design stays consistent
    - Animate screen change
 Useful links:
 File storage overview: https://developer.android.com/guide/topics/data/data-storage
 SharedPreferences: https://developer.android.com/training/data-storage/shared-preferences.html
 Fragments:
 - Fragments in java: https://abhiandroid.com/ui/fragment
 - Buttons in fragments: https://stackoverflow.com/questions/18711433/button-listener-for-button-in-fragment-in-android
 - Fragment communication: https://developer.android.com/training/basics/fragments/communicating.html
 - Fragment general:
    - https://developer.android.com/guide/components/fragments
    - https://guides.codepath.com/android/creating-and-using-fragments
 */

public class MainActivity extends AppCompatActivity implements SetupGradeSelectFragment.onGradeSelectFragmentInteraction, SetupActivitySelectFragment.OnActivitySelectFragmentListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressBar setupProgressBar = findViewById(R.id.setupProgressBar);
        final TextView setupTxt = findViewById(R.id.setupTxt);

        setupProgressBar.setProgress(0);

        Button gradeSelectNextBtn = findViewById(R.id.setupGradeNextBtn);
        gradeSelectNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupProgressBar.setProgress(setupProgressBar.getProgress() + 1);
            }
        });
    }

    @Override
    public void onGradeSelectFragmentInteraction(int defaultGrade) {

    }

    @Override
    public void OnActivitySelectFragmentListener(int defaultActivityNum) {

    }
}
