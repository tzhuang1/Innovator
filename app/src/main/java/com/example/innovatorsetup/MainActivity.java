package com.example.innovatorsetup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
TODO
 - Implement Persisting data for activities/day & grade
    - Store in local file for now (cache file)
    - Store in database after login is implemented(?)
 - Create new activity for activities/day
    - Needed for activities/day screen:
        - New activity
        - New set of GUI
 Useful links:
 Switching between activities: https://stackoverflow.com/questions/7991393/how-to-switch-between-screens
 File storage overview: https://developer.android.com/guide/topics/data/data-storage
 */

public class MainActivity extends AppCompatActivity {
    private final String[] gradeList = {"1st", "2nd", "3rd", "4th", "5th", "6th"};
    private int gradeSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gradeSelect = 0;

        Button gradeSelectLeftBtn = findViewById(R.id.gradeSelectLeftBtn);
        Button gradeSelectRightBtn = findViewById(R.id.gradeSelectRightBtn);

        gradeSelectLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView setupGradeDisplay = findViewById(R.id.setupGradeDisplayTxt);

                gradeSelect--;
                if(gradeSelect < 0) {
                    gradeSelect = 0;
                }
                setupGradeDisplay.setText(gradeList[gradeSelect]);
            }
        });
        gradeSelectRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView setupGradeDisplay = findViewById(R.id.setupGradeDisplayTxt);

                gradeSelect++;
                if(gradeSelect > 5) {
                    gradeSelect = 5;
                }
                setupGradeDisplay.setText(gradeList[gradeSelect]);
            }
        });

        Button gradeSelectNextBtn = findViewById(R.id.setupGradeNextBtn);
    }
}
