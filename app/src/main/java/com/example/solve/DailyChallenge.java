package com.example.solve;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class DailyChallenge extends AppCompatActivity{

    private int minMathQuestionsCount=80;
    private int minReadingQuestionsCount=65;

    private String challengeGradeLevel;
    private String category;

    // Firebase Objects
    private FirebaseAuth auth;
    private DatabaseReference userDatabase;

    private int questionIndex=-1;

    private Map<String, Object> currentQuestionData;

    private String mathCategory;
    private String readingCategory;

    private String[] mathCategories={"Computation and Estimation", "Measurement and Geometry","Numbers and Number Sense","Patterns, Functions, Algebra", "Probability and Statistics"};
    private String[] readingCategories={"Reading Comprehension", "Word Analysis"};


    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.daily_challenge_options);

        auth=FirebaseAuth.getInstance();
        userDatabase= FirebaseDatabase.getInstance().getReference();

        mathCategory=mathCategories[new Random().nextInt(mathCategories.length)];
        readingCategory=readingCategories[new Random().nextInt(readingCategories.length)];
    }

    public void returnToHome(View view){
        startActivity(new Intent(this, MainMenuController.class));
    }

    public void onChallengeClick(View view) {
        String completeStr =view.getResources().getResourceEntryName(view.getId());
        String gradeLevel=completeStr.substring(completeStr.length()-1);
        String categoryStr=completeStr.substring(0, completeStr.length()-1);

        TopicManager.setDataLocations(gradeLevel);

        category=categoryStr;
        challengeGradeLevel=gradeLevel;

        populateQuestionMap();
    }



    public void loadChallenge(){
        setContentView(R.layout.daily_challenge);

        TextView questionDisplay=findViewById(R.id.questionDisplay);
        Button choiceA=findViewById(R.id.answerA);
        Button choiceB=findViewById(R.id.answerB);
        Button choiceC=findViewById(R.id.answerC);
        Button choiceD=findViewById(R.id.answerD);

        currentQuestionData=DailyChallengeManager.getCurrentQuestionData();

        questionDisplay.setText("Question: "+(String)currentQuestionData.get("question"));
        choiceA.setText("A. "+(String)currentQuestionData.get("optA"));
        choiceB.setText("B. "+(String)currentQuestionData.get("optB"));
        choiceC.setText("C. "+(String)currentQuestionData.get("optC"));
        choiceD.setText("D. "+(String)currentQuestionData.get("optD"));

        if(category.equals("Reading")){
            if(DailyChallengeManager.getCurrentQuestionData().get("passage")!=null){
                TextView passageText=findViewById(R.id.passageText);
                passageText.setText((String)DailyChallengeManager.getCurrentQuestionData().get("passage"));
            }
        }

    }


    private int generateQuestionIndex(){
        Random rand=new Random();
        int questionIndex=rand.nextInt(15)+1;
        return questionIndex;

    }


    private void populateQuestionMap(){

        Calendar calendar=Calendar.getInstance();
        int minutes=calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR);
        int AMvalue=calendar.get(Calendar.AM);

        if(minutes==0&&hour==12&&AMvalue==1){
            questionIndex=generateQuestionIndex();
        }
        if(questionIndex==-1){
            questionIndex=generateQuestionIndex();
        }

        if(category.equals("Reading")){
            userDatabase.child(category).child("Grade_"+challengeGradeLevel).child(readingCategory).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        List<Object> questionsList= (List<Object>)task.getResult().getValue();
                        DailyChallengeManager.setCurrentType(category+""+challengeGradeLevel);
                        DailyChallengeManager.setCurrentQuestionData((Map<String, Object>)questionsList.get(questionIndex));
                        if(!retrieveDataPoints((Map<String, Object>)questionsList.get(questionIndex))){
                            questionIndex=generateQuestionIndex();
                            populateQuestionMap();
                            return;
                        }
                        loadChallenge();
                        if(DailyChallengeManager.getCurrentQuestionData().get("questionPicNumber")==null){
                            generateImage(-1);
                        }
                        else{
                            generateImage((long)DailyChallengeManager.getCurrentQuestionData().get("questionPicNumber"));
                        }
                    }
                }
            });
        }
        if(category.equals("Math")){
            userDatabase.child(category).child("Grade_"+challengeGradeLevel).child(mathCategory).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        List<Object> questionsList= (List<Object>)task.getResult().getValue();
                        DailyChallengeManager.setCurrentType(category+""+challengeGradeLevel);
                        DailyChallengeManager.setCurrentQuestionData((Map<String, Object>)questionsList.get(questionIndex));
                        if(!retrieveDataPoints((Map<String, Object>)questionsList.get(questionIndex))){
                            questionIndex=generateQuestionIndex();
                            populateQuestionMap();
                            return;
                        }
                        loadChallenge();
                        if(DailyChallengeManager.getCurrentQuestionData().get("questionPicNumber")==null){
                            generateImage(-1);
                        }
                        else{
                            generateImage((long)DailyChallengeManager.getCurrentQuestionData().get("questionPicNumber"));
                        }
                    }
                }
            });
        }


    }

    // OnClick methods
    public void clickedA(View view){
        verifyAnswer("A");
    }

    public void clickedB(View view){
        verifyAnswer("B");
    }

    public void clickedC(View view){
        verifyAnswer("C");
    }

    public void clickedD(View view){
        verifyAnswer("D");
    }

    private void verifyAnswer(String choice){
        TextView explanationText = findViewById(R.id.explanation);

        explanationText.setText("Explanation: "+currentQuestionData.get("explanation"));
    }

    private boolean retrieveDataPoints(Map<String, Object> databaseStorage){
        if(databaseStorage==null){
            return false;
        }

        Object question, opta, optb, optc, optd, answer, explanation;

        question=databaseStorage.get("question");
        opta=databaseStorage.get("optA");
        optb=databaseStorage.get("optB");
        optc=databaseStorage.get("optC");
        optd=databaseStorage.get("optD");
        answer=databaseStorage.get("answer");
        explanation=databaseStorage.get("explanation");

        if(question==null||opta==null||optb==null||optc==null||optd==null||answer==null||explanation==null){
            return false;
        }
        return true;

    }

    private void generateImage(long imgID){
        if(imgID<0){
            return;
        }

        ImageView questionPic =findViewById(R.id.questionPic);

        StorageReference qImageRef = FirebaseStorage.getInstance().getReference()   //but what if it doesn't exist?
                .child(TopicManager.getPicRootFolderName())
                .child("Question_Pics")
                .child(TopicManager.getPicNamePrefix()+"_q_"+imgID+".PNG");

        qImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    Glide.with(DailyChallenge.this)
                            .load(task.getResult())
                            .into(questionPic);
                }
                else {
                    Toast.makeText(DailyChallenge.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
