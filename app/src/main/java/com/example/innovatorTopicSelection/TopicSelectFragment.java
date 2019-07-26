package com.example.innovatorTopicSelection;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.angela_innovator_v2.R;


public class TopicSelectFragment extends Fragment {

    //-------------------------------variables--------------------------------
    private int topicIdx; //current topic index

    //---------topic storage--------------
    private String[] topicTitles;
    private Image[] topicImages;
    //private ????[] topicLinks; //how do we communicate the topic to the practice view?


    //------------------------------UI elements-------------------------------
    //-------ImageViews & Buttons----------
    private ImageButton leftButton, rightButton;
    private ImageView left, current, right;
    private TextView practiceTitle;

    //-------------progress bar------------
    private ProgressBar progress;
    private TextView progressText;

    //------------Start practice-----------
    private Button startPracticeButton;


    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.topic_select_fragment, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //------------------------------find views---------------------------
        leftButton = view.findViewById(R.id.topic_left_ImageButton);
        rightButton = view.findViewById(R.id.topic_right_ImageButton);

        left = view.findViewById(R.id.left_practice_ImageView);
        current = view.findViewById(R.id.current_practice_ImageView);
        right = view.findViewById(R.id.right_practice_ImageView);

        practiceTitle = view.findViewById(R.id.practice_title);

        progress = view.findViewById(R.id.progressBar);
        progressText = view.findViewById(R.id.progress_text);

        startPracticeButton = view.findViewById(R.id.practice_start_button);
        //------------------------------listeners-----------------------------
        startPracticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent();

                //Transition to another activity, bla bla bla
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //scroll left (check if there is no more)
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //scroll right (check if there is no more)
            }
        });




        //this is triggered soon after OCV above.
        //any view setup here (view lookups, view listener attach)
        super.onViewCreated(view, savedInstanceState);
    }

    private void setProgressBarText(int percent){
        progress.setProgress(percent);
        progressText.setText(percent + "% Mastered");
        if(percent > 63){progressText.setTextColor(Color.WHITE);}else{progressText.setTextColor(Color.BLACK);}
    }

    private void updateTopicView(int position){
        //set the ImageViews, practice title, etc.
    }
}
