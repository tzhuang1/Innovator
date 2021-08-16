package com.example.solve;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;

import org.bson.Document;


public class ProgressFragment extends Fragment {
//
private int questionsCompletedToday;
    private int questionsCorrectToday = 0;
    protected int questionsGoal;

    private int dailyAccuracyPercentage = 100;
    //weekly stats
    private int questionsCompletedWeekly;
    private int questionsCorrectWeekly = 0;
    private int weeklyAccuracyPercentage = 100;
    //overall stats
    private int questionsCompletedOverall;
    private int questionsCorrectOverall = 0;
    private int overallAccuracyPercentage = 100;

    //UIs
    private TextView questionsCompletedTodayCount;
    private TextView dailyAccuracyDisplay;
//    private TextView questionsCompletedWeeklyCount;
//    private TextView weeklyAccuracyDisplay;
    private TextView questionsCompletedOverallCount;
    private TextView overallAccuracyDisplay;

    private FirebaseFirestore firestoreDB;
    private List<AnsweredQuestionData> questionsCompleted;
    private int totalCorrect;
    private int completedToday;
    private int correctToday;

    private Calendar calendar;


    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.progress_fragment, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //UIs initialization
        firestoreDB=FirebaseFirestore.getInstance();

        questionsCompletedTodayCount = view.findViewById(R.id.questionsCompletedTodayCount);
        dailyAccuracyDisplay = view.findViewById(R.id.dailyAccuracyDisplay);

//        questionsCompletedWeeklyCount = view.findViewById(R.id.questionsCompletedWeeklyCount);
//        weeklyAccuracyDisplay = view.findViewById(R.id.weeklyAccuracyDisplay);

        questionsCompletedOverallCount = view.findViewById(R.id.questionsCompletedOverallCount);
        overallAccuracyDisplay = view.findViewById(R.id.overallAccuracyDisplay);

        super.onViewCreated(view, savedInstanceState);

        calendar=Calendar.getInstance();

        questionsCompleted=new ArrayList<AnsweredQuestionData>();
        totalCorrect=0;
        setValuesToNone();
        obtainOverallStatistics();
    }


    /*  adds questions to the statistics, and updates values depending on if it was correct or not.
     *   Correct is list of correctness of all questions entered, each element must be either
     *   "CORRECT" or "INCORRECT"    */
    public void addQuestions(ArrayList<String> questions) {
        int numQuestions = questions.size();
        //updates completed questions
        questionsCompletedToday += numQuestions;
        questionsCompletedWeekly += numQuestions;
        questionsCompletedOverall += numQuestions;

        questionsCompletedTodayCount.setText(questionsCompletedToday);
        //questionsCompletedWeeklyCount.setText(questionsCompletedWeekly);
        questionsCompletedOverallCount.setText(questionsCompletedWeekly);

        //calculates and updates the number of correct questions
        int numCorrect = 0;
        for(int i=0; i < numQuestions; i++) {
            if(questions.get(i).equals("CORRECT")) {
                numCorrect ++;
            }
        }
        questionsCorrectToday += numCorrect;
        questionsCorrectWeekly += numCorrect;
        questionsCorrectOverall += numCorrect;

        //updates the percentages
        //goalMetPercentage = (int)(100 * questionsCorrectToday/questionsGoal);
        dailyAccuracyPercentage = (int)(100 * questionsCorrectToday/questionsCompletedToday);
        weeklyAccuracyPercentage = (int)(100 * questionsCorrectWeekly/questionsCompletedWeekly);

        dailyAccuracyDisplay.setText(dailyAccuracyPercentage + "%");
        //weeklyAccuracyDisplay.setText(weeklyAccuracyPercentage + "%");
    }


    public void setValuesToNone(){
        questionsCompletedOverallCount.setText("0");
        overallAccuracyDisplay.setText("0");

        questionsCompletedTodayCount.setText("0");
        dailyAccuracyDisplay.setText("0%");

//        questionsCompletedWeeklyCount.setText("0");
//        weeklyAccuracyDisplay.setText("0%");
    }

    private void obtainOverallStatistics(){
        if(InnovatorApplication.getUser()!=null){
            firestoreDB.collection("User_"+InnovatorApplication.getUser().getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots.isEmpty()){
                        setValuesToNone();
                    }

                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> dataList=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : dataList){
                            String selectedAnswer = d.get("answer").toString();

                            Map<String, Object> questionData=(Map<String, Object>)d.get("question");
                            Question currentQuestion=new Question(questionData.get("question").toString(), null, null, null, null, questionData.get("answer").toString(), null, null);
                            questionsCompleted.add(new AnsweredQuestionData(currentQuestion, selectedAnswer));

                            if(selectedAnswer.substring(selectedAnswer.length()-1).equals(questionData.get("answer").toString())){
                                totalCorrect++;
                            }
                        }
                        setDataValues();
                    }
                }
            });
        }
    }

    private void setDataValues() {


        questionsCompletedOverallCount.setText(""+questionsCompleted.size());
        double overallAccuracy= 1.0*totalCorrect/questionsCompleted.size();
        overallAccuracyDisplay.setText(""+(Math.round(overallAccuracy*100))+"%");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String dateStr=formatter.format(date).substring(0,10).replace('/','-');

        getFirebaseStats(dateStr);

        SharedPreferences completedProgress = getActivity().getSharedPreferences("CompletedAmount", Context.MODE_PRIVATE);
        SharedPreferences.Editor editPref=completedProgress.edit();

        questionsCompletedTodayCount.setText((completedProgress.getInt(dateStr+"_Completed",0)+completedToday)+"");

        Map<String, Object> todayData=new HashMap<String, Object>();
        todayData.put("total", completedProgress.getInt(dateStr+"_Completed",0)+completedToday);
        todayData.put("correct",0);

        firestoreDB.collection(InnovatorApplication.getUser().getId()+"_Statistics").document(dateStr+"_Completed").set(todayData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });

        completedProgress.edit().remove(dateStr+"_Completed");
        completedProgress.edit().commit();




    }

    private void getFirebaseStats(String dateStr) {
        firestoreDB.collection(InnovatorApplication.getUser().getId() + "_Statistics").document(dateStr + "_Completed").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(!documentSnapshot.exists()){
                    completedToday=0;
                    correctToday=0;
                    return;
                }

                long currentTotal=(long)documentSnapshot.get("total");
                long currentCorrect=(long)documentSnapshot.get("correct");

                completedToday=(int)currentTotal;
                correctToday=(int)currentCorrect;

            }
        });


    }
}
