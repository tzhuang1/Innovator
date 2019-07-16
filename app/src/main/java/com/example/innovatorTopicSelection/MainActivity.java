package com.example.innovatorTopicSelection;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.innovatorsetup.R;


public class MainActivity extends AppCompatActivity {

    Button startPracticeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_select);
        startPracticeButton = findViewById(R.id.practice_start_button);



    }
}
