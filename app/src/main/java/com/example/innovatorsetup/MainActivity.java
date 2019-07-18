package com.example.innovatorsetup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    int currSetupPage;

    int gradeSelect;
    int activitySelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button gradeSelectNextBtn = findViewById(R.id.setupGradeNextBtn);

        final FragmentManager fragMan = getSupportFragmentManager();
        final FragmentTransaction fragTran = fragMan.beginTransaction();

        gradeSelect = 1;
        activitySelect = 1;

        if(findViewById(R.id.setupFragmentFrameLayout) != null) {
            if(savedInstanceState != null) {
                return;
            }
        }
        SetupGradeSelectFragment gradeFragment = SetupGradeSelectFragment.newInstance(1);
        gradeFragment.setArguments(getIntent().getExtras());
        fragTran.add(R.id.setupFragmentFrameLayout, gradeFragment);

        currSetupPage = 0;

        gradeSelectNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressBar setupProgressBar = findViewById(R.id.setupProgressBar);
                TextView setupTxt = findViewById(R.id.setupTxt);
                currSetupPage++;
                switch(currSetupPage) {
                    case 0:
                        fragTran.replace(R.id.setupFragmentFrameLayout, SetupGradeSelectFragment.newInstance(gradeSelect));
                        setupTxt.setText(getString(R.string.setup_grade_select_text));
                        fragTran.commit();
                        break;
                    case 1:
                        fragTran.replace(R.id.setupFragmentFrameLayout, SetupActivitySelectFragment.newInstance(activitySelect));
                        setupTxt.setText(getString(R.string.setup_activity_select_text));
                        fragTran.commit();
                        break;
                }

                setupProgressBar.setProgress(currSetupPage);
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
