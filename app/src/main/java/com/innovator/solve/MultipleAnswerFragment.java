package com.innovator.solve;

import android.content.Intent;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.innovator.solve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import info.hoang8f.widget.FButton;

import static android.view.View.GONE;

public class MultipleAnswerFragment extends Fragment {
    public interface OnAnswerSelected {
        public void onMcqAnswerSelect(int[] answers);
        public void clearChoices();
    }
    OnAnswerSelected mCallback;
    Typeface tb;
    View view;
    View questionPicLayout;
    TextView questionPicText;
    ImageView questionPic;
    TextView questionText;

    View textAnswersLayout;
    View picAnswersLayout;
    FButton buttonA, buttonB, buttonC, buttonD, buttonE, buttonF, buttonG;
    FButton buttonSubmit;
    ImageView optAPic, optBPic, optCPic, optDPic;

    ImageView explanationPic;

    Question currentQuestion;
    //boolean multipleSelect = true;

    int numChoices = 4;
    int reqAnswers = 1;

    /*List<Question> questionsList;
    List<AnsweredQuestionData> answeredQuestionList;
    int qid = 0;

    private FirebaseFirestore firestoreDB;
    private String currentUserID; */
    private int[] buttonClicked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.multiple_answer_question_activity, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //multipleSelect = (Boolean)requireArguments().getBoolean("MSALLOWED");
        mCallback = (OnAnswerSelected)getActivity();
        numChoices = (Integer)requireArguments().getInt("NUMCHOICES");
        reqAnswers = (Integer)requireArguments().getInt("REQANSWERS");
        if (reqAnswers == -1) {
            reqAnswers = numChoices + 1;
            view.findViewById(R.id.ms_text).setVisibility(View.VISIBLE);

        }
        if (reqAnswers > 1) {
            TextView ms_text = view.findViewById(R.id.ms_text);
            ms_text.setVisibility(View.VISIBLE);
            ms_text.setText("(Choose " + reqAnswers +" answers total)");
        }
        else {
            view.findViewById(R.id.ms_text).setVisibility(View.GONE);
        }
        buttonClicked = new int[numChoices];
        this.view = view;
        questionPicLayout = view.findViewById(R.id.question_pic_layout);
        questionPicText = view.findViewById(R.id.question_pic_text);
        questionPic = view.findViewById(R.id.question_picture);
        questionText = (TextView) view.findViewById(R.id.question_text);

        textAnswersLayout = view.findViewById(R.id.textAnswersLayout);
        picAnswersLayout = view.findViewById(R.id.picAnswersLayout);
        optAPic = view.findViewById(R.id.optAPic);
        optBPic = view.findViewById(R.id.optBPic);
        optCPic = view.findViewById(R.id.optCPic);
        optDPic = view.findViewById(R.id.optDPic);
        buttonA = (FButton) view.findViewById(R.id.buttonA);
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonA(true);
            }
        });
        buttonB = (FButton) view.findViewById(R.id.buttonB);
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonB(true);
            }
        });
        buttonC = (FButton) view.findViewById(R.id.buttonC);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonC(true);
            }
        });
        buttonD = (FButton) view.findViewById(R.id.buttonD);
        if (numChoices > 3) {
            buttonD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonD(true);
                }
            });
        }
        else
            view.findViewById(R.id.buttonD).setVisibility(View.GONE);
        buttonE = (FButton) view.findViewById(R.id.buttonE);
        if (numChoices > 4) {
            view.findViewById(R.id.buttonE).setVisibility(View.VISIBLE);
            buttonE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonE(true);
                }
            });
        }
        else
            view.findViewById(R.id.buttonE).setVisibility(View.GONE);
        buttonF = (FButton) view.findViewById(R.id.buttonF);
        if (numChoices > 5) {
            view.findViewById(R.id.buttonF).setVisibility(View.VISIBLE);
            buttonF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonF(true);
                }
            });
        }
        else
            view.findViewById(R.id.buttonF).setVisibility(View.GONE);
        buttonG = (FButton) view.findViewById(R.id.buttonG);
        if (numChoices > 6) {
            view.findViewById(R.id.buttonG).setVisibility(View.VISIBLE);

            buttonG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonG(true);
                }
            });
        }
        else
            view.findViewById(R.id.buttonG).setVisibility(View.GONE);
        buttonSubmit = (FButton) view.findViewById(R.id.buttonSubmit);

        tb = Typeface.createFromAsset(getContext().getAssets(), "fonts/karla.ttf");

        //Setting typefaces for textview and buttons
        questionPicText.setTypeface(tb);
        questionText.setTypeface(tb);
        buttonA.setTypeface(tb);
        buttonB.setTypeface(tb);
        buttonC.setTypeface(tb);
        buttonD.setTypeface(tb);
        buttonE.setTypeface(tb);
        buttonG.setTypeface(tb);
        buttonF.setTypeface(tb);
        resetColor();
        setQuestion(QuestionManager.decompileData(requireArguments()));
        displayQuestion();

        int numAns = requireArguments().getInt("NUMANSWERS");
        for (int i = 0; i < numAns; i++) {
            setAnswer(requireArguments().getInt("ANSWER" + i));
        }
    }

    public void onDestroy() {
        //updateChoices();
        super.onDestroy();
    }

    public void updateChoices() {
        mCallback.clearChoices();
        int[] answers = new int[sum()];
        int z = 0;
        for (int i = 0; i < buttonClicked.length; i++) {
            if (buttonClicked[i] == 1)
                answers[z++] = i;
        }
        mCallback.onMcqAnswerSelect(answers);
    }

    public void setQuestion(Question q)
    {
        currentQuestion = q;
    }

    public void displayQuestion() {
        questionText.setText(currentQuestion.getQuestion());
        buttonA.setText(currentQuestion.getOptA());
        buttonB.setText(currentQuestion.getOptB());
        buttonC.setText(currentQuestion.getOptC());
        buttonD.setText(currentQuestion.getOptD());
    }

    public void setAnswer(int i) {
        if (i == 0)
            buttonA(false);
        if (i == 1)
            buttonB(false);
        if (i == 2)
            buttonC(false);
        if (i == 3)
            buttonD(false);
        if (i == 4)
            buttonE(false);
        if (i == 5)
            buttonF(false);
        if (i == 6)
            buttonG(false);
    }


    //--------------------------------------------------------UI stuff---------------------------------------------
    private int sum() {
        int i = 0;
        for (int ii: buttonClicked)
        {
            i += ii;
        }
        return i;
    }

    public void updateButtons(boolean notOnCreate) {
        if (notOnCreate)
            updateChoices();
        buttonA.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[0] == 1)?R.color.blue:R.color.white));
        buttonB.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[1] == 1)?R.color.blue:R.color.white));
        if (numChoices > 2) buttonC.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[2] == 1)?R.color.blue:R.color.white));
        if (numChoices > 3) buttonD.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[3] == 1)?R.color.blue:R.color.white));
        if (numChoices > 4) buttonE.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[4] == 1)?R.color.blue:R.color.white));
        if (numChoices > 5) buttonF.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[5] == 1)?R.color.blue:R.color.white));
        if (numChoices > 6) buttonG.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[6] == 1)?R.color.blue:R.color.white));
        buttonA.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[0] == 1)?R.color.white:R.color.grey));
        buttonB.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[1] == 1)?R.color.white:R.color.grey));
        if (numChoices > 2) buttonC.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[2] == 1)?R.color.white:R.color.grey));
        if (numChoices > 3) buttonD.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[3] == 1)?R.color.white:R.color.grey));
        if (numChoices > 4) buttonE.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[4] == 1)?R.color.white:R.color.grey));
        if (numChoices > 5) buttonF.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[5] == 1)?R.color.white:R.color.grey));
        if (numChoices > 6) buttonG.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), (buttonClicked[6] == 1)?R.color.white:R.color.grey));
    }

    public void buttonA(boolean notOnCreate) {
        if (buttonClicked[0] == 1) {
            buttonClicked[0] = 0;
        }
        else {
            if (reqAnswers == 1) buttonClicked = new int[numChoices];
            if (sum() < reqAnswers) buttonClicked[0] = 1;
        }
        updateButtons(notOnCreate);
    }

    public void buttonB(boolean notOnCreate) {
        if (buttonClicked[1] == 1) {
            buttonClicked[1] = 0;
        }
        else {
            if (reqAnswers == 1) buttonClicked = new int[numChoices];
            if (sum() < reqAnswers) buttonClicked[1] = 1;
        }
        updateButtons(notOnCreate);
    }

    public void buttonC(boolean notOnCreate) {
        if (buttonClicked[2] == 1) {
            buttonClicked[2] = 0;
        }
        else {
            if (reqAnswers == 1) buttonClicked = new int[numChoices];
            if (sum() < reqAnswers) buttonClicked[2] = 1;
        }
        updateButtons(notOnCreate);
    }

    public void buttonD(boolean notOnCreate) {
        if (buttonClicked[3] == 1) {
            buttonClicked[3] = 0;
        }
        else {
            if (reqAnswers == 1) buttonClicked = new int[numChoices];
            if (sum() < reqAnswers) buttonClicked[3] = 1;
        }
        updateButtons(notOnCreate);
    }

    public void buttonE(boolean notOnCreate) {
        if (buttonClicked[4] == 1) {
            buttonClicked[4] = 0;
        }
        else {
            if (reqAnswers == 1) buttonClicked = new int[numChoices];
            if (sum() < reqAnswers) buttonClicked[4] = 1;
        }
        updateButtons(notOnCreate);
    }

    public void buttonF(boolean notOnCreate) {
        if (buttonClicked[5] == 1) {
            buttonClicked[5] = 0;
        }
        else {
            if (reqAnswers == 1) buttonClicked = new int[numChoices];
            if (sum() < reqAnswers) buttonClicked[5] = 1;
        }
        updateButtons(notOnCreate);
    }

    public void buttonG(boolean notOnCreate) {
        if (buttonClicked[6] == 1) {
            buttonClicked[6] = 0;
        }
        else {
            if (reqAnswers == 1) buttonClicked = new int[numChoices];
            if (sum() < reqAnswers) buttonClicked[6] = 1;
        }
        updateButtons(notOnCreate);
    }

    public void resetColor() {
        buttonA.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.white));
        buttonA.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.grey));
        buttonB.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.white));
        buttonB.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.grey));
        buttonC.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.white));
        buttonC.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.grey));
        buttonD.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.white));
        buttonD.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.grey));
        buttonE.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.white));
        buttonE.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.grey));
        buttonF.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.white));
        buttonF.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.grey));
        buttonG.setButtonColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.white));
        buttonG.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.grey));
    }
}
