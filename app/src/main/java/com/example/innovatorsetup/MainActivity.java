package com.example.innovatorsetup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
TODO
 - Implement Persisting data for activities/day & grade
    - Store in local file for now (internal storage or SharedPreferences)
    - Store in database after login is implemented(?)
 - Debug activity select screen
    - Carry over progress bar
    - Smoother transition (maybe don't use another activity?)
 - Other tasks(unimportant)
    - Customize buttons
        - Better buttons than just + and -, may need to wait until full app is developed to ensure design stays consistent
    - Animate screen change
 Useful links:
 File storage overview: https://developer.android.com/guide/topics/data/data-storage
 SharedPreferences: https://developer.android.com/training/data-storage/shared-preferences.html
 */

public class MainActivity extends AppCompatActivity {
    private final String[] gradeList = {"1st", "2nd", "3rd", "4th", "5th", "6th"};
    private int gradeSelect;

    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button gradeSelectLeftBtn = findViewById(R.id.gradeSelectLeftBtn);
        Button gradeSelectRightBtn = findViewById(R.id.gradeSelectRightBtn);

        final ProgressBar setupProgressBar = findViewById(R.id.setupProgressBar);

        gradeSelect = 0;
        progress = 0;

        setupProgressBar.setMax(2);
        setupProgressBar.setProgress(0);

        gradeSelectLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView setupGradeDisplay = findViewById(R.id.setupGradeDisplayTxt);

                gradeSelect = Math.max(--gradeSelect, 0);
                setupGradeDisplay.setText(gradeList[gradeSelect]);
            }
        });
        gradeSelectRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView setupGradeDisplay = findViewById(R.id.setupGradeDisplayTxt);


                gradeSelect = Math.min(++gradeSelect, gradeList.length - 1);
                setupGradeDisplay.setText(gradeList[gradeSelect]);
            }
        });

        Button gradeSelectNextBtn = findViewById(R.id.setupGradeNextBtn);
        gradeSelectNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress++;
                setupProgressBar.setProgress(progress);

                Intent activitySelectIntent = new Intent(view.getContext(), Main2Activity.class);
                startActivity(activitySelectIntent);
                setResult(RESULT_OK, activitySelectIntent);
                finish();
            }
        });
    }

    public int getSetupProgress() {
        return progress;
    }

    public void setSetupProgress(int progress) {
        this.progress = progress;
    }
}
