package com.example.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import info.hoang8f.widget.FButton;

public class QuestionMainActivity extends AppCompatActivity {

    FButton buttonA, buttonB, buttonC, buttonD;
    TextView question;
    Typeface tb;

    Questions currentQuestion;
    QuestionsHelper questionsHelper;

    List<Questions> list;
    int qid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity_main);

        //Initializing variables
        buttonA = (FButton) findViewById(R.id.buttonA);
        buttonB = (FButton) findViewById(R.id.buttonB);
        buttonC = (FButton) findViewById(R.id.buttonC);
        buttonD = (FButton) findViewById(R.id.buttonD);
        question = (TextView) findViewById(R.id.question);
        tb = Typeface.createFromAsset(getAssets(), "fonts/karla.ttf");

        //Setting typefaces for textview and buttons
        question.setTypeface(tb);
        buttonA.setTypeface(tb);
        buttonB.setTypeface(tb);
        buttonC.setTypeface(tb);
        buttonD.setTypeface(tb);

        resetColor();
        //Our database helper class
        questionsHelper = new QuestionsHelper(this);
        //Make db writable
        questionsHelper.getWritableDatabase();

        //Checks if the question options are already added in the table or not
        //If they are not added, getAllOfTheQuestions() will return a list of size zero
        if (questionsHelper.getAllOfTheQuestions().size() == 0) {
            //If not added then add the ques,options in table
            questionsHelper.allQuestion();
        }

        //This will return us a list of data type TriviaQuestion
        list = questionsHelper.getAllOfTheQuestions();

        //Now we gonna shuffle the elements of the list so that we will get questions randomly
        Collections.shuffle(list);

        //currentQuestion will hold the que, 4 option and ans for particular id
        currentQuestion = list.get(qid);

        updateQueueAndOptions();

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
            if (qid < list.size() - 1) {

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
            //Incorrect dialogue
        }
    }

    //Onclick listener for sec button
    public void buttonB(View view) {
        if (currentQuestion.getOptB().equals(currentQuestion.getAnswer())) {
            buttonB.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            if (qid < list.size() - 1) {
                disableButton();
                correctDialog();
            } else {

            }
        } else { // in place of PlayAgain activity to be implemented later? test to pull up incorrect dialog
            incorrectDialog();
            //Incorrect dialogue
        }
    }

    //Onclick listener for third button
    public void buttonC(View view) {
        if (currentQuestion.getOptC().equals(currentQuestion.getAnswer())) {
            buttonC.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            if (qid < list.size() - 1) {
                disableButton();
                correctDialog();
            } else {

            }
        } else {
            incorrectDialog();
            //Incorrect dialogue
        }
    }

    //Onclick listener for fourth button
    public void buttonD(View view) {
        if (currentQuestion.getOptD().equals(currentQuestion.getAnswer())) {
            buttonD.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            if (qid < list.size() - 1) {
                disableButton();
                correctDialog();
            } else {

            }
        } else {
            incorrectDialog();
            //IncorrectDialogue
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
                currentQuestion = list.get(qid);
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


        TextView correctText = (TextView) dialogIncorrect.findViewById(R.id.incorrectText);
        FButton buttonNext = (FButton) dialogIncorrect.findViewById(R.id.dialogNext);
        FButton buttonTryAgain = (FButton) dialogIncorrect.findViewById(R.id.dialogTryAgain);

        //Setting type faces
        correctText.setTypeface(tb);
        buttonNext.setTypeface(tb);
        buttonTryAgain.setTypeface(tb);

        //OnCLick listener to go next que
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This will dismiss the dialog
                dialogIncorrect.dismiss();
                //it will increment the question number
                qid++;
                //get the que and 4 option and store in the currentQuestion
                currentQuestion = list.get(qid);
                //Now this method will set the new que and 4 options
                updateQueueAndOptions();
                //reset the color of buttons back to white
                resetColor();
                //Enable button - remember we had disable them when user ans was correct in there particular button methods
                enableButton();
            }
        });

        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This will dismiss the dialog
                dialogIncorrect.dismiss();
                //it will increment the question number
                //qid++;
                //get the que and 4 option and store in the currentQuestion
                //currentQuestion = list.get(qid);
                //Now this method will set the new que and 4 options
                //updateQueueAndOptions();
                //reset the color of buttons back to white
                //resetColor();
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

