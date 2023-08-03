package com.innovator.solve;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.innovator.solve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class RetryProblem extends AppCompatActivity {

    public static Question retryQuestion;


    public TextView questionText, explanationText;
    public ImageView questionImg;

    public Button choiceA, choiceB, choiceC, choiceD;

    public Map<String, Button> buttonDict;

    private LinearLayout popup;

    private boolean answerSelected;

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.retry_problem);


        answerSelected=false;
        questionText=findViewById(R.id.questionDisplay);
        explanationText=findViewById(R.id.explanation);

        questionImg=findViewById(R.id.questionPic);

        choiceA=findViewById(R.id.retryAnswerA);
        choiceB=findViewById(R.id.retryAnswerB);
        choiceC=findViewById(R.id.retryAnswerC);
        choiceD=findViewById(R.id.retryAnswerD);

        buttonDict=new HashMap<String, Button>(){{
            put("Option A", choiceA);
            put("Option B", choiceB);
            put("Option C", choiceC);
            put("Option D", choiceD);
        }};

        initiateValues();
    }

    public void initiateValues(){
        popup = findViewById(R.id.popup);
        popup.setVisibility(View.GONE);

        questionText.setText("Question: "+retryQuestion.getQuestion());
        choiceA.setText(retryQuestion.getOptA());
        choiceB.setText(retryQuestion.getOptB());
        choiceC.setText(retryQuestion.getOptC());
        choiceD.setText(retryQuestion.getOptD());

        loadImage(questionImg, retryQuestion.getPicNumber());
    }

    private void verifyAnswer(String choice){
        popup.setVisibility(View.VISIBLE);
        TextView txt = (TextView) findViewById(R.id.textView2);
        txt.setText("Explanation: "+retryQuestion.getExplanation());
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.setVisibility(View.GONE);
            }
        });

        buttonDict.get(choice).setBackgroundColor(Color.RED);
        buttonDict.get(choice).setTextColor(Color.WHITE);


        buttonDict.get("Option "+retryQuestion.getAnswer()).setBackgroundColor(Color.GREEN);
        buttonDict.get("Option "+retryQuestion.getAnswer()).setTextColor(Color.WHITE);

        answerSelected=true;


    }

    public void back(View view){
        startActivity(new Intent(this, PastProblems.class));
    }

    public void clickedA(View view){
        if(!answerSelected)
            verifyAnswer("Option A");
    }

    public void clickedB(View view){
        if(!answerSelected)
            verifyAnswer("Option B");

    }

    public void clickedC(View view){
        if(!answerSelected)
            verifyAnswer("Option C");

    }

    public void clickedD(View view){
        if(!answerSelected)
            verifyAnswer("Option D");

    }

    private void loadImage(ImageView img, long imgID){
        if(retryQuestion.getPicNumber()==-1)
            return;
        StorageReference qImageRef = FirebaseStorage.getInstance().getReference()   //but what if it doesn't exist?
                .child(TopicManager.getPicRootFolderName())
                .child("Question_Pics")
                .child(TopicManager.getPicNamePrefix()+"_q_"+imgID+".PNG");

        qImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    Glide.with(RetryProblem.this)
                            .load(task.getResult())
                            .into(img);
                }
                else {
                    Toast.makeText(RetryProblem.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
