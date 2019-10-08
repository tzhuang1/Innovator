package com.example.solve;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import info.hoang8f.widget.FButton;

public class QuestionMainActivity extends AppCompatActivity {

    FButton buttonA, buttonB, buttonC, buttonD;
    View loadingScreen;
    TextView question;
    Typeface tb;

    Questions currentQuestion;
    UserData currentUser;
    QuestionsHelper questionsHelper;

    List<Questions> questionsList;
    int qid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //------------------------------------------------------------------view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity_main);
        //Initializing variables
        buttonA = (FButton) findViewById(R.id.buttonA);
        buttonB = (FButton) findViewById(R.id.buttonB);
        buttonC = (FButton) findViewById(R.id.buttonC);
        buttonD = (FButton) findViewById(R.id.buttonD);
        loadingScreen = findViewById(R.id.loading_screen);
        question = (TextView) findViewById(R.id.question);
        tb = Typeface.createFromAsset(getAssets(), "fonts/karla.ttf");

        //Setting typefaces for textview and buttons
        question.setTypeface(tb);
        buttonA.setTypeface(tb);
        buttonB.setTypeface(tb);
        buttonC.setTypeface(tb);
        buttonD.setTypeface(tb);
        resetColor();

        /*------------------------------------------------------------------SQLite stuff (local)

        //Our database helper class
        questionsHelper = new QuestionsHelper(this);
        //Make db writable
        questionsHelper.getWritableDatabase();

        //Checks if the question options are already added in the table or not
        //If they are not added, getAllOfTheQuestions() will return a questionsList of size zero
        if (questionsHelper.getAllOfTheQuestions().size() == 0) {
            //If not added then add the ques,options in table
            questionsHelper.allQuestion();
        }
        //This will return us a questionsList of data type TriviaQuestion
        //questionsList = questionsHelper.getAllOfTheQuestions();
        */
        //------------------------------------------------------------------Firebase stuff (cloud)
        getFirebaseQuestionsList();
    }

    private void getFirebaseQuestionsList(){

        DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference("SampleQs");
        qListRef.addValueEventListener(new ValueEventListener() {//This retrieves the data once
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionsList = dataSnapshot.getValue(new GenericTypeIndicator<List<Questions>>() {});
                Log.i("FB getList", "Firebase data fetched");
                Collections.shuffle(questionsList);
                currentQuestion = questionsList.get(qid);//currentQuestion will hold the que, 4 option and ans for particular id
                loadingScreen.setVisibility(View.GONE);
                updateQueueAndOptions();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO: handle network outage
                Log.e("FB getList", "onCancelled with "+databaseError.getMessage()+", details: "+databaseError.getDetails());
            }
        });
    }

    private void getFirebaseUserData(){

        DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference("UserData");
        qListRef.addValueEventListener(new ValueEventListener() {//This retrieves the data once
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<UserData> type = new GenericTypeIndicator<UserData>() {};
                currentUser = dataSnapshot.getValue(type); //DatabaseException: Class java.util.List has generic type parameters, please use GenericTypeIndicator instead
                Log.i("Get User Data", "Firebase data fetched");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FB getList", "onCancelled with "+databaseError.getMessage()+", details: "+databaseError.getDetails());
            }
        });
    }

    public void updateQueueAndOptions() {

        //This method will setText for que and options
        question.setText(currentQuestion.getQuestion());
        buttonA.setText(currentQuestion.getOptA());
        buttonB.setText(currentQuestion.getOptB());
        buttonC.setText(currentQuestion.getOptC());
        buttonD.setText(currentQuestion.getOptD());
    }

    //Onclick listener for first button
    public void buttonA(View view) {
        //compare the option with the ans if yes then make button color green
        if (currentQuestion.getOptA().equals(currentQuestion.getAnswer())) {
            buttonA.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            //Check if user has not exceeds the que limit
            if (qid < questionsList.size() - 1) {

                //Now disable all the option button since user ans is correct so
                //user won't be able to press another option button after pressing one button
                disableButton();

                //Show the dialog that ans is correct
                correctDialog();
            }
            //If user has exceeds the que limit just navigate him to GameWon activity
            else {

            }
        }
        //User ans is wrong then just navigate him to the PlayAgain activity
        else {
            incorrectDialog();
        }
    }

    //Onclick listener for sec button
    public void buttonB(View view) {
        if (currentQuestion.getOptB().equals(currentQuestion.getAnswer())) {
            buttonB.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            if (qid < questionsList.size() - 1) {
                disableButton();
                correctDialog();
            } else {

            }
        } else { // in place of PlayAgain activity to be implemented later? test to pull up incorrect dialog
            incorrectDialog();
        }
    }

    //Onclick listener for third button
    public void buttonC(View view) {
        if (currentQuestion.getOptC().equals(currentQuestion.getAnswer())) {
            buttonC.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            if (qid < questionsList.size() - 1) {
                disableButton();
                correctDialog();
            } else {

            }
        } else {
            incorrectDialog();
        }
    }

    //Onclick listener for fourth button
    public void buttonD(View view) {
        if (currentQuestion.getOptD().equals(currentQuestion.getAnswer())) {
            buttonD.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            if (qid < questionsList.size() - 1) {
                disableButton();
                correctDialog();
            } else {

            }
        } else {
            incorrectDialog();
        }
    }

    //This dialog is show to the user after he ans correct
    public void correctDialog() {
        final Dialog dialogCorrect = new Dialog(QuestionMainActivity.this);
        dialogCorrect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialogCorrect.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialogCorrect.getWindow().setBackgroundDrawable(colorDrawable);
        }
        dialogCorrect.setContentView(R.layout.dialog_correct);
        dialogCorrect.setCancelable(false);
        dialogCorrect.show();

        //Since the dialog is show to user just pause the timer in background
        onPause();


        TextView correctText = (TextView) dialogCorrect.findViewById(R.id.correctText);
        FButton buttonNext = (FButton) dialogCorrect.findViewById(R.id.dialogNext);

        //Setting type faces
        correctText.setTypeface(tb);
        buttonNext.setTypeface(tb);

        //OnCLick listener to go next que
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This will dismiss the dialog
                dialogCorrect.dismiss();
                //it will increment the question number
                qid++;
                //get the que and 4 option and store in the currentQuestion
                currentQuestion = questionsList.get(qid);
                //Now this method will set the new que and 4 options
                updateQueueAndOptions();
                //reset the color of buttons back to white
                resetColor();
                //Enable button - remember we had disable them when user ans was correct in there particular button methods
                enableButton();
            }
        });
    }

    public void incorrectDialog() {
        final Dialog dialogIncorrect = new Dialog(QuestionMainActivity.this);
        dialogIncorrect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialogIncorrect.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialogIncorrect.getWindow().setBackgroundDrawable(colorDrawable);
        }
        dialogIncorrect.setContentView(R.layout.dialog_incorrect);
        dialogIncorrect.setCancelable(false);
        dialogIncorrect.show();

        //Since the dialog is show to user just pause the timer in background
        onPause();


        TextView incorrectText = (TextView) dialogIncorrect.findViewById(R.id.incorrectText);
        TextView incorrect_label = (TextView) dialogIncorrect.findViewById(R.id.explanationLabel);
        TextView incorrect_explanation = (TextView) dialogIncorrect.findViewById(R.id.explanationText);
        FButton buttonNext = (FButton) dialogIncorrect.findViewById(R.id.dialogNext);

        //Setting type faces
        incorrectText.setTypeface(tb);
        incorrect_explanation.setTypeface(tb);
        incorrect_explanation.setMovementMethod(new ScrollingMovementMethod());
        incorrect_label.setTypeface(tb);
        buttonNext.setTypeface(tb);

        incorrect_explanation.setText(currentQuestion.getExplanation());

        //OnCLick listener to go next que
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This will dismiss the dialog
                dialogIncorrect.dismiss();
                //it will increment the question number
                qid++;
                //get the que and 4 option and store in the currentQuestion
                currentQuestion = questionsList.get(qid);
                //Now this method will set the new que and 4 options
                updateQueueAndOptions();
                //reset the color of buttons back to white
                resetColor();
                //Enable button - remember we had disable them when user ans was correct in there particular button methods
                enableButton();
            }
        });

    }

    //This method will make button color white again since our one button color was turned green
    public void resetColor() {
        buttonA.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonB.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonC.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonD.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
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
