package com.example.solve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import java.util.Iterator;
import java.util.List;

import info.hoang8f.widget.FButton;

import static android.view.View.GONE;
import static com.example.solve.R.id.btnA;
import static com.example.solve.R.id.homeButton;

public class ReadingQuestions extends AppCompatActivity {

    private View loadingScreen;

    private Typeface tb;

    private View scrollViewPassage;
    private TextView questionText;
    private TextView passageText;

    private Button homeButton;
    private FButton buttonA, buttonB, buttonC, buttonD;

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

        homeButton = findViewById(R.id.homeButton);

        scrollViewPassage = findViewById(R.id.scrollView2);
        passageText = findViewById(R.id.txtPassage);
        questionText = findViewById(R.id.txtQuestion);
        buttonA = (FButton) findViewById(R.id.btnA);
        buttonB = (FButton) findViewById(R.id.btnB);
        buttonC = (FButton) findViewById(R.id.btnC);
        buttonD = (FButton) findViewById(R.id.btnD);

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

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Switch back to home activity
                Intent intent = new Intent(ReadingQuestions.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
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
                //List<HashMap<Object, Object>> listOfQuestions = (List<HashMap<Object, Object>>)dataSnapshot.getValue();
                List<HashMap<Object, Object>> listOfQuestions = (List<HashMap<Object, Object>>)dataSnapshot.getValue();
                for(int i = 0; i < listOfQuestions.size(); i++) {
                    HashMap<Object, Object> entry = listOfQuestions.get(i);
                    try{
                        if(entry != null) {
                            int pictureNum = -1;
                            if(entry.get("questionPicNumber") != null){
                                pictureNum = Integer.parseInt(entry.get("questionPicNumber").toString());
                            }

                            int explanationNum = -1;
                            if(entry.get("explanationPicNumber") != null){
                                explanationNum = Integer.parseInt(entry.get("explanationPicNumber").toString());
                            }

                            //String question, String opta, String optb, String optc, String optd, String answer, String explanation, String category, int picNumber, int exPicNumber
                            Question newQuestion = new Question(entry.get("question").toString(), entry.get("optA").toString(), entry.get("optB").toString(), entry.get("optC").toString(), entry.get("optD").toString(),
                                    entry.get("answer").toString(), entry.get("explanation").toString(), entry.get("category").toString(), pictureNum, explanationNum,
                                    (String) entry.get("passage"));
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
                if(questionsList.size()>0) {
                    currentQuestion = questionsList.get(qid);
                }
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
        passageText.setText(currentQuestion.getPassage());

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
            //savePerUserFirebaseQuestionsList();

        }
    }

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
        final Dialog dialogCorrect = new Dialog(ReadingQuestions.this);
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
        final Dialog dialogComplete = new Dialog(ReadingQuestions.this);
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
                setContentView(R.layout.angela_activity_main);
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, homeFragment);
                ft.commit();

            }
        });
    }

    public void incorrectDialog() {
        final Dialog dialogIncorrect = new Dialog(ReadingQuestions.this);
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

    private void resetColor() {
        buttonA.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonB.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonC.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonD.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
    }
}
