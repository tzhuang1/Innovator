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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import info.hoang8f.widget.FButton;

import static android.view.View.GONE;

import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

public class StatsMainActivity extends AppCompatActivity {


    View loadingScreen;

    Typeface tb;

    View questionPicLayout;
    TextView questionPicText;
    ImageView questionPic;
    TextView questionText;

    View textAnswersLayout;
    View picAnswersLayout;
    FButton buttonA, buttonB, buttonC, buttonD;

    UserData currentUser;
    Topic currentTopic;
    List<Question> questionsList;
    List<AnsweredQuestionData> answeredQuestionList;
    int qid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //------------------------------------------------------------------view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.satistics_page);
        Intent intent = getIntent();
        currentTopic = (Topic) intent.getSerializableExtra("TOPIC");
        //Toast.makeText(this,topic, Toast.LENGTH_LONG ).show();
        //getFirebaseUserData();
        //Initializing variables

        loadingScreen = findViewById(R.id.loading_screen);
        loadingScreen.setVisibility(View.VISIBLE);


        tb = Typeface.createFromAsset(getAssets(), "fonts/karla.ttf");
        Button button = (Button) findVeiwById(R.id.returnHome);
        button.setOnClickListener(new View.OnClickListener()) {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(StatsMainActivity.this, QuestionMainActivity.class);
                startActivity(intent)
            }
        }
        drawPie();
        //Setting typefaces for textview and buttons


        //getFirebaseQuestionsList(currentTopic);
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

    /* private void getFirebaseQuestionsList(Topic topic){
         DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference()
                 .child(topic.getQuestionFolderName());
         qListRef.addValueEventListener(new ValueEventListener() {//This retrieves the data once
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 questionsList = dataSnapshot.getValue(new GenericTypeIndicator<List<Question>>() {});//stops here Failed to convert value of type java.lang.Long to String
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
     }*/
    public void drawPie() {
        AnimatedPieView mAnimatedPieView = findViewById(R.id.animatedPieView);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90)// Starting angle offset
                .addData(new SimplePieInfo((float) (currentUser.getMissedAddSub()), Color.parseColor("BLACK"), "Addition and Subtraction"))//Data (bean that implements the IPieInfo interface)
                .addData(new SimplePieInfo((float) (currentUser.getMissedMeasGeo()), Color.parseColor("MAGENTA"), "Measurement and Geometry"))
                .addData(new SimplePieInfo((float) (currentUser.getMissedNumSense()), Color.parseColor("RED"), "Number and Number Sense"))
                .addData(new SimplePieInfo((float) (currentUser.getMissedPatFuncAlg()), Color.parseColor("BLUE"), "Patterns, Functions, and Algebra"))
                .addData(new SimplePieInfo((float) (currentUser.getMissedProbStat()), Color.parseColor("GREEN"), "Probability and Statistics")).drawText(true).textSize(30)
                .duration(2000);


        private void getFirebaseUserData () {
            DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference("UserData");
            qListRef.addValueEventListener(new ValueEventListener() {//This retrieves the data once
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<UserData> type = new GenericTypeIndicator<UserData>() {
                    };
                    currentUser = dataSnapshot.getValue(type); //DatabaseException: Class java.util.List has generic type parameters, please use GenericTypeIndicator instead
                    Log.i("Get User Data", "Firebase data fetched");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FB getUserData", "onCancelled with " + databaseError.getMessage() + ", details: " + databaseError.getDetails());
                }
            });
        }

   /* public void updateQueueAndOptions() {
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

    }*/

        private void saveHistory ( int questionID, String answerChosen, Question currentQuestion){
            if (answeredQuestionList != null) {
                Iterator<AnsweredQuestionData> iterator = answeredQuestionList.iterator();
                AnsweredQuestionData currentAnsweredQuestion = null;
                while (iterator.hasNext()) {
                    AnsweredQuestionData question = iterator.next();
                    if (question.getQuestion().getId() == questionID) {
                        currentAnsweredQuestion = question;
                        break;
                    }
                }
                if (currentAnsweredQuestion == null) {
                    currentAnsweredQuestion = new AnsweredQuestionData(currentQuestion, answerChosen);
                    answeredQuestionList.add(currentAnsweredQuestion);
                } else {
                    currentAnsweredQuestion.setAnswer(answerChosen);
                }
                savePerUserFirebaseQuestionsList();

            }
        }


        //--------------------------------------------------------UI stuff---------------------------------------------

        //Onclick listener for first button
    }
}
