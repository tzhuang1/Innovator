package com.example.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
TODO
 - Test if SharedPreferences actually works
 - Other tasks(unimportant)
    - Customize buttons
        - Better buttons than just + and -, may need to wait until full app is developed to ensure design stays consistent
    - Animate screen/fragment change
 Finished:
  - Add OnClickListener to back button    
  - Store data from fragments in variables in AngelaMainActivity
 */

public class MainActivity extends AppCompatActivity implements SetupActivitySelectFragment.OnDataPass, SetupGradeSelectFragment.OnDataPass, SetupGradeSelectFragment.onGradeSelectFragmentInteraction, SetupActivitySelectFragment.OnActivitySelectFragmentListener {

    ProgressBar setupProgressBar;
    TextView setupTxt;

    int currSetupPage;

    int gradeSelect;
    int activitySelect;

    static final String SETTINGS = "InitialSettings";
    static final String GRADE = "SelectGrade";
    static final String ACTIVITY = "SelectActivity";

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button gradeSelectNextBtn = findViewById(R.id.setupGradeNextBtn);
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
                if(currSetupPage < 2)
                    currSetupPage++;
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
                        storeSettings();
                        System.out.println(settings.getAll());
                        break;
                }

                setupProgressBar.setProgress(currSetupPage);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener(){
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

    public void storeSettings(){
        settings = getSharedPreferences(SETTINGS, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt(GRADE, gradeSelect);
        edit.putInt(ACTIVITY, activitySelect);
        edit.apply();
    }

    @Override
    public void onGradeSelectFragmentInteraction(int defaultGrade) {

    }

    @Override
    public void OnActivitySelectFragmentListener(int defaultActivityNum) {

    }

    @Override
    public void putGradeSelect(int gradeSelect) {
        this.gradeSelect = gradeSelect;
    }

    @Override
    public void putActivitySelect(int activitySelect) {
        this.activitySelect = activitySelect;
    }
}
