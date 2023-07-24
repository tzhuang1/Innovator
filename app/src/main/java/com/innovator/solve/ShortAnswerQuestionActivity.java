package com.innovator.solve;

import android.content.Intent;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.innovator.solve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import info.hoang8f.widget.FButton;

import static android.view.View.GONE;

public class ShortAnswerQuestionActivity extends AppCompatActivity {

    Typeface tb;

    View questionPicLayout;
    TextView questionPicText;
    ImageView questionPic;
    TextView questionText;

    View textAnswersLayout;
    EditText textAnswer;

    FButton buttonSubmit;

    Question currentQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_answer_activity);
        questionPicLayout = findViewById(R.id.question_pic_layout);
        questionPicText = findViewById(R.id.question_pic_text);
        questionPic = findViewById(R.id.question_picture);
        questionText = (TextView) findViewById(R.id.question_text);

        textAnswersLayout = findViewById(R.id.textAnswersLayout);
        textAnswer = findViewById(R.id.textAnswer);
        buttonSubmit = (FButton) findViewById(R.id.buttonSubmit);

        tb = Typeface.createFromAsset(getAssets(), "fonts/karla.ttf");

        //Setting typefaces for textview and buttons
        questionPicText.setTypeface(tb);
        questionText.setTypeface(tb);
        textAnswer.setTypeface(tb);

    }

    public void setQuestion(Question q)
    {
        currentQuestion = q;
    }



    //--------------------------------------------------------UI stuff---------------------------------------------

    public void buttonSubmit(View view) {
        String ansString = textAnswer.getText().toString();
        ansString = ansString.trim();
        if ("".equals(ansString)) {
            return;
        }
        String option;
        if (ansString.equalsIgnoreCase(currentQuestion.getOptA())) {
            option = "Option A";
        }
        else if (ansString.equalsIgnoreCase(currentQuestion.getOptB())) {
            option = "Option B";
        }
        else if (ansString.equalsIgnoreCase(currentQuestion.getOptC())) {
            option = "Option C";
        }
        else if (ansString.equalsIgnoreCase(currentQuestion.getOptD())){
            option = "Option D";
        }
        else {
            option = "WRONGINPUT";
        }
        //saveHistory(qid, option, currentQuestion);
        textAnswer.setText("");
        /*if (currentQuestion.getAnswer().equalsIgnoreCase(option) ||
                currentQuestion.getAnswer().equalsIgnoreCase(option.substring(option.length()-1))) {
            buttonSubmit.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            //TODO: when on last question and success, the success dialog does not show
            correctDialog();

        } else {
            incorrectDialog();
        }*/
    }
}
