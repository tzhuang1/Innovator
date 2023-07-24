package com.innovator.solve;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class DragAndDropQuestion extends AppCompatActivity implements View.OnDragListener, View.OnLongClickListener {
    private HashMap<Integer, Boolean> filled = new HashMap<>();
    private HashMap<Integer, View> choices = new HashMap<>();
    private HashMap<Integer, ViewParent> parents = new HashMap<>();
    private Question currentQuestion;
    private TextView questionText;
    private Button buttonA, buttonB, buttonC, buttonD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_and_drop_question);
        questionText = findViewById(R.id.question_text);

        //Find all views and set Tag to all draggable views
        buttonA = (Button) findViewById(R.id.btnDrag);
        buttonA.setTag("DRAGGABLE BUTTON");
        buttonA.setOnLongClickListener(this);
        parents.put(buttonA.getId(), buttonA.getParent());
        buttonB = (Button) findViewById(R.id.btnDrag1);
        buttonB.setTag("DRAGGABLE BUTTON");
        buttonB.setOnLongClickListener(this);
        parents.put(buttonB.getId(), buttonB.getParent());
        buttonC = (Button) findViewById(R.id.btnDrag2);
        buttonC.setTag("DRAGGABLE BUTTON");
        buttonC.setOnLongClickListener(this);
        parents.put(buttonC.getId(), buttonC.getParent());
        buttonD = (Button) findViewById(R.id.btnDrag3);
        buttonD.setTag("DRAGGABLE BUTTON");
        buttonD.setOnLongClickListener(this);
        parents.put(buttonD.getId(), buttonD.getParent());
        //Set Drag Event Listeners for defined layouts
        findViewById(R.id.box1).setOnDragListener(this);
        filled.put(R.id.box1, false);
        if (getIntent().hasExtra("NUMBOXES") && (Integer) (getIntent().getExtras().get("NUMBOXES")) > 1) {
            findViewById(R.id.box9).setOnDragListener(this);
            filled.put(R.id.box9, false);
        } else {
            findViewById(R.id.box9_container).setVisibility(View.GONE);
        }

        if (getIntent().hasExtra("NUMBOXES") && (Integer) (getIntent().getExtras().get("NUMBOXES")) > 2) {
            findViewById(R.id.box10).setOnDragListener(this);
            filled.put(R.id.box10, false);
        } else {
            findViewById(R.id.box10_container).setVisibility(View.GONE);
        }
        setQuestion(QuestionManager.decompileData(getIntent()));
        displayQuestion();
    }

    public void setQuestion(Question q) {
        currentQuestion = q;
    }

    public void displayQuestion() {
        questionText.setText(currentQuestion.getQuestion());
        buttonA.setText(currentQuestion.getOptA());
        buttonB.setText(currentQuestion.getOptB());
        buttonC.setText(currentQuestion.getOptC());
        buttonD.setText(currentQuestion.getOptD());
    }

    @Override
    public boolean onLongClick(View v) {
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);
        v.startDrag(data, dragshadow, v, 0);
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) && !filled.get(v.getId())) {
                    return true;
                }
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                v.getBackground().clearColorFilter();
                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                ClipData.Item item = event.getClipData().getItemAt(0);
                String dragData = item.getText().toString();
                v.getBackground().clearColorFilter();
                v.invalidate();

                View vw = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) vw.getParent();
                owner.removeView(vw); //remove the dragged view
                LinearLayout container = (LinearLayout) v;
                container.addView(vw);//Add the dragged view
                vw.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE
                // Returns true. DragEvent.getResult() will return true.
                if (filled.containsKey(v.getId())) {
                    findViewById(R.id.clearOpt).setVisibility(View.VISIBLE);
                    choices.put(v.getId(), vw);
                    filled.put(v.getId(), true);
                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                // Turns off any color tinting
                v.getBackground().clearColorFilter();
                // Invalidates the view to force a redraw
                v.invalidate();
                return true;
            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }
    public void buttonClear(View view)
    {
        for (int id: filled.keySet()) {
            if (filled.get(id) && choices.containsKey(id)) {
                View choice = choices.get(id);
                ((LinearLayout) findViewById(id)).removeView(choice);
                ((ViewGroup)parents.get(choice.getId())).addView(choice);
                filled.put(id, false);
            }
        }
        findViewById(R.id.clearOpt).setVisibility(View.GONE);
    }
}