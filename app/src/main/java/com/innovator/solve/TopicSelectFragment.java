package com.innovator.solve;

import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import android.widget.Toast;

import android.util.Log;

import com.innovator.solve.R;


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
    private RadioButton radioButtonReading;
    private RadioButton radioButtonMath;
    private RadioGroup radioGroup;

    //-------------progress bar------------
    private ProgressBar progress;
    private TextView progressText;

    //------------Start practice-----------
    private Button startPracticeButton;

    //----grade sent from settings---------
    private String grade;
  
    private String[] mathCategories={"Computation and Estimation", "Measurement and Geometry","Numbers and Number Sense","Patterns, Functions, and Algebra", "Probability and Statistics"};
    private String[] readingCategories={"Reading Comprehension", "Word Analysis"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.topic_select_fragment, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

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

        radioGroup=view.findViewById(R.id.RadioGroup);
        radioButtonReading = view.findViewById(R.id.radioReading);
        radioButtonMath = view.findViewById(R.id.radioMath);
        radioButtonMath.setChecked(true); //by default, math is selected

        generateTestTopicList();
        //------------------get grade from saved settings and set grade------------------------
        SharedPreferences saved;
        saved = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        grade = saved.getString("grade", "9");
        TopicManager.setDataLocations(grade);
        //------------------------------listeners-----------------------------
        startPracticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(grade.equals("9") && radioButtonReading.isChecked())) {
                    Intent intent = new Intent(TopicSelectFragment.this.getActivity(), QuestionMainActivity.class);
                    if (radioButtonReading.isChecked()) {
                        TopicManager.setCategory(readingCategories[topicIdx]);
                        intent = new Intent(TopicSelectFragment.this.getActivity(), ReadingQuestions.class);
                    }
                    UserData u = InnovatorApplication.getUser();

                    //u.getGrade() is returning grade of 0, so using locally saved grade for now
                    if (grade == null) {
                        Toast.makeText(view.getContext(), "Please save grade level before starting topic", Toast.LENGTH_SHORT).show();
                    } else {
                        if (grade == "3") {
                            intent.putExtra("TOPIC", Topic.Grade3);
                        } else if (grade == "4") {
                            intent.putExtra("TOPIC", Topic.Grade4);
                        } else if (grade == "5") {
                            intent.putExtra("TOPIC", Topic.Grade5);
                        } else if (grade == "6") {
                            intent.putExtra("TOPIC", Topic.Grade6);
                        } else if (grade == "7") {
                            intent.putExtra("TOPIC", Topic.Grade7);
                        } else if (grade == "8") {
                            intent.putExtra("TOPIC", Topic.Grade8);
                        } else if (grade == "9") {
                            intent.putExtra("TOPIC", Topic.Grade9);
                        }
                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(view.getContext(), "Unfortunately, there are no Grade 9 reading questions at this time.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTopicView(--topicIdx);
                if(radioButtonMath.isChecked()){
                    TopicManager.setCategory(mathCategories[topicIdx]);
                }
                else{
                    TopicManager.setCategory(readingCategories[topicIdx]);
                }
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTopicView(++topicIdx);
                if(radioButtonMath.isChecked()){
                    TopicManager.setCategory(mathCategories[topicIdx]);
                }
                else{
                    TopicManager.setCategory(readingCategories[topicIdx]);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                topicIdx=0;
                if(radioButtonMath.isChecked()){
                    TopicManager.setCategory(mathCategories[topicIdx]);
                }
                else{
                    TopicManager.setCategory(readingCategories[topicIdx]);
                }

                generateTestTopicList();
                updateTopicView(topicIdx);
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
        if(radioButtonMath.isChecked()){
            topicList=new ArrayList<TopicSelect>(5);
            TopicSelect tmpTopic = new TopicSelect("Computation and Estimation", R.drawable.ic_topic_test_one, null);
            tmpTopic.setMasteryPct(0);
            topicList.add(tmpTopic);
            tmpTopic = new TopicSelect("Measurement and Geometry", R.drawable.ic_topic_test_two, null);
            tmpTopic.setMasteryPct(0);
            topicList.add(tmpTopic);
            tmpTopic = new TopicSelect("Numbers and Number Sense", R.drawable.ic_topic_test_three, null);
            tmpTopic.setMasteryPct(0);
            topicList.add(tmpTopic);
            tmpTopic = new TopicSelect("Patterns, Functions, and Algebra", R.drawable.ic_topic_test_four, null);
            tmpTopic.setMasteryPct(0);
            topicList.add(tmpTopic);
            tmpTopic=new TopicSelect("Probability and Statistics", R.drawable.ic_topic_test_five, null);
            tmpTopic.setMasteryPct(0);
            topicList.add(tmpTopic);
        }
        if(radioButtonReading.isChecked()){
            topicList=new ArrayList<TopicSelect>(2);
            TopicSelect tmpTopic=new TopicSelect(readingCategories[0],R.drawable.ic_topic_test_one, null);
            tmpTopic.setMasteryPct(0);
            topicList.add(tmpTopic);
            tmpTopic=new TopicSelect(readingCategories[1],R.drawable.ic_topic_test_two,null);
            tmpTopic.setMasteryPct(0);
            topicList.add(tmpTopic);
        }
    }




}
