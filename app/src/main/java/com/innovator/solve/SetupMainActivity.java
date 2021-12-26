package com.innovator.solve;

import android.content.Intent;
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
import android.widget.Toast;

import com.innovator.solve.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
TODO
 - Other tasks(unimportant)
    - Customize buttons
        - Better buttons than just + and -, may need to wait until full app is developed to ensure design stays consistent
    - Animate screen/fragment change
 */

public class SetupMainActivity extends AppCompatActivity implements SetupActivitySelectFragment.OnDataPass, SetupGradeSelectFragment.OnDataPass {

    public static final String SHARED_PREFERENCES_FILE = "com.example.solve.preferenceFile";

    private final AppCompatActivity me = this;

    ProgressBar setupProgressBar;
    TextView setupTxt;

    boolean showSetup = true;

    int currSetupPage;

    int gradeSelect;
    int activitySelect;

    SharedPreferences settings;

    static final String GRADE = "SelectGrade";
    static final String ACTIVITY = "SelectActivity";
    static final String SHOW_SETUP = "ShowSetup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        settings = getSharedPreferences(SHARED_PREFERENCES_FILE, 0);

        if (settings.getBoolean(SHOW_SETUP, true)) {
            showSetup = true;
        } else {
            // Set to showSetup = true to test setup
            showSetup = false;
        }

        //showSetup = true; //uncomment to test setup screen

        if (!showSetup) {
            Intent mainMenuIntent = new Intent(me, MainMenuActivity.class);
            startActivity(mainMenuIntent);
            finish();
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_activity_main);

        final Button gradeSelectNextBtn = findViewById(R.id.setupGradeNextBtn);
        Button backBtn = findViewById(R.id.backBtn);
        final FragmentTransaction fragTran1 = getSupportFragmentManager().beginTransaction();

        setupProgressBar = findViewById(R.id.setupProgressBar);
        setupProgressBar.getProgressDrawable().setColorFilter(Color.parseColor("#19A0FB"), android.graphics.PorterDuff.Mode.SRC_IN);
        setupTxt = findViewById(R.id.setupTxt);

        gradeSelect = 1;
        activitySelect = 1;

        if (findViewById(R.id.setupFragmentFrameLayout) != null) {
//            SetupGradeSelectFragment gradeFragment = SetupGradeSelectFragment.newInstance(1);
//            gradeFragment.setArguments(getIntent().getExtras());
//            fragTran1.add(R.id.setupFragmentFrameLayout, gradeFragment);
//            fragTran1.commit();
            fragTran1.add(R.id.setupFragmentFrameLayout, new AccountFragment());
            fragTran1.commit();
        }

        currSetupPage = 0;

        gradeSelectNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragMan = getSupportFragmentManager();
                FragmentTransaction fragTran2 = fragMan.beginTransaction();

                //fragTran1.addToBackStack(null);
                if (currSetupPage < 3)
                    currSetupPage++;
                switch (currSetupPage) {
                    case 0:
                        fragTran2.replace(R.id.setupFragmentFrameLayout, new AccountFragment());
                        setupTxt.setText(getString(R.string.sign_In_Google));
                        fragTran2.addToBackStack(null); //user can reverse transaction of replacing the setupFragmentFrameLayout
                        fragTran2.commit();
                        break;
                    case 1:
                        fragTran2.replace(R.id.setupFragmentFrameLayout, SetupGradeSelectFragment.newInstance(gradeSelect));
                        setupTxt.setText(getString(R.string.setup_grade_select_text));
                        fragTran2.addToBackStack(null); //user can reverse transaction of replacing the setupFragmentFrameLayout
                        fragTran2.commit();
                        break;
                    case 2:
                        fragTran2.replace(R.id.setupFragmentFrameLayout, SetupActivitySelectFragment.newInstance(activitySelect));
                        setupTxt.setText(getString(R.string.setup_activity_select_text));
                        //fragTran1.addToBackStack("grade-to-activity");
                        fragTran2.addToBackStack(null);
                        fragTran2.commit();
                        break;
                    case 3:
                        storeSettings();
                        setSetupFinished();
                        /*
                        TODO
                         Remove println when done, used for debug
                         */
                        System.out.println(settings.getAll());
                        Intent mainMenuIntent = new Intent(me, MainMenuActivity.class);
                        startActivity(mainMenuIntent);
                        finish();
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
                if (currSetupPage > 0) {
                    currSetupPage--;
                    setupProgressBar.setProgress(currSetupPage);
                }
                else{
                    Toast.makeText(view.getContext(), "Please Sign-in before starting questions", Toast.LENGTH_SHORT).show();
                }
                switch (currSetupPage) {
                    case 0:
                        setupTxt.setText("Sign in To Google");
                        break;
                    case 1:
                        setupTxt.setText(getString(R.string.setup_grade_select_text));
                        break;
                    case 2:
                        setupTxt.setText(getString(R.string.setup_activity_select_text));
                        break;
                    case 3:

                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + currSetupPage);
                }

                //onBackPressed();

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        storeSettings();
    }

    public void setSetupFinished() {
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(SHOW_SETUP, false);
        edit.apply();
    }

    public void storeSettings() {
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt(GRADE, gradeSelect);
        edit.putInt(ACTIVITY, activitySelect);
        edit.apply();
        UserData u = InnovatorApplication.getUser();
        if(u != null && u.getId() != null) {
            u.setGrade(gradeSelect);
            u.setActivities(activitySelect);
            DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference().child("UserData").child("Profile").child(u.getId());
            if(qListRef!=null)
                qListRef.setValue(u);
//            else {
//                FirebaseDatabase.getInstance().getReference().child("UserData").child("Profile").add
//                qListRef.push().setValue(u);
//
//            }
        }
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
