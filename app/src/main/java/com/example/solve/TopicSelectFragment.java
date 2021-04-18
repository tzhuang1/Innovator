package com.example.solve;

import android.content.Intent;
import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;


public class TopicSelectFragment extends Fragment {

    //-------------------------------variables--------------------------------
    private int topicIdx; //current topic index
    private List<TopicSelect> topicList;
    //---------topic storage--------------

    private Question question;
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
        generateTestTopicList();
        topicIdx = 0;
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
                //Transition to another activity, bla bla bla
                Intent intent = new Intent(TopicSelectFragment.this.getActivity(),QuestionMainActivity.class);
                UserData u = InnovatorApplication.getUser();
                if(u.getGrade() == 3)
                    intent.putExtra("TOPIC",Topic.Grade3);
                else if(u.getGrade() == 4)
                    intent.putExtra("TOPIC",Topic.Grade4);
                else if(u.getGrade() == 5)
                    intent.putExtra("TOPIC",Topic.Grade5);
                else if(u.getGrade() == 6)
                    intent.putExtra("TOPIC",Topic.Grade6);
                startActivity(intent);
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTopicView(--topicIdx);
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTopicView(++topicIdx);
            }
        });




        //this is triggered soon after OCV above.
        //any view setup here (view lookups, view listener attach)
        super.onViewCreated(view, savedInstanceState);
        updateTopicView(topicIdx);
    }

    private void setProgressBarText(int percent){
        progress.setProgress(percent);
        progressText.setText(percent + "% Mastered");
        if(percent > 63){progressText.setTextColor(Color.WHITE);}else{progressText.setTextColor(Color.BLACK);}
    }

    private void updateTopicView(int position){
        //set the ImageViews, practice title, etc.
        TopicSelect topic = topicList.get(position);
        setProgressBarText(topic.getMasteryPct());
        current.setImageResource(topic.getImageDrawable());
        practiceTitle.setText(topic.getTitle());

        if(position == 0){//left is blank
            leftButton.setVisibility(View.GONE);
            left.setVisibility(View.GONE);
        }else{
            leftButton.setVisibility(View.VISIBLE);
            left.setVisibility(View.VISIBLE);
            left.setImageResource(topicList.get(position - 1).getImageDrawable());
        }

        if(position == topicList.size()-1){//right is blank
            rightButton.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
        }else{//left and right available
            rightButton.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);
            right.setImageResource(topicList.get((position + 1)).getImageDrawable());
        }
    }

    private void generateTestTopicList(){//initiates 4 examples for debugging only
        topicList = new ArrayList<TopicSelect>(4);
        TopicSelect tmpTopic = new TopicSelect("Topic one title", R.drawable.ic_topic_test_one, null);
        tmpTopic.setMasteryPct(10);
        topicList.add(tmpTopic);
        tmpTopic = new TopicSelect("Topic two title", R.drawable.ic_topic_test_two, null);
        tmpTopic.setMasteryPct(20);
        topicList.add(tmpTopic);
        tmpTopic = new TopicSelect("Topic three title", R.drawable.ic_topic_test_three, null);
        tmpTopic.setMasteryPct(30);
        topicList.add(tmpTopic);
        tmpTopic = new TopicSelect("Topic four title", R.drawable.ic_topic_test_four, null);
        tmpTopic.setMasteryPct(40);
        topicList.add(tmpTopic);
    }


}
