package com.example.innovatorsetup;

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
    private static final String ARG_GRADE_DEFUALT_NUM = "defaultGradeNum";

    private int gradeSelect;
    private TextView setupGradeDisplay;

    private onGradeSelectFragmentInteraction mListener;

    public SetupGradeSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param defaultGradeNum Parameter 1.
     * @return A new instance of fragment SetupGradeSelectFragment.
     */
    public static SetupGradeSelectFragment newInstance(String defaultGradeNum) {
        SetupGradeSelectFragment fragment = new SetupGradeSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GRADE_DEFUALT_NUM, defaultGradeNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gradeSelect = getArguments().getInt(ARG_GRADE_DEFUALT_NUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_setup_grade_select, container, false);

        Button gradeSelectLeftBtn = view.findViewById(R.id.gradeSelectLeftBtn);
        Button gradeSelectRightBtn = view.findViewById(R.id.gradeSelectRightBtn);

        setupGradeDisplay = view.findViewById(R.id.setupGradeDisplayTxt);
        setupGradeDisplay.setText(gradeList[gradeSelect]);

        gradeSelectLeftBtn.setOnClickListener(this);
        gradeSelectRightBtn.setOnClickListener(this);

        return view;
    }

    public void onButtonPressed(int defaultGradeNum) {
        if (mListener != null) {
            mListener.onGradeSelectFragmentInteraction(defaultGradeNum);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mListener = (onGradeSelectFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.gradeSelectLeftBtn:
                gradeSelect = Math.max(--gradeSelect, 0);
                setupGradeDisplay.setText(gradeList[gradeSelect]);
                break;
            case R.id.gradeSelectRightBtn:
                gradeSelect = Math.min(++gradeSelect, gradeList.length - 1);
                setupGradeDisplay.setText(gradeList[gradeSelect]);
                break;
        }
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
}
