package com.example.solve;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DrawableUtils;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import info.hoang8f.widget.FButton;

import static android.view.View.GONE;

public class QuestionMainActivity extends AppCompatActivity {

    FButton buttonA, buttonB, buttonC, buttonD;
    View loadingScreen;
    TextView question;
    Typeface tb;

    View questionPicLayout;
    TextView questionPicText;
    ImageView questionPic;

    Questions currentQuestion;
    UserData currentUser;
    QuestionsHelper questionsHelper;

    List<Questions> questionsList;
    List<AnsweredQuestionData> answeredQuestionList;
    int qid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //------------------------------------------------------------------view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity_main);
        Intent intent = getIntent();
        String topic = intent.getStringExtra("TOPIC");//If no intent, string is empty (no try/catch needed)
        //Toast.makeText(this,topic, Toast.LENGTH_LONG ).show();
        //Initializing variables
        buttonA = (FButton) findViewById(R.id.buttonA);
        buttonB = (FButton) findViewById(R.id.buttonB);
        buttonC = (FButton) findViewById(R.id.buttonC);
        buttonD = (FButton) findViewById(R.id.buttonD);
        loadingScreen = findViewById(R.id.loading_screen);
        loadingScreen.setVisibility(View.VISIBLE);
        question = (TextView) findViewById(R.id.question_text);

        questionPicLayout = findViewById(R.id.question_pic_layout);
        questionPicText = findViewById(R.id.question_pic_text);
        questionPic = findViewById(R.id.question_picture);

        tb = Typeface.createFromAsset(getAssets(), "fonts/karla.ttf");

        //Setting typefaces for textview and buttons
        questionPicText.setTypeface(tb);
        question.setTypeface(tb);
        buttonA.setTypeface(tb);
        buttonB.setTypeface(tb);
        buttonC.setTypeface(tb);
        buttonD.setTypeface(tb);
        resetColor();

        getFirebaseQuestionsList(topic);
        if(currentUser != null)
            getPerUserFirebaseQuestionsList(currentUser.getId());//not reached
    }

    private void getPerUserFirebaseQuestionsList(String userID){//TODO: find path relative to topic (switch statement)
        DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference().child("UserData").child("Questions_History").child(userID);//that goes nowhere!

        qListRef.addValueEventListener(new ValueEventListener() {//This retrieves the data once
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                answeredQuestionList = dataSnapshot.getValue(new GenericTypeIndicator<List<AnsweredQuestionData>>() {});//stops here Failed to convert value of type java.lang.Long to String
                Log.i("get user data", "Firebase data fetched");
                //currentQuestion will hold the que, 4 option and ans for particular id
                loadingScreen.setVisibility(GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO: handle network outage
                Log.e("FB getList", "onCancelled with "+databaseError.getMessage()+", details: "+databaseError.getDetails());
            }
        });
    }

    private void savePerUserFirebaseQuestionsList()
    {
        //get current userID, if userID is empty, then don't save anything
        if(currentUser != null && currentUser.getId() != null) {
            DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference().child("UserData").child("Questions_History").child(currentUser.getId());
            if(qListRef != null)
            {
                qListRef.setValue(answeredQuestionList);
            }
        }
    }

    private void getFirebaseQuestionsList(String topic){//TODO: find path relative to topic (switch statement)
        DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference().child("Questions").child("Number_Number_Sense");
        qListRef.addValueEventListener(new ValueEventListener() {//This retrieves the data once
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                questionsList = dataSnapshot.getValue(new GenericTypeIndicator<List<Questions>>() {});//stops here Failed to convert value of type java.lang.Long to String
                Log.i("FB getList", "Firebase data fetched");
                Collections.shuffle(questionsList);
                //currentQuestion will hold the que, 4 option and ans for particular id
                currentQuestion = questionsList.get(qid);
                updateQueueAndOptions();
                loadingScreen.setVisibility(GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO: handle network outage
                Log.e("FB getList", "onCancelled with "+databaseError.getMessage()+", details: "+databaseError.getDetails());
            }
        });
    }

    //Change this?
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
                Log.e("FB getUserData", "onCancelled with "+databaseError.getMessage()+", details: "+databaseError.getDetails());
            }
        });
    }

    public void updateQueueAndOptions() {
        if(currentQuestion.getPicNumber() != -1){//question has text and picture: current question is null
            question.setVisibility(GONE);
            questionPicLayout.setVisibility(View.VISIBLE);
            questionPicText.setText(currentQuestion.getQuestion());
            StorageReference storage = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storage.child("Number_NumberSense").child("number_sense_q"+currentQuestion.getPicNumber()+".PNG");
            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Glide.with(QuestionMainActivity.this)
                                .load(task.getResult())
                                .into(questionPic);
                    }
                    else {
                        Toast.makeText(QuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Log.d("Glide image loading","Done loading image");

        }else{//question only has text
            question.setText(currentQuestion.getQuestion());
            question.setVisibility(View.VISIBLE);
            questionPicLayout.setVisibility(GONE);
        }
        //This method will setText for que and options
        buttonA.setText(currentQuestion.getOptA());
        buttonB.setText(currentQuestion.getOptB());
        buttonC.setText(currentQuestion.getOptC());
        buttonD.setText(currentQuestion.getOptD());
    }

    private void saveHistory(int questionID, String answerChosen, Questions currentQuestion) {
        if(answeredQuestionList != null)
        {
            Iterator<AnsweredQuestionData> iterator = answeredQuestionList.iterator();
            AnsweredQuestionData currentAnsweredQuestion = null;
            while (iterator.hasNext()) {
                AnsweredQuestionData question = iterator.next();
                if (question.getQuestion().getId() == questionID) {
                    currentAnsweredQuestion = question;
                    break;
                }
            }
            if(currentAnsweredQuestion == null)
            {
                currentAnsweredQuestion = new AnsweredQuestionData(currentQuestion, answerChosen);
                answeredQuestionList.add(currentAnsweredQuestion);
            }
            else {
                currentAnsweredQuestion.setAnswer(answerChosen);
            }
            savePerUserFirebaseQuestionsList();

        }
    }

    //Onclick listener for first button
    public void buttonA(View view) {
        //compare the option with the ans if yes then make button color green
        saveHistory(qid, "Option A", currentQuestion);
        if (currentQuestion.getAnswer().equalsIgnoreCase("Option A") ) {
            buttonA.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            //TODO: when on last question and success, the success dialog does not show
            disableButton();
            correctDialog();
        }
        //User ans is wrong then just navigate him to the PlayAgain activity
        else {
            incorrectDialog();
        }
    }

    //Onclick listener for sec button
    public void buttonB(View view) {
        saveHistory(qid, "Option B", currentQuestion);
        if (currentQuestion.getAnswer().equalsIgnoreCase("Option B")) {
            buttonB.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            //TODO: when on last question and success, the success dialog does not show
            disableButton();
            correctDialog();
        } else { // in place of PlayAgain activity to be implemented later? test to pull up incorrect dialog
            incorrectDialog();
        }
    }


    //Onclick listener for third button
    public void buttonC(View view) {
        saveHistory(qid, "Option C", currentQuestion);
        if (currentQuestion.getAnswer().equalsIgnoreCase("Option C") ) {
            buttonC.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            //TODO: when on last question and success, the success dialog does not show
            disableButton();
            correctDialog();
        } else {
            incorrectDialog();
        }
    }

    //Onclick listener for fourth button
    public void buttonD(View view) {
        saveHistory(qid, "Option D", currentQuestion);
        if (currentQuestion.getAnswer().equalsIgnoreCase("Option D")) {
            buttonD.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            //TODO: when on last question and success, the success dialog does not show
            disableButton();
            correctDialog();
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
                if(qid >= questionsList.size())
                {
                    congratsDialog();
                }
                else
                {
                    //get the que and 4 option and store in the currentQuestion
                    currentQuestion = questionsList.get(qid);
                    //Now this method will set the new que and 4 options
                    updateQueueAndOptions();
                    //reset the color of buttons back to white
                    resetColor();
                    //Enable button - remember we had disable them when user ans was correct in there particular button methods
                    enableButton();
                }

            }
        });
    }

    public void congratsDialog() {
        final Dialog dialogComplete = new Dialog(QuestionMainActivity.this);
        dialogComplete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialogComplete.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialogComplete.getWindow().setBackgroundDrawable(colorDrawable);
        }
        dialogComplete.setContentView(R.layout.dialog_completion);
        dialogComplete.setCancelable(false);
        dialogComplete.show();

        //Since the dialog is show to user just pause the timer in background
        onPause();


        TextView correctText = (TextView) dialogComplete.findViewById(R.id.congratsText);
        FButton buttonHome = (FButton) dialogComplete.findViewById(R.id.dialogNext);

        //Setting type faces
        correctText.setTypeface(tb);
        buttonHome.setTypeface(tb);

        //OnCLick listener to go next que
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This will dismiss the dialog
                dialogComplete.dismiss();
                //go home
                setContentView(R.layout.angela_activity_main);
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, homeFragment);
                ft.commit();

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
                if(qid >= questionsList.size())
                {
                    congratsDialog();

                }
                else
                {
                    currentQuestion = questionsList.get(qid);
                    //Now this method will set the new que and 4 options
                    updateQueueAndOptions();
                    //reset the color of buttons back to white
                    resetColor();
                    //Enable button - remember we had disable them when user ans was correct in there particular button methods
                    enableButton();
                }

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
