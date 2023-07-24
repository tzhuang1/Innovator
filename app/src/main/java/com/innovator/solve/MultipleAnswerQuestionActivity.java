package com.innovator.solve;

import android.content.Intent;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MultipleAnswerQuestionActivity extends AppCompatActivity {
    Typeface tb;

    View questionPicLayout;
    TextView questionPicText;
    ImageView questionPic;
    TextView questionText;

    View textAnswersLayout;
    View picAnswersLayout;
    FButton buttonA, buttonB, buttonC, buttonD;
    FButton buttonSubmit;
    ImageView optAPic, optBPic, optCPic, optDPic;

    ImageView explanationPic;

    Question currentQuestion;
    boolean multipleSelect = true;
    UserData currentUser;

    /*List<Question> questionsList;
    List<AnsweredQuestionData> answeredQuestionList;
    int qid = 0;

    private FirebaseFirestore firestoreDB;
    private String currentUserID; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*currentUserID=InnovatorApplication.getUser().getId();
        firestoreDB = FirebaseFirestore.getInstance();

        answeredQuestionList=new ArrayList<AnsweredQuestionData>();

        qid=0;*/
        //------------------------------------------------------------------view
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        setContentView(R.layout.multiple_answer_question_activity);
        setMultipleSelect((Boolean)getIntent().getExtras().get("MSALLOWED"));
        questionPicLayout = findViewById(R.id.question_pic_layout);
        questionPicText = findViewById(R.id.question_pic_text);
        questionPic = findViewById(R.id.question_picture);
        questionText = (TextView) findViewById(R.id.question_text);

        textAnswersLayout = findViewById(R.id.textAnswersLayout);
        picAnswersLayout = findViewById(R.id.picAnswersLayout);
        optAPic = findViewById(R.id.optAPic);
        optBPic = findViewById(R.id.optBPic);
        optCPic = findViewById(R.id.optCPic);
        optDPic = findViewById(R.id.optDPic);
        buttonA = (FButton) findViewById(R.id.buttonA);
        buttonB = (FButton) findViewById(R.id.buttonB);
        buttonC = (FButton) findViewById(R.id.buttonC);
        buttonD = (FButton) findViewById(R.id.buttonD);
        buttonSubmit = (FButton) findViewById(R.id.buttonSubmit);

        tb = Typeface.createFromAsset(getAssets(), "fonts/karla.ttf");

        //Setting typefaces for textview and buttons
        questionPicText.setTypeface(tb);
        questionText.setTypeface(tb);
        buttonA.setTypeface(tb);
        buttonB.setTypeface(tb);
        buttonC.setTypeface(tb);
        buttonD.setTypeface(tb);
        resetColor();
        setQuestion(QuestionManager.decompileData(getIntent().getExtras()));
        displayQuestion();
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

    public void setMultipleSelect(boolean b)
    {
        multipleSelect = b;
        if (b) {
            findViewById(R.id.ms_text).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.ms_text).setVisibility(View.GONE);
        }
    }


    //--------------------------------------------------------UI stuff---------------------------------------------
    private int[] buttonClicked = new int[4];
    //Onclick listener for first button
    private int sum() {
        int i = 0;
        for (int ii: buttonClicked)
        {
            i += ii;
        }
        return i;
    }
    public void buttonA(View view) {
        if (buttonClicked[0] == 1) {
            buttonA.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            buttonA.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.grey));
            buttonClicked[0] = 0;
        }
        else if (multipleSelect || sum() < 1) {
            buttonA.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.blue));
            buttonA.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            buttonClicked[0] = 1;
        }
    }

    //Onclick listener for sec button
    public void buttonB(View view) {
        if (buttonClicked[1] == 1) {
            buttonB.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            buttonB.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.grey));
            buttonClicked[1] = 0;
        }
        else if (multipleSelect || sum() < 1) {
            buttonB.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.blue));
            buttonB.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            buttonClicked[1] = 1;
        }
    }

    //Onclick listener for third button
    public void buttonC(View view) {
        if (buttonClicked[2] == 1) {
            buttonC.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            buttonC.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.grey));
            buttonClicked[2] = 0;
        }
        else if (multipleSelect || sum() < 1) {
            buttonC.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.blue));
            buttonC.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            buttonClicked[2] = 1;
        }
    }

    //Onclick listener for fourth button
    public void buttonD(View view) {
        if (buttonClicked[3] == 1) {
            buttonD.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            buttonD.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.grey));
            buttonClicked[3] = 0;
        }
        else if (multipleSelect || sum() < 1){
            buttonD.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.blue));
            buttonD.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            buttonClicked[3] = 1;
        }
    }
    public void buttonSubmit(View view) {
        String ansString = "";
        String shortenedAns = "";
        int ASCII = 65;
        for (int ans: buttonClicked) {
            if (ans == 1) {
                ansString += "Option " + (char)ASCII + " ";
                shortenedAns += (char)ASCII + " ";
            }
            ASCII++;
        }
        if (ansString.equals("")) {
            return;
        }
        buttonClicked = new int[4];
        ansString = ansString.substring(0, ansString.length()-1);
        shortenedAns = shortenedAns.substring(0, shortenedAns.length()-1);
        //saveHistory(qid, ansString, currentQuestion);
        if (currentQuestion.getAnswer().equalsIgnoreCase(ansString) ||
                currentQuestion.getAnswer().equalsIgnoreCase(shortenedAns)) {
            buttonSubmit.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            //TODO: when on last question and success, the success dialog does not show
            disableButton();

        }
    }
    //This method will make button color white again since our one button color was turned green
    public void resetColor() {
        buttonA.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonA.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.grey));
        buttonB.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonB.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.grey));
        buttonC.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonC.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.grey));
        buttonD.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonD.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.grey));
    }

    //This method will disable all the option button
    public void disableButton() {
        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);
    }

    //This method will all enable the option buttons
    public void enableButton() {
        buttonA.setEnabled(true);
        buttonB.setEnabled(true);
        buttonC.setEnabled(true);
        buttonD.setEnabled(true);
    }
}
