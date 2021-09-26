package com.example.solve;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import info.hoang8f.widget.FButton;

import static android.view.View.GONE;

public class QuestionMainActivity extends AppCompatActivity {


    View loadingScreen;

    Typeface tb;

    View questionPicLayout;
    TextView questionPicText;
    ImageView questionPic;
    TextView questionText;

    View textAnswersLayout;
    View picAnswersLayout;
    FButton buttonA, buttonB, buttonC, buttonD;
    ImageView optAPic, optBPic, optCPic, optDPic;

    ImageView explanationPic;

    Question currentQuestion;
    UserData currentUser;

    List<Question> questionsList;
    List<AnsweredQuestionData> answeredQuestionList;
    int qid = 0;

    private FirebaseFirestore firestoreDB;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentUserID=InnovatorApplication.getUser().getId();
        firestoreDB = FirebaseFirestore.getInstance();

        answeredQuestionList=new ArrayList<AnsweredQuestionData>();

        qid=0;
        //------------------------------------------------------------------view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity_main);

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
        buttonA = (FButton) findViewById(R.id.buttonA);
        buttonB = (FButton) findViewById(R.id.buttonB);
        buttonC = (FButton) findViewById(R.id.buttonC);
        buttonD = (FButton) findViewById(R.id.buttonD);

        tb = Typeface.createFromAsset(getAssets(), "fonts/karla.ttf");

        //Setting typefaces for textview and buttons
        questionPicText.setTypeface(tb);
        questionText.setTypeface(tb);
        buttonA.setTypeface(tb);
        buttonB.setTypeface(tb);
        buttonC.setTypeface(tb);
        buttonD.setTypeface(tb);
        resetColor();

        try{
            getFirebaseQuestionsList();
        }
        catch(Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
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

    private void savePerUserFirebaseQuestionsList(AnsweredQuestionData currentAnsweredQuestion) {
        //get current userID, if userID is empty, then don't save anything
        if(InnovatorApplication.getUser() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String dateStr=formatter.format(date).substring(0,10).replace('/','-');


            int questionNumber = currentAnsweredQuestion.getQuestion().getId();
            String currentGrade=TopicManager.getGradeLevel();
            try{
               firestoreDB.collection("User_"+currentUserID).document(dateStr+":Math"+currentGrade+"_"+questionNumber+"_"+TopicManager.getCategory()).set(currentAnsweredQuestion).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                    public void onSuccess(Void unused) {
                    //Toast.makeText(QuestionMainActivity.this, "Question complete added to user", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch(Exception e){
                Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            }



        }
    }


    private void getFirebaseQuestionsList(){
        Log.e("tag",TopicManager.getCategory());
        DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference()
                .child("Math").child((String)TopicManager.getQuestionFolderName())
                .child((String)TopicManager.getCategory());
        qListRef.addValueEventListener(new ValueEventListener() {//This retrieves the data once
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object myData = dataSnapshot.getValue();
                questionsList = new ArrayList<Question>();
                List<Map<String, Object>> listOfQuestions = (List<Map<String, Object>>)(dataSnapshot.getValue());

                for(int i = 0; i < listOfQuestions.size(); i++) {
                    Map<String, Object> entry = listOfQuestions.get(i);
                    //try{
                        if(entry.get("explanationPicNumber") != null) {
                            Question newQuestion = new Question(""+entry.get("question"), entry.get("optA")+"", entry.get("optB")+"", entry.get("optC")+"", entry.get("optD")+"",
                                    entry.get("answer")+"",
                                    entry.get("explanation")+"",
                                    entry.get("category")+"",
                                    Integer.parseInt(entry.get("questionPicNumber")+""),
                                    Integer.parseInt(entry.get("explanationPicNumber")+""));
                            newQuestion.setId(i);
                            questionsList.add(newQuestion);
                        }
                        else{
                            Question newQuestion = new Question(""+entry.get("question"), entry.get("optA")+"", entry.get("optB")+"", entry.get("optC")+"", entry.get("optD")+"",
                                    entry.get("answer")+"",
                                    entry.get("explanation")+"",
                                    entry.get("category")+"",
                                    Integer.parseInt(entry.get("questionPicNumber")+""),
                                    -1);
                            newQuestion.setId(i);
                            questionsList.add(newQuestion);
                        }
                    //}
                    //catch (Exception ex) {
                    //    Log.e("LoadQuestion", ex.toString());
                    //}
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

//    private void getFirebaseUserData(){
//        DatabaseReference qListRef = FirebaseDatabase.getInstance().getReference("UserData");
//        qListRef.addValueEventListener(new ValueEventListener() {//This retrieves the data once
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                GenericTypeIndicator<UserData> type = new GenericTypeIndicator<UserData>() {};
//                currentUser = dataSnapshot.getValue(type); //DatabaseException: Class java.util.List has generic type parameters, please use GenericTypeIndicator instead
//                Log.i("Get User Data", "Firebase data fetched");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("FB getUserData", "onCancelled with "+databaseError.getMessage()+", details: "+databaseError.getDetails());
//            }
//        });
//    }

    public void updateQueueAndOptions() {
        //sets visibility of layout according to what pictures are in the question
        //question has text even if it has pic
        boolean hasQPic = (currentQuestion.getPicNumber() > -1);
        boolean hasAPics = (currentQuestion.getOptAPicNumber() > -1 || currentQuestion.getOptBPicNumber() > -1 || currentQuestion.getOptCPicNumber() > -1 || currentQuestion.getOptDPicNumber() > -1);
        if(!hasQPic && !hasAPics){ //text only
            //Q
            questionText.setText(currentQuestion.getQuestion());
            questionText.setVisibility(View.VISIBLE);
            questionPicLayout.setVisibility(GONE);
            //A
            picAnswersLayout.setVisibility(GONE);
            textAnswersLayout.setVisibility(View.VISIBLE);
            buttonA.setText(currentQuestion.getOptA());
            buttonB.setText(currentQuestion.getOptB());
            buttonC.setText(currentQuestion.getOptC());
            buttonD.setText(currentQuestion.getOptD());
        }else if(hasQPic && !hasAPics){ //pic question, text answer
            //Q
            questionText.setVisibility(GONE);
            questionPicLayout.setVisibility(View.VISIBLE);
            questionPicText.setText(currentQuestion.getQuestion());
            loadQuestionPic(currentQuestion.getPicNumber());
            //A
            picAnswersLayout.setVisibility(GONE);
            textAnswersLayout.setVisibility(View.VISIBLE);
            buttonA.setText(currentQuestion.getOptA());
            buttonB.setText(currentQuestion.getOptB());
            buttonC.setText(currentQuestion.getOptC());
            buttonD.setText(currentQuestion.getOptD());
        }else if(!hasQPic && hasAPics){ //text question, pic answer
            //Q
            questionText.setText(currentQuestion.getQuestion());
            questionText.setVisibility(View.VISIBLE);
            questionPicLayout.setVisibility(GONE);
            //A
            picAnswersLayout.setVisibility(View.VISIBLE);
            textAnswersLayout.setVisibility(GONE);
            loadAnswerPics(currentQuestion.getOptAPicNumber(), currentQuestion.getOptBPicNumber(), currentQuestion.getOptCPicNumber(), currentQuestion.getOptDPicNumber());
        }else{ //all pictures
            //Q
            questionText.setVisibility(GONE);
            questionPicLayout.setVisibility(View.VISIBLE);
            questionPicText.setText(currentQuestion.getQuestion());
            loadQuestionPic(currentQuestion.getPicNumber());
            //A
            picAnswersLayout.setVisibility(View.VISIBLE);
            textAnswersLayout.setVisibility(GONE);
            loadAnswerPics(currentQuestion.getOptAPicNumber(), currentQuestion.getOptBPicNumber(), currentQuestion.getOptCPicNumber(), currentQuestion.getOptDPicNumber());
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
                savePerUserFirebaseQuestionsList(currentAnsweredQuestion);

            }
    }

    private void loadQuestionPic(int questionPicID){
        if(questionPicID < 0)return;
        StorageReference qImageRef = FirebaseStorage.getInstance().getReference()   //but what if it doesn't exist?
                .child(TopicManager.getPicRootFolderName())
                .child("Question_Pics")
                .child(TopicManager.getPicNamePrefix()+"_q_"+questionPicID+".PNG");

        qImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    Glide.with(QuestionMainActivity.this)
                            .load(task.getResult())
                            .into(questionPic);
                }
                else {
                    Toast.makeText(QuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                    Glide.with(QuestionMainActivity.this)
                            .load(task.getResult())
                            .into(explanationPic);
                }
                else {
                    Toast.makeText(QuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadAnswerPics(int optAID, int optBID, int optCID, int optDID){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if(optAID > -1){
            StorageReference optAImageRef = storageReference
                    .child(TopicManager.getPicRootFolderName())
                    .child("Answer_Pics")
                    .child(TopicManager.getPicNamePrefix()+"_a_"+optAID+".PNG");   //but what if it doesn't exist?
            optAImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Glide.with(QuestionMainActivity.this)
                                .load(task.getResult())
                                .into(optAPic);
                    }
                    else {
                        Toast.makeText(QuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(optBID > -1){
            StorageReference optBImageRef = storageReference
                    .child(TopicManager.getPicRootFolderName())
                    .child("Answer_Pics")
                    .child(TopicManager.getPicNamePrefix()+"_a_"+optBID+".PNG");   //but what if it doesn't exist?
            optBImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Glide.with(QuestionMainActivity.this)
                                .load(task.getResult())
                                .into(optBPic);
                    }
                    else {
                        Toast.makeText(QuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(optCID > -1){
            StorageReference optCImageRef = storageReference
                    .child(TopicManager.getPicRootFolderName())
                    .child("Answer_Pics")
                    .child(TopicManager.getPicNamePrefix()+"_a_"+optCID+".PNG");   //but what if it doesn't exist?
            optCImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Glide.with(QuestionMainActivity.this)
                                .load(task.getResult())
                                .into(optCPic);
                    }
                    else {
                        Toast.makeText(QuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(optDID > -1){
            StorageReference optDImageRef = storageReference
                    .child(TopicManager.getPicRootFolderName())
                    .child("Answer_Pics")
                    .child(TopicManager.getPicNamePrefix()+"_a_"+optDID+".PNG");   //but what if it doesn't exist?
            optDImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Glide.with(QuestionMainActivity.this)
                                .load(task.getResult())
                                .into(optDPic);
                    }
                    else {
                        Toast.makeText(QuestionMainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    //--------------------------------------------------------UI stuff---------------------------------------------

    //Onclick listener for first button
    public void buttonA(View view) {
        //compare the option with the ans if yes then make button color green
        saveHistory(qid, "Option A", currentQuestion);
        if (currentQuestion.getAnswer().equalsIgnoreCase("Option A") ||
                currentQuestion.getAnswer().equalsIgnoreCase("A")) {
            buttonA.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            //TODO: when on last question and success, the success dialog does not show
            disableButton();
            correctDialog();

        }
        //User ans is wrong then just navigate him to the PlayAgain activity
        else {
            incorrectDialog();
        }
    }

    //Onclick listener for sec button
    public void buttonB(View view) {
        saveHistory(qid, "Option B", currentQuestion);
        if (currentQuestion.getAnswer().equalsIgnoreCase("Option B") ||
                currentQuestion.getAnswer().equalsIgnoreCase("B")) {
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
        if (currentQuestion.getAnswer().equalsIgnoreCase("Option C") ||
                currentQuestion.getAnswer().equalsIgnoreCase("C")) {
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
        if (currentQuestion.getAnswer().equalsIgnoreCase("Option D") ||
                currentQuestion.getAnswer().equalsIgnoreCase("D")) {
            buttonD.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            //TODO: when on last question and success, the success dialog does not show
            disableButton();
            correctDialog();

        } else {
            incorrectDialog();
        }
    }

    //This dialog is show to the user after he ans correct
    public void correctDialog() {
        final Dialog dialogCorrect = new Dialog(QuestionMainActivity.this);
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
        final Dialog dialogComplete = new Dialog(QuestionMainActivity.this);
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
                startActivity(new Intent(QuestionMainActivity.this, MainMenuController.class));

            }
        });
    }

    public void incorrectDialog() {
        final Dialog dialogIncorrect = new Dialog(QuestionMainActivity.this);
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
        buttonA.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonB.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonC.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonD.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
    }

    //This method will disable all the option button
    public void disableButton() {
        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);
    }

    //This method will all enable the option buttons
    public void enableButton() {
        buttonA.setEnabled(true);
        buttonB.setEnabled(true);
        buttonC.setEnabled(true);
        buttonD.setEnabled(true);
    }

    private boolean retrieveDataPoints(Map<String, Object> databaseStorage){
        Object question, opta, optb, optc, optd, answer, explanation, category, picNumber, exPicNumber;

        question=databaseStorage.get("question");
        opta=databaseStorage.get("optA");
        optb=databaseStorage.get("optB");
        optc=databaseStorage.get("optC");
        optd=databaseStorage.get("optD");
        answer=databaseStorage.get("answer");
        explanation=databaseStorage.get("explanation");
        category=databaseStorage.get("category");

        picNumber=databaseStorage.get("questionPicNumber");
        exPicNumber=databaseStorage.get("explanationPicNumber");

        if(question==null||opta==null||optb==null||optc==null||optd==null||answer==null||explanation==null||category==null||picNumber==null||exPicNumber==null){
            return false;
        }
        return true;

    }
}
