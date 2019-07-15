package com.example.innovatorsetup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Main2Activity extends MainActivity {
    private int activitySelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button activitySelectUpBtn = findViewById(R.id.activitySelectUpBtn);
        Button activitySelectDownBtn = findViewById(R.id.activitySelectDownBtn);
        Button setupActivityNextBtn = findViewById(R.id.setupActivityNextBtn);

        final ProgressBar setupProgressBar = findViewById(R.id.setupProgressBar);

        activitySelect = 1;

        activitySelectUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView setupActivityDisplayTxt = findViewById(R.id.setupActivityDisplayTxt);

                activitySelect = Math.min(activitySelect+1, 8);
                if(activitySelect == 1) {
                    setupActivityDisplayTxt.setText(activitySelect + " activity/day");
                }
                else {
                    setupActivityDisplayTxt.setText(activitySelect + " activities/day");
                }
            }
        });
        activitySelectDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView setupActivityDisplayTxt = findViewById(R.id.setupActivityDisplayTxt);

                activitySelect = Math.max(activitySelect-1, 1);
                if(activitySelect == 1) {
                    setupActivityDisplayTxt.setText(activitySelect + " activity/day");
                }
                else {
                    setupActivityDisplayTxt.setText(activitySelect + " activities/day");
                }
            }
        });

        setupActivityNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSetupProgress(getSetupProgress()+1);
                setupProgressBar.setProgress(getSetupProgress());
            }
        });
    }
}
