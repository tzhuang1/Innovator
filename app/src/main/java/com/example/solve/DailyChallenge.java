package com.example.solve;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

public class DailyChallenge extends AppCompatActivity{

    private int minMathQuestionsCount=80;
    private int minReadingQuestionsCount=65;

    private String challengeGradeLevel;
    private String category;

    // Firebase Objects
    private FirebaseAuth auth;
    private DatabaseReference userDatabase;

    private int questionIndex=1;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.daily_challenge_options);

        auth=FirebaseAuth.getInstance();
        userDatabase= FirebaseDatabase.getInstance().getReference();

    }

    public void returnToHome(View view){
        startActivity(new Intent(this, MainMenuController.class));
    }

    public void onChallengeClick(View view) {
        String completeStr =view.getResources().getResourceEntryName(view.getId());
        String gradeLevel=completeStr.substring(completeStr.length()-1);
        String categoryStr=completeStr.substring(0, completeStr.length()-1);

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

        Map<String, Object> currentQuestionData=DailyChallengeManager.getCurrentQuestionData();

        questionDisplay.setText("Question: "+(String)currentQuestionData.get("question"));
        choiceA.setText("A. "+(String)currentQuestionData.get("optA"));
        choiceB.setText("B. "+(String)currentQuestionData.get("optB"));
        choiceC.setText("C. "+(String)currentQuestionData.get("optC"));
        choiceD.setText("D. "+(String)currentQuestionData.get("optD"));
    }



    private void populateQuestionMap(){

        Calendar calendar=Calendar.getInstance();
        int minutes=calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR);
        int AMvalue=calendar.get(Calendar.AM);


        if(minutes==0&&hour==12&&AMvalue==1){
            questionIndex=3;
            Toast.makeText(this, ""+minutes+""+hour, Toast.LENGTH_SHORT).show();
        }

        userDatabase.child(category).child("Grade_"+challengeGradeLevel).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    List<Object> questionsList= (List<Object>)task.getResult().getValue();
                    DailyChallengeManager.setCurrentType(category+""+challengeGradeLevel);
                    DailyChallengeManager.setCurrentQuestionData((Map<String, Object>)questionsList.get(questionIndex));
                    loadChallenge();
                    //saveInProperties(category+""+challengeGradeLevel, (Map<String, Object>)questionsList.get(1));

                }
            }
        });

    }

    private void saveInProperties(String currentType, Map<String, Object> metaData){
        String filePath = getApplicationContext().getFilesDir().getPath().toString();

        try (OutputStream outputStream=new FileOutputStream(filePath+"DailyChallengeProperties.properties")){
            Properties prop=new Properties();

            //prop.setProperty(currentType)

            prop.store(outputStream, null);

            Toast.makeText(this, ""+prop.getProperty("HelloMessage"), Toast.LENGTH_SHORT).show();


        }
        catch(Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
