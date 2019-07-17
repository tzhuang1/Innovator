package com.example.innovatorTopicSelection;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.innovatorsetup.R;

public class ProgressBarTestActivity extends AppCompatActivity {
    ProgressBar progress;
    TextView progressText;
    SeekBar seekbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar_test);
        progress = findViewById(R.id.progressBar);
        progress.setProgress(18);
        progressText = findViewById(R.id.progress_text);
        progressText.setText("18% Mastered");
        seekbar = findViewById(R.id.seek_bar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress.setProgress(i);
                progressText.setText(i+"% Mastered");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }





}
