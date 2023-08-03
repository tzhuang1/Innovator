package com.innovator.solve;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.widget.LinearLayout.HORIZONTAL;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import android.widget.Toast;

import android.util.Log;

import com.google.api.Distribution;
import com.innovator.solve.R;


public class MockSelectFragment extends Fragment {
    //------------------------------UI elements-------------------------------
    //-------ImageViews & Buttons----------
    private ImageButton leftButton, rightButton;
    private TextView left, current, right;

    private FrameLayout popup;
    private String testName;
    private ConstraintLayout[] tests;
    private LinearLayout testContainer;
    private int[] IDs = {R.id.row1, R.id.row2, R.id.row3, R.id.row4, R.id.row5, R.id.row6, R.id.row7, R.id.row8, R.id.row9, R.id.row10};
    private int subjectIndex = 0;
    private String[] subjects = {"Math", "Reading", "Science", "Social Studies"};
    private int testCounts[] = {8, 6, 3, 2};

    private View mainView, loadingView;
    private final int NUM_SUBJECTS = subjects.length;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mock_select_fragment, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MockTestManager.init();
        //------------------------------find views---------------------------
        mainView = view.findViewById(R.id.main);
        loadingView = view.findViewById(R.id.loading);
        mainView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);

        leftButton = view.findViewById(R.id.topic_left_ImageButton);
        rightButton = view.findViewById(R.id.topic_right_ImageButton);

        left = view.findViewById(R.id.left_practice_TextView);
        current = view.findViewById(R.id.current_practice_TextView);
        right = view.findViewById(R.id.right_practice_TextView);

        tests = new ConstraintLayout[10];

        for (int i=0; i<10; i++) {
            ConstraintLayout tmp = view.findViewById(IDs[i]);
            tests[i] = tmp;
        }

        testContainer = view.findViewById(R.id.testList);

        popup = view.findViewById(R.id.popup);
        popup.setVisibility(View.GONE);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectIndex = (subjectIndex + NUM_SUBJECTS - 1)%NUM_SUBJECTS;
                renderCarousel();
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectIndex = (subjectIndex + 1)%NUM_SUBJECTS;
                renderCarousel();
            }
        });

        awaitLoad(0);

        //this is triggered soon after OCV above.
        //any view setup here (view lookups, view listener attach)
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean cutWait = false; //terminate awaits if another request gets sent

    private void awaitLoad(int depth) {
        if (depth > 15) {
            //Error message
            Log.d("Error", "Not loaded");
            return;
        }
        if (cutWait) {
            //another render request was sent
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (MockTestManager.loaded) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            renderCarousel();
                            mainView.setVisibility(View.VISIBLE);
                            loadingView.setVisibility(View.GONE);
                            cutWait = false;
                        }
                    }, 25);
                }
                else {
                    awaitLoad(depth+1);
                }
            }
        }, 250);
    }

    private void renderCarousel() {
        //render the top
        Log.d("Rendering", String.valueOf(subjectIndex));

        cutWait = true;

        left.setText(subjects[(subjectIndex + NUM_SUBJECTS - 1)%NUM_SUBJECTS]);
        current.setText(subjects[(subjectIndex)%NUM_SUBJECTS]);
        right.setText(subjects[(subjectIndex+1)%NUM_SUBJECTS]);

        ArrayList<MockTestManager.MockTest> mocks = MockTestManager.getTestBySubject(subjects[(subjectIndex)%NUM_SUBJECTS]);
        int testCount = mocks.size();
        //fetch the bottom
        for (int i=0; i<10; i++) {
            if (i < testCount) {
                tests[i].setVisibility(View.VISIBLE);
                LinearLayout tst = (LinearLayout) tests[i].getChildAt(0);

                MockTestManager.MockTest m = mocks.get(i);

                ((TextView) tst.getChildAt(0)).setText("Grade " + (m.getGrade()));
                ((TextView) tst.getChildAt(1)).setText((m.getQuestionCount()) + " Questions");
                tests[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView txt = (TextView) ((LinearLayout) ((ConstraintLayout) view).getChildAt(0)).getChildAt(0);
                        popup(txt.getText().toString(), m);
                    }
                });
            }
            else {
                tests[i].setVisibility(View.GONE);
            }
        }
    }

    private MockTestManager.MockTest activeSelection;
    private void popup(String testTitle, MockTestManager.MockTest mock) {
        popup.setVisibility(View.VISIBLE);
        LinearLayout container = ((LinearLayout) ((RelativeLayout) popup.getChildAt(0)).getChildAt(0));
        TextView txt = (TextView) container.getChildAt(0);
        txt.setText("Start " + testTitle + " " + subjects[subjectIndex] + " SOL?");
        testName = testTitle + " " + subjects[subjectIndex];
        LinearLayout buttons = (LinearLayout) container.getChildAt(1);
        Button b1 = (Button) buttons.getChildAt(0), b2 = (Button) buttons.getChildAt(1);

        activeSelection = mock;
        b1.setText("Confirm");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.setVisibility(View.GONE);
                startTest();
            }
        });
        b2.setText("Cancel");
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.setVisibility(View.GONE);
            }
        });
    }

    private void popup2(Intent intent, SharedPreferences sp) {
        popup.setVisibility(View.VISIBLE);
        LinearLayout container = ((LinearLayout) ((RelativeLayout) popup.getChildAt(0)).getChildAt(0));
        TextView txt = (TextView) container.getChildAt(0);
        txt.setText("It seems like you didn't completely finish this test last time. Would you like to resume?");
        LinearLayout buttons = (LinearLayout) container.getChildAt(1);
        Button b1 = (Button) buttons.getChildAt(0), b2 = (Button) buttons.getChildAt(1);
        b1.setText("Yes");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.setVisibility(View.GONE);
                intent.putExtra("NUMQUESTIONS", sp.getInt("NUMQUESTIONS", 0));
                intent.putExtra("FROMSAVEDSTATE", true);
                awaitStart(0, intent, activeSelection);
            }
        });
        b2.setText("No");
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.setVisibility(View.GONE);
                intent.putExtra("FROMSAVEDSTATE", false);
                awaitStart(0, intent, activeSelection);
            }
        });
    }

    private void startTest() {
        Intent intent = new Intent(MockSelectFragment.this.getActivity(), TestActivity.class);
        activeSelection.populateQuestions();
        intent.putExtra("TestName", testName);
        SharedPreferences sp = getActivity().getSharedPreferences(testName, Context.MODE_PRIVATE);
        if (sp.contains("NUMQUESTIONS")) {
            popup2(intent, sp);
        }
        else {
            intent.putExtra("FROMSAVEDSTATE", false);
            awaitStart(0, intent, activeSelection);
        }
    }

    private void awaitStart(int depth, Intent intent, MockTestManager.MockTest m) {
        if (depth > 15) {
            //Error message
            Log.d("Error", "Questions could not be populated");
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (m.populated) {
                    intent.putExtra("NUMQUESTIONS", activeSelection.questionList.size());
                    intent.putExtra("TestID", activeSelection.getID());
                    startActivity(intent);
                }
                else {
                    awaitStart(depth+1, intent, m);
                }
            }
        }, 250);
    }



    private void fetchTests() {

    }
}