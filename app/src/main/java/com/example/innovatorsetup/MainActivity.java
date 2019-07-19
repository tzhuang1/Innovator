package com.example.innovatorsetup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
TODO
 - not sure why initial screen is not the setUpGradetxt but straight to grade fragment??
 - Store data from fragments in variables in MainActivity
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
 - Finished:
    - Add OnClickListener to back button
 */

public class MainActivity extends AppCompatActivity implements SetupGradeSelectFragment.onGradeSelectFragmentInteraction, SetupActivitySelectFragment.OnActivitySelectFragmentListener {

    ProgressBar setupProgressBar;
    TextView setupTxt;

    int currSetupPage;

    int gradeSelect;
    int activitySelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button gradeSelectNextBtn = findViewById(R.id.setupGradeNextBtn);
        Button backBtn = findViewById(R.id.backBtn);
        final FragmentTransaction fragTran1 = getSupportFragmentManager().beginTransaction();

        setupProgressBar = findViewById(R.id.setupProgressBar);
        setupProgressBar.getProgressDrawable().setColorFilter(Color.parseColor("#19A0FB"), android.graphics.PorterDuff.Mode.SRC_IN);
        setupTxt = findViewById(R.id.setupTxt);

        gradeSelect = 1;
        activitySelect = 1;

        if(findViewById(R.id.setupFragmentFrameLayout) != null) {
            SetupGradeSelectFragment gradeFragment = SetupGradeSelectFragment.newInstance(1);
            gradeFragment.setArguments(getIntent().getExtras());
            fragTran1.add(R.id.setupFragmentFrameLayout, gradeFragment);
            fragTran1.commit();
        }

        currSetupPage = 0;

        gradeSelectNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragMan = getSupportFragmentManager();
                FragmentTransaction fragTran2 = fragMan.beginTransaction();

                //fragTran1.addToBackStack(null);
                if(currSetupPage < 2) {
                    currSetupPage++;
                }
                switch(currSetupPage) {
                    case 0:
                        fragTran2.replace(R.id.setupFragmentFrameLayout, SetupGradeSelectFragment.newInstance(gradeSelect));
                        setupTxt.setText(getString(R.string.setup_grade_select_text));
                        fragTran2.addToBackStack(null); //user can reverse transaction of replacing the setupFragmentFrameLayout
                        fragTran2.commit();
                        break;
                    case 1:
                        fragTran2.replace(R.id.setupFragmentFrameLayout, SetupActivitySelectFragment.newInstance(activitySelect));
                        setupTxt.setText(getString(R.string.setup_activity_select_text));
                        //fragTran1.addToBackStack("grade-to-activity");
                        fragTran2.addToBackStack(null);
                        fragTran2.commit();
                        break;
                    case 2:

                        break;
                }

                setupProgressBar.setProgress(currSetupPage);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getSupportFragmentManager().popBackStack("grade-to-activity", 0);

                getSupportFragmentManager().popBackStack();
                if(currSetupPage > 0) {
                    currSetupPage--;
                    setupProgressBar.setProgress(currSetupPage);
                }

                switch(currSetupPage) {
                    case 0:
                        setupTxt.setText(getString(R.string.setup_grade_select_text));
                        break;
                    case 1:
                        setupTxt.setText(getString(R.string.setup_activity_select_text));
                        break;
                    case 2:

                        break;
                }

                //onBackPressed();

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
