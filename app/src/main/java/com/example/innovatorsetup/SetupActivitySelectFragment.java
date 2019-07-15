package com.example.innovatorsetup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetupActivitySelectFragment.OnActivitySelectFragmentListener} interface
 * to handle interaction events.
 * Use the {@link SetupActivitySelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetupActivitySelectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DEFAULT_ACTIVITY_NUM = "defaultActivityNum";

    // TODO: Rename and change types of parameters
    private int defaultActivityNum;

    private OnActivitySelectFragmentListener mListener;

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
    public static SetupActivitySelectFragment newInstance(String defaultActivityNum) {
        SetupActivitySelectFragment fragment = new SetupActivitySelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEFAULT_ACTIVITY_NUM, defaultActivityNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            defaultActivityNum = getArguments().getInt(ARG_DEFAULT_ACTIVITY_NUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_activity_select, container, false);

        Button activityUpBtn = view.findViewById(R.id.activitySelectUpBtn);
        Button activityDownBtn = view.findViewById(R.id.activitySelectDownBtn);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int defaultActivityNum) {
        if (mListener != null) {
            mListener.OnActivitySelectFragmentListener(defaultActivityNum);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivitySelectFragmentListener) {
            mListener = (OnActivitySelectFragmentListener) context;
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
}
