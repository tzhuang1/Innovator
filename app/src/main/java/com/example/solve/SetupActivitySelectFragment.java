package com.example.solve;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.solve.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetupActivitySelectFragment.OnActivitySelectFragmentListener} interface
 * to handle interaction events.
 * Use the {@link SetupActivitySelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetupActivitySelectFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DEFAULT_ACTIVITY_NUM = "defaultActivityNum";

    // TODO: Rename and change types of parameters
    private int defaultActivityNum = 1;

    private OnActivitySelectFragmentListener mListener;
    private OnDataPass dataPasser;

    private int activityNum;
    private TextView setupActivityDisplay;

    public SetupActivitySelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param defaultActivityNum Parameter 1.
     * @return A new instance of fragment SetupActivitySelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetupActivitySelectFragment newInstance(int defaultActivityNum) {
        SetupActivitySelectFragment fragment = new SetupActivitySelectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DEFAULT_ACTIVITY_NUM, defaultActivityNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            defaultActivityNum = getArguments().getInt(ARG_DEFAULT_ACTIVITY_NUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_activity_select, container, false);

        activityNum = defaultActivityNum;
        setupActivityDisplay = view.findViewById(R.id.setupActivityDisplayTxt);

        Button activityUpBtn = view.findViewById(R.id.activitySelectUpBtn);
        Button activityDownBtn = view.findViewById(R.id.activitySelectDownBtn);
        activityUpBtn.setOnClickListener(this);
        activityDownBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.activitySelectDownBtn:
                activityNum = Math.max(--activityNum, 1);
                if(activityNum == 1) {
                    setupActivityDisplay.setText(activityNum + " activity/day");
                } else {
                    setupActivityDisplay.setText(activityNum + " activities/day");
                }
                break;
            case R.id.activitySelectUpBtn:
                activityNum = Math.min(++activityNum, 10);
                if(activityNum == 1) {
                    setupActivityDisplay.setText(activityNum + " activity/day");
                } else {
                    setupActivityDisplay.setText(activityNum + " activities/day");
                }
                break;
        }
        dataPasser.putActivitySelect(activityNum);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int defaultActivityNum) {
        if(mListener != null) {
            mListener.OnActivitySelectFragmentListener(defaultActivityNum);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnActivitySelectFragmentListener) {
            mListener = (OnActivitySelectFragmentListener) context;
            dataPasser = (OnDataPass) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.OnActivitySelectFragmentListener(activityNum);
        mListener = null;
        dataPasser.putActivitySelect(activityNum);
        dataPasser = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mListener.OnActivitySelectFragmentListener(activityNum);
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
    public interface OnActivitySelectFragmentListener {
        // TODO: Update argument type and name
        void OnActivitySelectFragmentListener(int defaultActivityNum);
    }

    public interface OnDataPass {
        void putActivitySelect(int activitySelect);
    }

    public void OnActivitySelectFragmentListener(OnActivitySelectFragmentListener mListener) {
        this.mListener = mListener;
    }

    public void putActivitySelect(int activityNum) {
        dataPasser.putActivitySelect(activityNum);
    }

}
