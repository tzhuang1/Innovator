package com.innovator.solve;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

public class DragAndDropFragment extends Fragment implements View.OnDragListener, View.OnLongClickListener {

    public interface OnAnswerSelected {
        public void onDndAnswerSelect(int[][] boxAndButton);
        public void clearChoices();
    }

    OnAnswerSelected mCallback;
    private HashMap<Integer, Boolean> filled = new HashMap<>();
    private HashMap<Integer, View> choices = new HashMap<>();
    private HashMap<Integer, ViewParent> parents = new HashMap<>();
    private ArrayList<Integer> buttons = new ArrayList<>();
    private ArrayList<Integer> boxes = new ArrayList<>();
    private Question currentQuestion;
    private TextView questionText;
    private Button buttonA, buttonB, buttonC, buttonD, buttonClear;
    private LinearLayout clearOpt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.drag_and_drop_question, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        questionText = view.findViewById(R.id.question_text);
        mCallback = (OnAnswerSelected)getActivity();

        //Find all views and set Tag to all draggable views
        buttonA = (Button) view.findViewById(R.id.btnDrag);
        buttonA.setTag("DRAGGABLE BUTTON");
        buttonA.setOnLongClickListener(this);
        parents.put(buttonA.getId(), buttonA.getParent());
        buttons.add(buttonA.getId());

        buttonB = (Button) view.findViewById(R.id.btnDrag1);
        buttonB.setTag("DRAGGABLE BUTTON");
        buttonB.setOnLongClickListener(this);
        parents.put(buttonB.getId(), buttonB.getParent());
        buttons.add(buttonB.getId());

        buttonC = (Button) view.findViewById(R.id.btnDrag2);
        buttonC.setTag("DRAGGABLE BUTTON");
        buttonC.setOnLongClickListener(this);
        parents.put(buttonC.getId(), buttonC.getParent());
        buttons.add(buttonC.getId());

        buttonD = (Button) view.findViewById(R.id.btnDrag3);
        buttonD.setTag("DRAGGABLE BUTTON");
        buttonD.setOnLongClickListener(this);
        parents.put(buttonD.getId(), buttonD.getParent());
        buttons.add(buttonD.getId());

        clearOpt = view.findViewById(R.id.clearOpt);
        buttonClear = view.findViewById(R.id.buttonClear);
        //Set Drag Event Listeners for defined layouts
        view.findViewById(R.id.box1).setOnDragListener(this);
        filled.put(R.id.box1, false);
        boxes.add(R.id.box1);
        boxes.add(R.id.box9);
        boxes.add(R.id.box10);

        if ((Integer) (requireArguments().getInt("NUMBOXES")) > 1) {
            view.findViewById(R.id.box9).setOnDragListener(this);
            filled.put(R.id.box9, false);
        } else {
            view.findViewById(R.id.box9_container).setVisibility(View.GONE);
        }

        if ((Integer) (requireArguments().getInt("NUMBOXES")) > 2) {
            view.findViewById(R.id.box10).setOnDragListener(this);
            filled.put(R.id.box10, false);
        } else {
            view.findViewById(R.id.box10_container).setVisibility(View.GONE);
        }
        setQuestion(QuestionManager.decompileData(requireArguments()));
        displayQuestion();

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClear(view);
            }
        });

        int numAns = requireArguments().getInt("NUMANSWERS");
        for (int i = 0; i < numAns; i++) {
            setAnswer(requireArguments().getInt("BOX" + i), requireArguments().getInt("BUTTON" + i));
        }
    }

    public void onDestroy() {
        updateChoices();
        super.onDestroy();
    }

    public void updateChoices() {
        mCallback.clearChoices();
        int[][] bnb = new int[choices.size()][2];
        int i = 0;
        for (int key: choices.keySet()) {
            bnb[i][0] = boxes.indexOf(key);
            bnb[i++][1] = buttons.indexOf(choices.get(key).getId());
        }
        mCallback.onDndAnswerSelect(bnb);
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

    public void setAnswer(int box, int button) {
        Button nButton = getView().findViewById(buttons.get(button));
        clearOpt.setVisibility(View.VISIBLE);
        filled.put(boxes.get(box), true);
        ((ViewGroup)parents.get(buttons.get(button))).removeView(nButton);
        ((LinearLayout)getView().findViewById(boxes.get(box))).addView(nButton);
        choices.put(boxes.get(box), nButton);
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
                v.getBackground().clearColorFilter();
                v.invalidate();

                View vw = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) vw.getParent();
                owner.removeView(vw);
                LinearLayout container = (LinearLayout) v;
                container.addView(vw);
                vw.setVisibility(View.VISIBLE);
                if (filled.containsKey(v.getId())) {
                    clearOpt.setVisibility(View.VISIBLE);
                    choices.put(v.getId(), vw);
                    filled.put(v.getId(), true);
                    updateChoices();
                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                v.getBackground().clearColorFilter();
                v.invalidate();
                return true;
            default:
                break;
        }
        return false;
    }
    public void buttonClear(View view)
    {
        for (int id: filled.keySet()) {
            if (filled.get(id) && choices.containsKey(id)) {
                View choice = choices.get(id);
                ((LinearLayout) view.findViewById(id)).removeView(choice);
                ((ViewGroup)parents.get(choice.getId())).addView(choice);
                filled.put(id, false);
                choices.remove(id);
            }
        }
        view.findViewById(R.id.clearOpt).setVisibility(View.GONE);
    }
}
