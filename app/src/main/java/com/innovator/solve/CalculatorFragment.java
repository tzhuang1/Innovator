package com.innovator.solve;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class CalculatorFragment extends Fragment{

    public static final String LEFT  = "([{<";
    public static final String RIGHT = ")]}>";
    public static final String operators = "+ - * /";
    TextView textInput;
    Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    Button[] numberButtons;
    HashMap<Integer, Integer> idsToNums = new HashMap<>();
    Button buttonPlus, buttonMinus, buttonMult, buttonDiv, buttonDot, buttonParenOpen, buttonParenClose;
    Button[] operationButtons;
    HashMap<Integer, String> idsToOps = new HashMap<>();
    Button buttonEquals;
    Button buttonClear;
    Button buttonExit;
    ConstraintLayout container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calculator, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;

        textInput = view.findViewById(R.id.calc_inout);

        button0 = view.findViewById(R.id.button0);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        button4 = view.findViewById(R.id.button4);
        button5 = view.findViewById(R.id.button5);
        button6 = view.findViewById(R.id.button6);
        button7 = view.findViewById(R.id.button7);
        button8 = view.findViewById(R.id.button8);
        button9 = view.findViewById(R.id.button9);
        numberButtons = new Button[]{button0, button1,button2, button3, button4, button5, button6, button7, button8, button9};
        int i = 0;
        for (Button b: numberButtons) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberButton(b.getId());
                }
            });
            idsToNums.put(b.getId(), i++);
        }

        buttonPlus = view.findViewById(R.id.button_plus);
        buttonMinus = view.findViewById(R.id.button_minus);
        buttonMult = view.findViewById(R.id.button_mult);
        buttonDiv = view.findViewById(R.id.button_div);
        buttonDot = view.findViewById(R.id.button_dot);
        buttonParenOpen = view.findViewById(R.id.button_parens_open);
        buttonParenClose = view.findViewById(R.id.button_parens_close);
        operationButtons = new Button[]{buttonPlus, buttonMinus, buttonMult, buttonDiv, buttonDot, buttonParenOpen, buttonParenClose};
        for (Button b: operationButtons) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    operationButton(b.getId());
                }
            });
            idsToOps.put(b.getId(), b.getText().toString());
        }

        buttonEquals = view.findViewById(R.id.button_equals);
        buttonEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluate();
            }
        });

        buttonClear = view.findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText();
            }
        });


        buttonExit = view.findViewById(R.id.button_close);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitFragment();
            }
        });
        ViewTreeObserver vto = button1.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                button1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float ogWidth = dpToPixel(88);
                int width  = button1.getMeasuredWidth();
                textInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, width/ogWidth*textInput.getTextSize());
                for (Button b: numberButtons) {
                    b.setTextSize(TypedValue.COMPLEX_UNIT_PX, width / ogWidth * b.getTextSize());
                }
                for (Button b: operationButtons)
                    b.setTextSize(TypedValue.COMPLEX_UNIT_PX, width/ogWidth*b.getTextSize());
                buttonEquals.setTextSize(TypedValue.COMPLEX_UNIT_PX, width/ogWidth*buttonEquals.getTextSize());
                buttonExit.setTextSize(TypedValue.COMPLEX_UNIT_PX, width/ogWidth*buttonExit.getTextSize());

                ((Button)view.findViewById(R.id.button0)).setWidth((int)(width));

                buttonClear.setWidth((int)(width/ogWidth*buttonClear.getWidth()));
                buttonClear.setTextSize(TypedValue.COMPLEX_UNIT_PX, width/ogWidth*buttonClear.getTextSize());

                container = view.findViewById(R.id.input_container);
                //container.setMaxWidth((int)(width/ogWidth*container.getWidth()));
                //container.setMaxHeight((int)(width/ogWidth*container.getHeight()));

            }
        });
    }

    public float dpToPixel(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());

    }

    public void numberButton(int id) {
        if (textInput.getText().toString().equals("ERROR"))
            clearText();
        textInput.setText(textInput.getText().toString() + idsToNums.get(id));
    }

    public void operationButton(int id) {
        if (textInput.getText().toString().equals("ERROR"))
            clearText();
        textInput.setText(textInput.getText().toString() + idsToOps.get(id));
    }

    public void clearText() {
        textInput.setText("");
    }

    public void exitFragment() {

        getActivity().getSupportFragmentManager().beginTransaction().remove(CalculatorFragment.this).commit();
    }

    public void evaluate() {
        int lastIndex = 0;
        ArrayList<String> parts = new ArrayList<>();
        String text = textInput.getText().toString();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '+' || text.charAt(i) == '-' || text.charAt(i) == '*' || text.charAt(i) == '/' || text.charAt(i) == '(' || text.charAt(i) == ')') {
                if (text.charAt(i) == '-' && lastIndex == i)
                    continue;
                if (text.substring(lastIndex, i).length() > 0)
                    parts.add(text.substring(lastIndex, i));
                if (text.substring(i, i+1).length() > 0)
                    parts.add(text.substring(i, i+1));
                lastIndex = i+1;
            }
        }
        if (text.substring(lastIndex).length() > 0)
            parts.add(text.substring(lastIndex));
        try {
            String postfix = infixToPostfix(parts);
            double d = eval(postfix);
            textInput.setText("" + (new DecimalFormat("#.####").format(d)));
        } catch (Exception e) {
            textInput.setText("ERROR");
        }

    }

    public static double eval(String pf)
    {
        List<String> postfixParts = new ArrayList<String>(Arrays.asList(pf.split(" ")));
        /*  enter your code here  */
        Stack<Double> stk = new Stack<Double>();
        for (String s: postfixParts)
        {
            try
            {
                stk.push(Double.parseDouble(s));
            } catch (Exception e)
            {
                if (!s.equals("!"))
                {
                    double b = stk.pop();
                    stk.push(eval(stk.pop(), b, s));
                } else
                {
                    stk.push(eval(stk.pop(), 0, s));
                }
            }
        }
        return stk.pop();
    }

    public static double eval(double a, double b, String op)
    {
        if (op.equals("!"))
        {
            if (a == 0)
                return 1;
            return a * eval(a-1, b, op);
        }
        if (op.equals("+"))
            return a + b;
        if (op.equals("*"))
            return a * b;
        if (op.equals("/"))
            return a/b;
        if (op.equals("-"))
            return a-b;
        if (op.equals("%"))
            return a%b;
        if (op.equals("^"))
            return Math.pow(a, b);
        return a;
    }

    public static String infixToPostfix(ArrayList<String> nums)
    {
        String finString = "";
        Stack<String> stk = new Stack<String>();
        for (String s: nums)
        {
            try
            {
                Double.parseDouble(s);
                finString += s + " ";
            } catch (Exception e)
            {
                if (!stk.isEmpty())
                {
                    if (RIGHT.indexOf(s) >= 0)
                    {
                        while (RIGHT.indexOf(s) != LEFT.indexOf(stk.peek()))
                            finString += stk.pop() + " ";
                        stk.pop();
                        continue;
                    }
                    while (!stk.isEmpty()&&(LEFT.indexOf(stk.peek())==-1) && !isStrictlyLower(s, stk.peek()))
                        finString += stk.pop() + " ";
                }
                stk.push(s);
            }
        }
        while (!stk.isEmpty())
            finString += stk.pop() + " ";
        return finString.substring(0, finString.length()-1);
    }

    //enter your precedence method below
    private static boolean isStrictlyLower(String next, String top)
    {
        return getLevel(next) > getLevel(top);
    }

    private static int getLevel(String op)
    {
        if (op.equals("+") || op.equals("-"))
            return 1;
        if ( op.equals("*") || op.equals("/"))
            return 2;
        return 3;
    }

}

