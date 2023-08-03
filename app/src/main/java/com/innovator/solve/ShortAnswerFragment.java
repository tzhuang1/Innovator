package com.innovator.solve;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ShortAnswerFragment extends Fragment {

    public interface OnAnswerSelected {
        public void onFrqAnswerSelect(String s);
        public void clearChoices();
    }
    OnAnswerSelected mCallback;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.short_answer_activity, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        questionPicLayout = view.findViewById(R.id.question_pic_layout);
        questionPicText = view.findViewById(R.id.question_pic_text);
        questionPic = view.findViewById(R.id.question_picture);
        questionText = (TextView) view.findViewById(R.id.question_text);

        textAnswersLayout = view.findViewById(R.id.textAnswersLayout);
        textAnswer = view.findViewById(R.id.textAnswer);
        buttonSubmit = (FButton) view.findViewById(R.id.buttonSubmit);

        tb = Typeface.createFromAsset(getActivity().getAssets(), "fonts/karla.ttf");

        //Setting typefaces for textview and buttons
        questionPicText.setTypeface(tb);
        questionText.setTypeface(tb);
        textAnswer.setTypeface(tb);
        int numAns = requireArguments().getInt("NUMANSWERS");
        if (numAns == 1) {
            setAnswer(requireArguments().getString("ANSWER"));
        }
        setQuestion(QuestionManager.decompileData(requireArguments()));
        displayQuestion(currentQuestion);
        mCallback = (OnAnswerSelected)getActivity();
        textAnswer.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                mCallback.clearChoices();
                mCallback.onFrqAnswerSelect(s.toString());
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void setAnswer(String s) {
        textAnswer.setText(s);
    }

    public void setQuestion(Question q)
    {
        currentQuestion = q;
    }

    public void displayQuestion(Question q) {
        questionText.setText(currentQuestion.getQuestion());
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
