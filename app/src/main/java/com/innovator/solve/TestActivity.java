package com.innovator.solve;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

public class TestActivity extends AppCompatActivity implements
        DragAndDropFragment.OnAnswerSelected,
        MultipleAnswerFragment.OnAnswerSelected,
        ShortAnswerFragment.OnAnswerSelected {
    int numSections = 1;
    Typeface tb;
    String testName;
    int numBoxes = 3;
    int numChoices = 7;
    int reqAnswers = 1;
    int lastPageNo;
    ImageButton leftButton, rightButton;
    int prevPage;
    int page = 0;
    int[] qIds;
    boolean saveState = true;
    ArrayList<AnswerFormatManager> answers = new ArrayList<>();

    ArrayList<Question> qs;
    String testID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_two);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        leftButton.setVisibility(View.GONE);
        testID = getIntent().getExtras().getString("TestID");
        MockTestManager.MockTest m = MockTestManager.getTestByID(testID);
        qs = m.questionList;
        lastPageNo = getIntent().getExtras().getInt("NUMQUESTIONS");
        qIds = new int[lastPageNo];
        for (int i = 0; i < lastPageNo; i++) {
            String questionType = qs.get(i).getQuestionType();
            if (questionType.charAt(0) == 'M') {
                qIds[i] = 0;
            } else if (questionType.charAt(0) == 'D') {
                qIds[i] = 1;

            } else if (questionType.charAt(0) == 'S') {
                qIds[i] = 2;
            } else {
                Log.d("Error", "Not valid question type");
            }
        }
        testName = getIntent().getExtras().getString("TestName");
        if (getIntent().getExtras().getBoolean("FROMSAVEDSTATE")) {
            SharedPreferences sp = getSharedPreferences(testName, Context.MODE_PRIVATE);
            for (int i = 0; i < lastPageNo; i++) {
                answers.add(i, AnswerFormatManager.unBundle(sp, i, qIds[i]));
            }
            page = sp.getInt("CURRENTPAGE", 0);
        }
        else {
            for (int i = 0; i < lastPageNo; i++) {
                answers.add(i, new AnswerFormatManager(qIds[i]));
            }
        }
        if (page >= lastPageNo)
            page = 0;
        tb = Typeface.createFromAsset(getAssets(), "fonts/karla.ttf");
        buttonGen();
        ((TextView)findViewById(R.id.question_no)).setTypeface(tb);
        ((TextView)findViewById(R.id.question_no)).setText("Question " + (page+1) +"/" + lastPageNo);
        prevPage = page;
        chooseFormat(qs.get(page), qIds[page], R.anim.no_slide, R.anim.no_slide);

        //activate/deactivate calculator/formula sheet
        View view = getWindow().getDecorView().getRootView();
        View calcBtn =  view.findViewById(R.id.calcButton);
        View formulaBtn = view.findViewById(R.id.FormulaSheetButton);
        if (qs.get(page).isCalcActive()) {
            calcBtn.setVisibility(View.VISIBLE);
        }
        else {
            calcBtn.setVisibility(View.GONE);
        }
        if (qs.get(page).isFormulaActive()) {
            formulaBtn.setVisibility(View.VISIBLE);
        }
        else {
            formulaBtn.setVisibility(View.GONE);
        }

    }

    public void updateImage() {
        Question q = qs.get(page);
        if (q.getPicNumber() > -1) {
            findViewById(R.id.viewPicButton).setVisibility(View.VISIBLE);
        }
        else
            findViewById(R.id.viewPicButton).setVisibility(View.GONE);
    }
    public void onDndAnswerSelect(int[][] boxAndButtons) {
        for (int[] pair: boxAndButtons)
            answers.get(page).addDndAnswer(pair[0], pair[1]);
    }
    public void onMcqAnswerSelect(int[] as) {
        for (int ans: as)
            answers.get(page).addMcqAnswer(ans);
    }

    public void onFrqAnswerSelect(String s) {
        clearChoices();
        answers.get(page).addFrqAnswer(s);
    }

    public void clearChoices() {
        answers.get(page).clearDndAnswers();
        answers.get(page).clearMcqAnswers();
        answers.get(page).clearFrqAnswer();
    }

    private void chooseFormat(Question q, int id, int slidein, int slideout) {
        if (id == 0) {
            numChoices = q.getChoices().size();
            startMultipleChoice(q, numChoices, reqAnswers, page, slidein, slideout);
        }
        if (id == 1) {
            startDragAndDrop(q, numBoxes, page, slidein, slideout);
        }
        if (id == 2) {
            startFreeResponse(q, page, slidein, slideout);
        }
    }

    private void startMultipleChoice(Question q, int nChoices, int rAnswers, int qNo, int slidein, int slideout) {
        Bundle bundle = new Bundle();
        bundle.putInt("REQANSWERS", rAnswers);
        bundle.putInt("NUMCHOICES", nChoices);
        QuestionManager.compileData(bundle, q);
        bundle = answers.get(qNo).bundleUp(bundle);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(slidein, slideout).setReorderingAllowed(true).replace(R.id.question_fragment, MultipleAnswerFragment.class, bundle).commit();

    }

    private void startDragAndDrop(Question q, int nBoxes, int qNo, int slidein, int slideout) {
        Bundle bundle = new Bundle();
        bundle.putInt("NUMBOXES", nBoxes);
        QuestionManager.compileData(bundle, q);
        bundle = answers.get(qNo).bundleUp(bundle);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(slidein, slideout).setReorderingAllowed(true).replace(R.id.question_fragment, DragAndDropFragment.class, bundle).commit();
    }

    private void startFreeResponse(Question q, int qNo, int slidein, int slideout) {
        Bundle bundle = new Bundle();
        QuestionManager.compileData(bundle, q);
        bundle = answers.get(qNo).bundleUp(bundle);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(slidein, slideout).setReorderingAllowed(true).replace(R.id.question_fragment, ShortAnswerFragment.class, bundle).commit();

    }

    private Question testQuestion() {
        Question q = new Question();
        q.setQuestion("What is 5+5?\nAHHHH\nAHHHH\nAHHHH\nAHHHH\nAHHHH\nAHHHH\nAHHHH\nAHHHH\nAHHHH\nAHHHH\nAHHHH");
        q.setOptA("34");
        q.setOptB("e");
        q.setOptC("3.14");
        q.setOptD("10");
        return q;
    }

    public void pageLeft(View view) {
        prevPage = page;
        if (page > 0) page --;
        if (page == 0) leftButton.setVisibility(View.GONE);
        buttonGen();
        chooseFormat(qs.get(page), qIds[page], R.anim.slide_in_ltr, R.anim.slide_out_ltr);
        ((TextView)findViewById(R.id.question_no)).setText("Question " + (page+1) +"/" + lastPageNo);

    }

    public void buttonGen() {
        updateImage();
        if (page == 0) leftButton.setVisibility(View.GONE);
        else leftButton.setVisibility(View.VISIBLE);
        if (page == lastPageNo-1) {
            rightButton.setVisibility(View.GONE);
            findViewById(R.id.button_submit).setVisibility(View.VISIBLE);
        }
        else {
            rightButton.setVisibility(View.VISIBLE);
            findViewById(R.id.button_submit).setVisibility(View.GONE);
        }
    }

    public void pageRight(View view) {
        prevPage = page;
        if (page < lastPageNo) page ++;
        buttonGen();
        chooseFormat(qs.get(page), qIds[page], R.anim.slide_in_rtl, R.anim.slide_out_rtl);
        ((TextView)findViewById(R.id.question_no)).setText("Question " + (page+1) +"/" + lastPageNo);
    }

    public void onDestroy() {
        if (saveState) {
            SharedPreferences sp = getSharedPreferences(testName, Context.MODE_PRIVATE);
            SharedPreferences.Editor spEdit = sp.edit();
            spEdit.clear();
            spEdit.putInt("NUMQUESTIONS", lastPageNo);
            for (int i = 0; i < lastPageNo; i++) {
                answers.get(i).bundleUp(spEdit, i);
            }
            spEdit.putInt("CURRENTPAGE", page);
            spEdit.apply();
        }
        super.onDestroy();
    }

    public void viewImage(View view) {
        //findViewById(R.id.constraintLayout4).setVisibility(View.GONE);
        findViewById(R.id.imageView3).setVisibility(View.VISIBLE);
        findViewById(R.id.imageView3).bringToFront();
        //findViewById(R.id.picture_layout).setVisibility(View.VISIBLE);
        loadQuestionPic(qs.get(page).getPicNumber());
        findViewById(R.id.question_picture).setVisibility(View.VISIBLE);
        findViewById(R.id.question_picture).bringToFront();
        //findViewById(R.id.question_picture).bringToFront();
    }

    public void returnToHome(View view) {
        prevPage = page;
        finish();
    }

    public void promptHome(View view) {
        findViewById(R.id.dark_screen).setVisibility(View.VISIBLE);
        findViewById(R.id.leave_prompt).setVisibility(View.VISIBLE);
        findViewById(R.id.leave_prompt).bringToFront();
        findViewById(R.id.constraintLayout4).setClickable(false);
        findViewById(R.id.leave_prompt).setFocusable(true);
        findViewById(R.id.leave_prompt).setClickable(true);
    }

    public void goToFormulaSheet(View view) {
        findViewById(R.id.formula_sheet).setVisibility(View.VISIBLE);
        findViewById(R.id.formula_sheet).bringToFront();
        //findViewById(R.id.constraintLayout4).setVisibility(View.GONE);
    }

    public void returnToTestPage(View view) {
        findViewById(R.id.formula_sheet).setVisibility(View.GONE);
        findViewById(R.id.dark_screen).setVisibility(View.GONE);
        findViewById(R.id.leave_prompt).setVisibility(View.GONE);
        findViewById(R.id.submit_prompt).setVisibility(View.GONE);
        findViewById(R.id.question_picture).setVisibility(View.GONE);
        findViewById(R.id.picture_layout).setVisibility(View.GONE);
        findViewById(R.id.constraintLayout4).setVisibility(View.VISIBLE);
    }

    public void displayCalculator(View view) {
        findViewById(R.id.calculator_layout).setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.calculator_fragment, CalculatorFragment.class, null).commit();
    }
    public void showTutorial(View view) {
        findViewById(R.id.tutorial_layout).setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.tutorial_fragment, TutorialFragment.class, null).commit();
    }
    public void submitTest(View view) {
        findViewById(R.id.dark_screen).setVisibility(View.VISIBLE);
        findViewById(R.id.submit_prompt).setVisibility(View.VISIBLE);
        findViewById(R.id.submit_prompt).bringToFront();
        findViewById(R.id.constraintLayout4).setClickable(false);
        findViewById(R.id.submit_prompt).setFocusable(true);
        findViewById(R.id.submit_prompt).setClickable(true);
    }

    public void goToResults(View view) {
        page = 0;
        SharedPreferences.Editor editor = getSharedPreferences(testName, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        saveState = false;
        ScoreOfMockTest.numSections = numSections;
        ScoreOfMockTest.section1 = gradeTest();
        ScoreOfMockTest.section1total = lastPageNo;
        Intent i = new Intent(this, CongratsPage.class);
        String ans = "";
        for (AnswerFormatManager afm: answers) {
            ans += afm.toString() + "\n";
        }
        i.putExtra("ANSWERS", ans.substring(0, ans.length()-1));
        i.putExtra("TestID", getIntent().getExtras().getString("TestID"));
        startActivity(i);
        finish();
    }

    public int gradeTest() {
        int total = 0;
        for (int i = 0; i < lastPageNo; i++) {
            total += answers.get(i).evaluateAnswer(qs.get(i), qIds[i]);
        }
        return total;
    }

    private void loadQuestionPic(int questionPicID){
        if(questionPicID < 0)return;
        //FirebaseAuth.getInstance().signInAnonymously();
        StorageReference qImageRef = FirebaseStorage.getInstance().getReference()   //but what if it doesn't exist?
                .child("Mocks")
                .child(testID)
                .child("Question_Pics")
                .child(questionPicID+".png");

        qImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    Glide.with(TestActivity.this)
                            .load(task.getResult())
                            .into((ImageView) findViewById(R.id.question_picture));
                    Log.d("IDK", ""+questionPicID);
                }
                else {

                }
            }
        });
    }

}
