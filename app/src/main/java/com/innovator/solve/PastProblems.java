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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    private LinearLayout popup;

    private void getPastProblems(){
        String currentUserID=InnovatorApplication.getUser().getId();

        questionLocation.collection("User_"+currentUserID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> dataList=queryDocumentSnapshots.getDocuments();
                    int index=0;
                    for(DocumentSnapshot d : dataList){
                        String selectedAnswer = d.get("answer").toString();

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
                                popup = findViewById(R.id.popup);
                                popup.setVisibility(View.GONE);

                                long questionPicNumber=-1;
                                if(questionData.get("picNumber")!=null){
                                    Toast.makeText(PastProblems.this, questionData.get("picNumber")+"", Toast.LENGTH_SHORT).show();
                                    questionPicNumber=(long)questionData.get("picNumber");
                                }

                                String correctAnswer=questionData.get("answer").toString();
                                try{

                                    ImageView questionPic =findViewById(R.id.questionPic);

                                    Button choiceA = findViewById(R.id.pastAnswerA);
                                    Button choiceB=findViewById(R.id.pastAnswerB);
                                    Button choiceC=findViewById(R.id.pastAnswerC);
                                    Button choiceD=findViewById(R.id.pastAnswerD);

                                    TextView questionText=findViewById(R.id.questionDisplay);
                                    Map<String, Button> references = new HashMap<String, Button>(){{
                                        put("Option A", choiceA);
                                        put("Option B", choiceB);
                                        put("Option C", choiceC);
                                        put("Option D", choiceD);
                                    }};

                                    choiceA.setText(questionData.get("optA").toString());
                                    choiceB.setText(questionData.get("optB").toString());
                                    choiceC.setText(questionData.get("optC").toString());
                                    choiceD.setText(questionData.get("optD").toString());

                                    questionText.setText("Question: "+questionData.get("question").toString());

                                    popup.setVisibility(View.VISIBLE);
                                    TextView txt = (TextView) findViewById(R.id.textView2);
                                    txt.setText("Explanation: "+questionData.get("explanation").toString());
                                    Button button = (Button) findViewById(R.id.button);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            popup.setVisibility(View.GONE);
                                        }
                                    });
                                    references.get("Option "+correctAnswer).setBackgroundColor(Color.GREEN);
                                    references.get("Option "+correctAnswer).setTextColor(Color.WHITE);

                                    if(!("Option "+correctAnswer).equals(selectedAnswer)){
                                        references.get(selectedAnswer).setBackgroundColor(Color.RED);
                                        references.get(selectedAnswer).setTextColor(Color.WHITE);
                                    }

                                    if(questionData.get("passage")!=null){
                                        TextView passageText=findViewById(R.id.text_view);
                                        passageText.setText("Passage: "+questionData.get("passage").toString());
                                    }
                                    if(questionPicNumber!=-1){
                                        insertPic(questionPicNumber, questionPic);
                                    }

                                    Question currentQuestion= new Question(questionData.get("question").toString(), questionData.get("optA").toString(), questionData.get("optB").toString(),
                                            questionData.get("optC").toString(), questionData.get("optD").toString(), correctAnswer, questionData.get("explanation").toString(), questionData.get("category").toString(), (int)questionPicNumber, -1);

                                    RetryProblem.retryQuestion=currentQuestion;
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

    private void insertPic(long picID, ImageView img){
        StorageReference qImageRef = FirebaseStorage.getInstance().getReference()   //but what if it doesn't exist?
                .child(TopicManager.getPicRootFolderName())
                .child("Question_Pics")
                .child(TopicManager.getPicNamePrefix()+"_q_"+picID+".PNG");

        qImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    Glide.with(PastProblems.this)
                            .load(task.getResult())
                            .into(img);
                }
                else {
                    Toast.makeText(PastProblems.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    public void retryCurrentQuestion(View view){
        startActivity(new Intent(this, RetryProblem.class));
    }

}
