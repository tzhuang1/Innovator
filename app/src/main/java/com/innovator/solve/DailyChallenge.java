package com.innovator.solve;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.innovator.solve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DailyChallenge extends AppCompatActivity{

    private int minMathQuestionsCount=80;
    private int minReadingQuestionsCount=65;

    private String challengeGradeLevel;
    private String category;

    // Firebase Objects
    private FirebaseAuth auth;
    private DatabaseReference userDatabase;

    private int questionIndex=0;
    private int questionCount=0;

    private Map<String, Object> currentQuestionData;

    private String mathCategory;
    private String readingCategory;

    private String[] mathCategories={"Computation and Estimation", "Measurement and Geometry","Numbers and Number Sense","Patterns, Functions, and Algebra", "Probability and Statistics"};
    private String[] readingCategories={"Reading Comprehension", "Word Analysis"};

    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.daily_challenge_options);

        auth=FirebaseAuth.getInstance();
        userDatabase= FirebaseDatabase.getInstance().getReference();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        currentDate=formatter.format(date).substring(0,10).replace('/','-');

        selectCategory();
    }

    public void selectCategory(){
        SharedPreferences pastCategories=getSharedPreferences("PastCategories", Context.MODE_PRIVATE);
        SharedPreferences.Editor editPastQuestions=pastCategories.edit();

        String dateStr=currentDate;

        if(!pastCategories.contains(dateStr+"_PastCategory_Math")){
            mathCategory=mathCategories[new Random().nextInt(mathCategories.length)];
            editPastQuestions.putString(dateStr+"_PastCategory_Math",mathCategory);
            editPastQuestions.commit();
        }
        else{
            mathCategory=pastCategories.getString(dateStr+"_PastCategory_Math", "Computation and Estimation");
        }
        if(!pastCategories.contains(dateStr+"_PastCategory_Reading")){
            readingCategory=readingCategories[new Random().nextInt(readingCategories.length)];
            editPastQuestions.putString(dateStr+"_PastCategory_Reading", readingCategory);
            editPastQuestions.commit();
        }
        else{
            readingCategory=pastCategories.getString(dateStr+"_PastCategory_Reading", "Reading Comprehension");
        }
    }

    public int getQuestionIndex(String grade, String category){
        SharedPreferences pastQuestions=getSharedPreferences("PastQuestions", Context.MODE_PRIVATE);
        SharedPreferences.Editor editPastQuestions= pastQuestions.edit();

        if(category.equals("Math")){
            if(!pastQuestions.contains(currentDate+"_Grade_"+grade+"_PastQuestion_Math")){
                editPastQuestions.putInt(currentDate+"_Grade_"+grade+"_PastQuestion_Math", new Random().nextInt(questionCount));
                editPastQuestions.commit();
            }
            return pastQuestions.getInt(currentDate+"_Grade_"+grade+"_PastQuestion_Math",0);
        }
        else{
            if(!pastQuestions.contains(currentDate+"_Grade_"+grade+"_PastQuestions_Reading")){
                editPastQuestions.putInt(currentDate+"_Grade_"+grade+"_PastQuestion_Reading", new Random().nextInt(questionCount));
                editPastQuestions.commit();
            }
            return pastQuestions.getInt(currentDate+"_Grade_"+grade+"_PastQuestion_Reading",0);
        }
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

        //populateQuestionMap();
        if(category.equals("Math")){
            getQuestionCount(challengeGradeLevel, category, mathCategory);
        }
        else{
            getQuestionCount(challengeGradeLevel, category, readingCategory);
        }
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
        choiceA.setText("A. "+currentQuestionData.get("optA"));
        choiceB.setText("B. "+currentQuestionData.get("optB"));
        choiceC.setText("C. "+currentQuestionData.get("optC"));
        choiceD.setText("D. "+currentQuestionData.get("optD"));

        if(category.equals("Reading")){
            if(DailyChallengeManager.getCurrentQuestionData().get("passage")!=null){
                TextView passageText=findViewById(R.id.passageText);
                passageText.setText((String)DailyChallengeManager.getCurrentQuestionData().get("passage"));
            }
        }

    }

    public void getQuestionCount(String grade, String category, String topic){
        userDatabase.child(category).child("Grade_"+grade).child(topic).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    List<Object> questionsList=(List<Object>)task.getResult().getValue();
                    questionCount=questionsList.size();
                    populateQuestionMap();
                }
            }
        });
    }

    private void populateQuestionMap(){

        questionIndex=getQuestionIndex(challengeGradeLevel, category);

        if(category.equals("Reading")){
            userDatabase.child(category).child("Grade_"+challengeGradeLevel).child(readingCategory).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        List<Object> questionsList= (List<Object>)task.getResult().getValue();
                        DailyChallengeManager.setCurrentType(category+""+challengeGradeLevel);
                        DailyChallengeManager.setCurrentQuestionData((Map<String, Object>)questionsList.get(questionIndex));
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
//                        if(!retrieveDataPoints((Map<String, Object>)questionsList.get(questionIndex))){
//                            questionIndex=generateQuestionIndex();
//                            populateQuestionMap();
//                            return;
//                        }
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
