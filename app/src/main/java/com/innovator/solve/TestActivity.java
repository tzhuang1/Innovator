package com.innovator.solve;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity implements
        DragAndDropFragment.OnAnswerSelected,
        MultipleAnswerFragment.OnAnswerSelected,
        ShortAnswerFragment.OnAnswerSelected{
    int numBoxes = 1;
    int numChoices = 7;
    int reqAnswers = 1;
    int lastPageNo;
    ImageButton leftButton, rightButton;
    int prevPage;
    int page = 0;
    int[] qIds;
    ArrayList<AnswerFormatManager> answers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_two);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        leftButton.setVisibility(View.GONE);
        lastPageNo = getIntent().getExtras().getInt("NUMQUESTIONS");
        qIds = new int[lastPageNo];
        for (int i = 0; i < lastPageNo; i++) {
            qIds[i] = (int)(Math.random()*3);
        }
        for (int i = 0; i < lastPageNo; i++) {
            answers.add(i, new AnswerFormatManager(qIds[i]));
        }
        chooseFormat(testQuestion(), qIds[page], R.anim.no_slide, R.anim.no_slide);

    }

    public void onDndAnswerSelect(int[][] boxAndButtons) {
        answers.get(prevPage).clearDndAnswers();
        for (int[] pair: boxAndButtons)
            answers.get(prevPage).addDndAnswer(pair[0], pair[1]);
    }
    public void onMcqAnswerSelect(int[] as) {
        answers.get(prevPage).clearMcqAnswer();
        for (int ans: as)
            answers.get(prevPage).addMcqAnswer(ans);
    }

    public void onFrqAnswerSelect(String s) {
        answers.get(prevPage).clearFrqAnswer();
        answers.get(prevPage).addFrqAnswer(s);
    }

    private void chooseFormat(Question q, int id, int slidein, int slideout) {
        if (id == 0) {
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
        Log.d("NUMANSWERS", ""+answers.get(qNo).getNumAnswers());
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
        q.setQuestion("What is 5+5?");
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
        else leftButton.setVisibility(View.VISIBLE);
        if (page == lastPageNo-1) rightButton.setVisibility(View.GONE);
        else rightButton.setVisibility(View.VISIBLE);
        chooseFormat(testQuestion(), qIds[page], R.anim.slide_in_ltr, R.anim.slide_out_ltr);
    }

    public void pageRight(View view) {
        prevPage = page;
        if (page < lastPageNo) page ++;
        if (page == lastPageNo-1) rightButton.setVisibility(View.GONE);
        else rightButton.setVisibility(View.VISIBLE);
        if (page == 0) leftButton.setVisibility(View.GONE);
        else leftButton.setVisibility(View.VISIBLE);
        chooseFormat(testQuestion(), qIds[page], R.anim.slide_in_rtl, R.anim.slide_out_rtl);
    }

    public void returnToHome(View view) {
        startActivity(new Intent(TestActivity.this, MainMenuController.class));
    }

    public void goToFormulaSheet(View view) {
        findViewById(R.id.formula_sheet).setVisibility(View.VISIBLE);
        findViewById(R.id.formula_sheet).bringToFront();
        //findViewById(R.id.constraintLayout4).setVisibility(View.GONE);
    }

    public void returnToTestPage(View view) {
        findViewById(R.id.formula_sheet).setVisibility(View.GONE);
    }
}
