package com.example.solve;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetupGradeSelectFragment.onGradeSelectFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link SetupGradeSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetupGradeSelectFragment extends Fragment implements View.OnClickListener {
    private final String[] gradeList = {"1st", "2nd", "3rd", "4th", "5th", "6th"};
    private View view;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GRADE_DEFAULT_NUM = "defaultGradeNum";

    private int defaultGradeNum = 1;
    private int gradeSelect;
    private TextView setupGradeDisplay;

    private onGradeSelectFragmentInteraction mListener;
    private OnDataPass dataPasser;

    public SetupGradeSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param defaultGradeNum Parameter 1.
     * @return A new instance of fragment SetupGradeSelectFragment.
     */
    public static SetupGradeSelectFragment newInstance(int defaultGradeNum) {
        SetupGradeSelectFragment fragment = new SetupGradeSelectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GRADE_DEFAULT_NUM, defaultGradeNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            defaultGradeNum = getArguments().getInt(ARG_GRADE_DEFAULT_NUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_setup_grade_select, container, false);

        gradeSelect = defaultGradeNum;

        Button gradeSelectLeftBtn = view.findViewById(R.id.gradeSelectLeftBtn);
        Button gradeSelectRightBtn = view.findViewById(R.id.gradeSelectRightBtn);

        setupGradeDisplay = view.findViewById(R.id.setupGradeDisplayTxt);
        System.out.println(gradeSelect);
        setupGradeDisplay.setText(gradeList[gradeSelect - 1]);

        gradeSelectLeftBtn.setOnClickListener(this);
        gradeSelectRightBtn.setOnClickListener(this);

        return view;
    }

    public void onButtonPressed(int defaultGradeNum) {
        if(mListener != null) {
            mListener.onGradeSelectFragmentInteraction(defaultGradeNum);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity) {
            mListener = (onGradeSelectFragmentInteraction) context;
            dataPasser = (OnDataPass) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onGradeSelectFragmentInteraction(gradeSelect);
        mListener = null;
        dataPasser.putGradeSelect(gradeSelect);
        dataPasser = null;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.gradeSelectLeftBtn:
                gradeSelect = Math.max(--gradeSelect, 1);
                setupGradeDisplay.setText(gradeList[gradeSelect - 1]);
            case R.id.gradeSelectRightBtn:
                gradeSelect = Math.min(++gradeSelect, gradeList.length);
                setupGradeDisplay.setText(gradeList[gradeSelect - 1]);
                break;
        }
        dataPasser.putGradeSelect(gradeSelect);
    }

    @Override
    public void onPause() {
        super.onPause();
        mListener.onGradeSelectFragmentInteraction(gradeSelect);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onGradeSelectFragmentInteraction {
        void onGradeSelectFragmentInteraction(int defaultGrade);
    }

    public interface OnDataPass {
        void putGradeSelect(int gradeSelect);
    }

    public void onGradeSelectFragmentInteraction(onGradeSelectFragmentInteraction mListener) {
        this.mListener = mListener;
    }
    public void putGradeSelectToMain() {
        dataPasser.putGradeSelect(gradeSelect);
    }
}
