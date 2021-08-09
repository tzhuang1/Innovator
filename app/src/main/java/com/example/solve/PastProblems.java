package com.example.solve;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PastProblems extends AppCompatActivity{

    private LinearLayout pastProblemsLayout;
    private final int displayNum=10;

    private FirebaseFirestore questionLocation;


    protected void onCreate(Bundle savedInstanceState){
        questionLocation=FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_past_problems);

        pastProblemsLayout = (LinearLayout)findViewById(R.id.past_problems_linear_layout);

        populateLayout();
    }

    private void getPastProblems(){
        String currentUserID=InnovatorApplication.getUser().getId();
        questionLocation.collection("User_"+currentUserID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> dataList=queryDocumentSnapshots.getDocuments();
                    int index=0;
                    for(DocumentSnapshot d : dataList){
                        Map<String, Object> questionData = (Map<String, Object>)d.get("question");
                        Button b = new Button(pastProblemsLayout.getContext());
                        if(questionData.get("question").toString().length()<45){
                            b.setText(questionData.get("question").toString());
                        }
                        else{
                            b.setText(questionData.get("question").toString().substring(0, 44)+"...");
                        }
                        b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                setContentView(R.layout.single_past_problem);
                                Toast.makeText(PastProblems.this, "dasjkljklasdjklasdjklasdjkl", Toast.LENGTH_LONG).show();


                                try{
                                    Button choiceA = findViewById(R.id.pastAnswerA);
                                    Button choiceB=findViewById(R.id.pastAnswerB);
                                    Button choiceC=findViewById(R.id.pastAnswerC);
                                    Button choiceD=findViewById(R.id.pastAnswerD);

                                    TextView questionText=findViewById(R.id.pastQuestionDisplay);
                                    TextView explanationText=findViewById(R.id.pastExplanation);

                                    choiceA.setText(questionData.get("optA").toString());
                                    choiceB.setText(questionData.get("optB").toString());
                                    choiceC.setText(questionData.get("optC").toString());
                                    choiceD.setText(questionData.get("optD").toString());

                                    questionText.setText(questionData.get("question").toString());
                                    explanationText.setText(questionData.get("explanation").toString());
                                }
                                catch(Exception e){
                                    Toast.makeText(PastProblems.this, "choiceA button is null: "+e.toString(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                        pastProblemsLayout.addView(b);
                        index++;
                        if(index==displayNum){
                            break;
                        }
                    }
                }
            }
        });
    }


    public void populateLayout(){
        getPastProblems();
    }

    public void back(View view){
        startActivity(new Intent(this, PastProblems.class));
    }

    public void returnToHome(View v){
        startActivity(new Intent(this, MainMenuController.class));
    }

}
