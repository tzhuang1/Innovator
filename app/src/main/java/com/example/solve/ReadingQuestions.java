package com.example.solve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import info.hoang8f.widget.FButton;

import static android.view.View.GONE;
import static com.example.solve.R.id.btnA;

public class ReadingQuestions extends AppCompatActivity {

    private View loadingScreen;

    private Typeface tb;

    private View scrollViewPassage;
    private TextView questionText;
    private TextView passageText;

    private Button buttonA, buttonB, buttonC, buttonD;

    //ImageView explanationPic;    TODO: ask CD if there are explanation pics

    private Question currentQuestion;
    private UserData currentUser;
    private Topic currentTopic;
    private List<Question> questionsList;
    private List<AnsweredQuestionData> answeredQuestionList;
    private int qid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_questions);
        Intent intent = getIntent();
        currentTopic = (Topic) intent.getSerializableExtra("TOPIC");

        //initializing variables
        loadingScreen = findViewById(R.id.loading_screen);
        loadingScreen.setVisibility(View.VISIBLE);

        scrollViewPassage = findViewById(R.id.scrollView2);
        passageText = findViewById(R.id.txtPassage);
        questionText = findViewById(R.id.textView2);
        buttonA = findViewById(R.id.btnA);
        buttonB = findViewById(R.id.btnB);
        buttonC = findViewById(R.id.btnC);
        buttonD = findViewById(R.id.btnD);

        tb = Typeface.createFromAsset(getAssets(), "fonts/karla.ttf");

        //Setting typefaces for textview and buttons
        questionText.setTypeface(tb);
        buttonA.setTypeface(tb);
        buttonB.setTypeface(tb);
        buttonC.setTypeface(tb);
        buttonD.setTypeface(tb);
        resetColor();

        //TODO: From database get all values

        getFirebaseQuestionsList(currentTopic);
    }

    private void getFirebaseQuestionsList(Topic topic){
        DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference()
                .child("Reading")
                .child(topic.getQuestionFolderName());
        qListRef.addValueEventListener(new ValueEventListener() {//This retrieves the data once
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object myData = dataSnapshot.getValue();
                questionsList = new ArrayList<Question>();
                List<HashMap<Object, Object>> listOfQuestions = (List<HashMap<Object, Object>>)dataSnapshot.getValue();
                for(int i = 0; i < listOfQuestions.size(); i++) {
                    HashMap<Object, Object> entry = listOfQuestions.get(i);
                    try{
                        if(entry != null) {
                            //String question, String opta, String optb, String optc, String optd, String answer, String explanation, String category, int picNumber, int exPicNumber
                            Question newQuestion = new Question(entry.get("question").toString(), entry.get("optA").toString(), entry.get("optB").toString(), entry.get("optC").toString(), entry.get("optD").toString(),
                                    entry.get("answer").toString(), entry.get("explanation").toString(), entry.get("category").toString(), Integer.parseInt(entry.get("questionPicNumber").toString()), Integer.parseInt(entry.get("explanationPicNumber").toString()));
                            newQuestion.setId(i);
                            questionsList.add(newQuestion);
                        }
                    }
                    catch (Exception ex) {
                        Log.e("LoadQuestion", ex.toString());
                    }
                }

                //questionsList = dataSnapshot.getValue(new GenericTypeIndicator<List<Question>>() {});//stops here Failed to convert value of type java.lang.Long to String
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

    public void updateQueueAndOptions() {
        //sets visibility of layout according to what pictures are in the question
        //question has text even if it has pic
        boolean hasQPic = (currentQuestion.getPicNumber() > -1);
        boolean hasAPics = (currentQuestion.getOptAPicNumber() > -1 || currentQuestion.getOptBPicNumber() > -1 || currentQuestion.getOptCPicNumber() > -1 || currentQuestion.getOptDPicNumber() > -1);
        if(!hasQPic && !hasAPics){ //text only
            //Q
            questionText.setText(currentQuestion.getQuestion());
            questionText.setVisibility(View.VISIBLE);
            //A
            buttonA.setText(currentQuestion.getOptA());
            buttonB.setText(currentQuestion.getOptB());
            buttonC.setText(currentQuestion.getOptC());
            buttonD.setText(currentQuestion.getOptD());
        }else if(hasQPic && !hasAPics){ //pic question, text answer
            //Q
            questionText.setVisibility(GONE);
            questionText.setText(currentQuestion.getQuestion());
            //A
            buttonA.setText(currentQuestion.getOptA());
            buttonB.setText(currentQuestion.getOptB());
            buttonC.setText(currentQuestion.getOptC());
            buttonD.setText(currentQuestion.getOptD());
        }else if(!hasQPic && hasAPics){ //text question, pic answer
            //Q
            questionText.setText(currentQuestion.getQuestion());
            questionText.setVisibility(View.VISIBLE);
            //A
        }else{ //all pictures
            //Q
            questionText.setVisibility(GONE);
            //A
        }

    }
    private void resetColor() {
        buttonA.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonB.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonC.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonD.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
    }
}
