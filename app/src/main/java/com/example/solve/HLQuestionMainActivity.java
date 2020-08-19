package com.example.solve;

import android.content.Intent;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class HLQuestionMainActivity extends AppCompatActivity {

    HLQuestion placeHolder=new HLQuestion(1,"Object there was none. Passion there was none. I loved the old man. He had never wronged me. ", "which sentence supports the statement below?","because","why","Reading",-1);
    View loadingScreen;

    Typeface tb;

    View questionPicLayout;
    TextView questionPicText;
    ImageView questionPic;
    TextView questionText;

    View textAnswersLayout;
    View picAnswersLayout;
    Button buttonA, buttonB, buttonC, buttonD, buttonE, buttonF, buttonSubmit;
    ImageView optAPic, optBPic, optCPic, optDPic;
    ImageView explanationPic;

    HLQuestion currentQuestion;
    UserData currentUser;
    Topic currentTopic;
    List<HLQuestion> questionsList;
    List<AnsweredQuestionData> answeredQuestionList;
    int qid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //------------------------------------------------------------------view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hlquestion_activity_main);
        Intent intent = getIntent();
        currentTopic = (Topic) intent.getSerializableExtra("TOPIC");
        //Toast.makeText(this,topic, Toast.LENGTH_LONG ).show();

        //Initializing variables

        loadingScreen = findViewById(R.id.loading_screen);
        loadingScreen.setVisibility(View.VISIBLE);

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
        buttonA = findViewById(R.id.abutton);
        buttonB = findViewById(R.id.bbutton);
        buttonC = findViewById(R.id.cbutton);
        buttonD = findViewById(R.id.dbutton);
        buttonE = findViewById(R.id.ebutton);
        buttonF = findViewById(R.id.fbutton);
        buttonSubmit= findViewById(R.id.submitButton);

        tb = Typeface.createFromAsset(getAssets(), "fonts/karla.ttf");

        //Setting typefaces for textview and buttons
        questionPicText.setTypeface(tb);
        questionText.setTypeface(tb);
        buttonA.setTypeface(tb);
        buttonB.setTypeface(tb);
        buttonC.setTypeface(tb);
        buttonD.setTypeface(tb);
        buttonE.setTypeface(tb);
        buttonF.setTypeface(tb);
        buttonSubmit.setTypeface(tb);
        resetColor();

        getFirebaseQuestionsList(currentTopic);
        /*if(currentUser != null)
            getPerUserFirebaseQuestionsList(currentUser.getId());//not reached

         */
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

    private void savePerUserFirebaseQuestionsList() {
        //get current userID, if userID is empty, then don't save anything
        if(currentUser != null && currentUser.getId() != null) {
            DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference().child("UserData").child("Questions_History").child(currentUser.getId());
            if(qListRef != null)
            {
                qListRef.setValue(answeredQuestionList);
            }
        }
    }

    private void getFirebaseQuestionsList(Topic topic){
        DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference()
                .child(topic.getQuestionFolderName());
        qListRef.addValueEventListener(new ValueEventListener() {//This retrieves the data once
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionsList = dataSnapshot.getValue(new GenericTypeIndicator<List<HLQuestion>>() {});//stops here Failed to convert value of type java.lang.Long to String
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
            }//DatabaseError: Permission denied
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
                Log.e("FB getUserData", "onCancelled with "+databaseError.getMessage()+", details: "+databaseError.getDetails());
            }
        });
    }

    public void updateQueueAndOptions() {
        //sets visibility of layout according to what pictures are in the question
        //question has text even if it has pic
        boolean hasQPic = (((HLQuestion)currentQuestion).getPicNumber() > -1);
        boolean hasAPics = (((HLQuestion)currentQuestion).getOptAPicNumber() > -1 || ((HLQuestion)currentQuestion).getOptBPicNumber() > -1 || ((HLQuestion)currentQuestion).getOptCPicNumber() > -1 || ((HLQuestion)currentQuestion).getOptDPicNumber() > -1);
        if(!hasQPic && !hasAPics){ //text only
            //Q
            questionText.setText(currentQuestion.getQuestion());
            questionText.setVisibility(View.VISIBLE);
            questionPicLayout.setVisibility(GONE);
            //A
            picAnswersLayout.setVisibility(GONE);
            textAnswersLayout.setVisibility(View.VISIBLE);
            buttonA.setText(((HLQuestion)currentQuestion).getOptA());
            buttonB.setText(((HLQuestion)currentQuestion).getOptB());
            buttonC.setText(((HLQuestion)currentQuestion).getOptC());
            buttonD.setText(((HLQuestion)currentQuestion).getOptD());
        }else if(hasQPic && !hasAPics){ //pic question, text answer
            //Q
            questionText.setVisibility(GONE);
            questionPicLayout.setVisibility(View.VISIBLE);
            questionPicText.setText(currentQuestion.getQuestion());
            loadQuestionPic(currentTopic, ((HLQuestion)currentQuestion).getPicNumber());
            //A
            picAnswersLayout.setVisibility(GONE);
            textAnswersLayout.setVisibility(View.VISIBLE);
            buttonA.setText(((HLQuestion)currentQuestion).getOptA());
            buttonB.setText(((HLQuestion)currentQuestion).getOptB());
            buttonC.setText(((HLQuestion)currentQuestion).getOptC());
            buttonD.setText(((HLQuestion)currentQuestion).getOptD());
            buttonE.setText(((HLQuestion)currentQuestion).getOptE());
            buttonF.setText(((HLQuestion)currentQuestion).getOptF());
            buttonF.setVisibility(View.VISIBLE);
            buttonE.setVisibility(View.VISIBLE);
            if(((HLQuestion)currentQuestion).getOptF().equals(null)){
                buttonF.setVisibility(View.INVISIBLE);
                buttonF.setEnabled(false);
                if(((HLQuestion)currentQuestion).getOptE().equals(null)){
                    buttonE.setVisibility(View.INVISIBLE);
                    buttonE.setEnabled(false);
                }
            }

        }else if(!hasQPic && hasAPics){ //text question, pic answer
            //Q
            questionText.setText(currentQuestion.getQuestion());
            questionText.setVisibility(View.VISIBLE);
            questionPicLayout.setVisibility(GONE);
            //A
            picAnswersLayout.setVisibility(View.VISIBLE);
            textAnswersLayout.setVisibility(GONE);
            loadAnswerPics(currentTopic, ((HLQuestion)currentQuestion).getOptAPicNumber(), ((HLQuestion)currentQuestion).getOptBPicNumber(), ((HLQuestion)currentQuestion).getOptCPicNumber(), ((HLQuestion)currentQuestion).getOptDPicNumber());
        }else{ //all pictures
            //Q
            questionText.setVisibility(GONE);
            questionPicLayout.setVisibility(View.VISIBLE);
            questionPicText.setText(currentQuestion.getQuestion());
            loadQuestionPic(currentTopic, ((HLQuestion)currentQuestion).getPicNumber());
            //A
            picAnswersLayout.setVisibility(View.VISIBLE);
            textAnswersLayout.setVisibility(GONE);
            loadAnswerPics(currentTopic, ((HLQuestion)currentQuestion).getOptAPicNumber(), ((HLQuestion)currentQuestion).getOptBPicNumber(), ((HLQuestion)currentQuestion).getOptCPicNumber(), ((HLQuestion)currentQuestion).getOptDPicNumber());
        }

    }

    private void saveHistory(int questionID, String answerChosen, Question currentQuestion) {
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

    private void loadQuestionPic(Topic topic, int questionPicID){
        if(questionPicID < 0)return;
        StorageReference qImageRef = FirebaseStorage.getInstance().getReference()   //but what if it doesn't exist?
                .child(topic.getPicRootFolderName())
                .child("Question_Pics")
                .child(topic.getPicNamePrefix()+"_q_"+questionPicID+".PNG");

        qImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    Glide.with(HLQuestionMainActivity.this)
                            .load(task.getResult())
                            .into(questionPic);
                }
                else {
                    Toast.makeText(HLQuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadExplanationPic(Topic topic, int explanationPicID){
        if(explanationPicID < 0)return;
        StorageReference eImageRef = FirebaseStorage.getInstance().getReference()   //but what if it doesn't exist?
                .child(topic.getPicRootFolderName())
                .child("Explanation_Pics")
                .child(topic.getPicNamePrefix()+"_e_"+explanationPicID+".PNG");

        eImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    Glide.with(HLQuestionMainActivity.this)
                            .load(task.getResult())
                            .into(explanationPic);
                }
                else {
                    Toast.makeText(HLQuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadAnswerPics(Topic topic, int optAID, int optBID, int optCID, int optDID){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if(optAID > -1){
            StorageReference optAImageRef = storageReference
                    .child(topic.getPicRootFolderName())
                    .child("Answer_Pics")
                    .child(topic.getPicNamePrefix()+"_a_"+optAID+".PNG");   //but what if it doesn't exist?
            optAImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Glide.with(HLQuestionMainActivity.this)
                                .load(task.getResult())
                                .into(optAPic);
                    }
                    else {
                        Toast.makeText(HLQuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(optBID > -1){
            StorageReference optBImageRef = storageReference
                    .child(topic.getPicRootFolderName())
                    .child("Answer_Pics")
                    .child(topic.getPicNamePrefix()+"_a_"+optBID+".PNG");   //but what if it doesn't exist?
            optBImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Glide.with(HLQuestionMainActivity.this)
                                .load(task.getResult())
                                .into(optBPic);
                    }
                    else {
                        Toast.makeText(HLQuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(optCID > -1){
            StorageReference optCImageRef = storageReference
                    .child(topic.getPicRootFolderName())
                    .child("Answer_Pics")
                    .child(topic.getPicNamePrefix()+"_a_"+optCID+".PNG");   //but what if it doesn't exist?
            optCImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Glide.with(HLQuestionMainActivity.this)
                                .load(task.getResult())
                                .into(optCPic);
                    }
                    else {
                        Toast.makeText(HLQuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(optDID > -1){
            StorageReference optDImageRef = storageReference
                    .child(topic.getPicRootFolderName())
                    .child("Answer_Pics")
                    .child(topic.getPicNamePrefix()+"_a_"+optDID+".PNG");   //but what if it doesn't exist?
            optDImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Glide.with(HLQuestionMainActivity.this)
                                .load(task.getResult())
                                .into(optDPic);
                    }
                    else {
                        Toast.makeText(HLQuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    //--------------------------------------------------------UI stuff---------------------------------------------

    //Onclick listener for first button
    /*public void buttonA(View view) {
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
    }*/
    public void buttonSubmit(View view)
    {
        if(!buttonA.isEnabled()) {
            buttonA.setEnabled(true);
            saveHistory(qid, "Option A", currentQuestion);
            if (currentQuestion.getAnswer().equalsIgnoreCase("Option A") ) {
                //((FButton)buttonA).setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
                //TODO: when on last question and success, the success dialog does not show
                buttonA.setBackgroundColor(Color.GREEN);
                disableButton();
                correctDialog();
            }
            //User ans is wrong then just navigate him to the PlayAgain activity
            else {
                incorrectDialog();
            }
        }
        else if(!buttonB.isEnabled()){
            buttonB.setEnabled(true);
            saveHistory(qid, "Option B", currentQuestion);
            if (currentQuestion.getAnswer().equalsIgnoreCase("Option B")) {
                //((FButton)buttonB).setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
                //TODO: when on last question and success, the success dialog does not show
                buttonB.setBackgroundColor(Color.GREEN);
                disableButton();
                correctDialog();
            } else { // in place of PlayAgain activity to be implemented later? test to pull up incorrect dialog
                incorrectDialog();
            }
        }
        if(!buttonC.isEnabled()){
            buttonC.setEnabled(true);
            saveHistory(qid, "Option C", currentQuestion);
            if (currentQuestion.getAnswer().equalsIgnoreCase("Option C") ) {
               // ((FButton)buttonC).setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
                //TODO: when on last question and success, the success dialog does not show
                buttonC.setBackgroundColor(Color.GREEN);
                disableButton();
                correctDialog();
            } else {
                incorrectDialog();
            }
        }
        else if(buttonD.isEnabled()){
            buttonD.setEnabled(true);
            saveHistory(qid, "Option D", currentQuestion);
            if (currentQuestion.getAnswer().equalsIgnoreCase("Option D")) {
                //((FButton)buttonD).setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
                //TODO: when on last question and success, the success dialog does not show
                buttonD.setBackgroundColor(Color.GREEN);
                disableButton();
                correctDialog();
            } else {
                incorrectDialog();
            }
        }

    }
    public void buttonA()
    {
        enableButton();
        buttonA.setEnabled(false);
    }
    public void buttonB()
    {
        enableButton();
        buttonB.setEnabled(false);
    }
    public void buttonC()
    {
        enableButton();
        buttonC.setEnabled(false);
    }
    public void buttonD()
    {
        enableButton();
        buttonD.setEnabled(false);

    }
    //Onclick listener for sec button
    /*public void buttonB(View view) {
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
    }*/

    //This dialog is show to the user after he ans correct
    public void correctDialog() {
        final Dialog dialogCorrect = new Dialog(HLQuestionMainActivity.this);
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
        final Dialog dialogComplete = new Dialog(HLQuestionMainActivity.this);
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
        final Dialog dialogIncorrect = new Dialog(HLQuestionMainActivity.this);
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
        ((FButton)buttonA).setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        ((FButton)buttonB).setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        ((FButton)buttonC).setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        ((FButton)buttonD).setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
    }

    //This method will disable all the option button
    public void disableButton() {
        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);
        buttonE.setEnabled(false);
        buttonF.setEnabled(false);
    }

    //This method will all enable the option buttons
    public void enableButton() {
        buttonA.setEnabled(true);
        buttonB.setEnabled(true);
        buttonC.setEnabled(true);
        buttonD.setEnabled(true);
        buttonE.setEnabled(true);
        buttonF.setEnabled(true);
    }
}
