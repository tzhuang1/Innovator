package com.example.innovatorTopicSelection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.innovatorsetup.R;


public class MainActivity extends Fragment {

    Button startPracticeButton;
    ImageButton leftButton, rightButton;
    ImageView left, current, right;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topic_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        startPracticeButton = (Button) getView().findViewById(R.id.practice_start_button);
        //leftButton = (ImageButton) getView().findViewById(R.id.)
        //left = getView().findViewById();
        //current = (ImageView) getView().findViewById();
        //right = getView().findViewById();

        //this is triggered soon after OCV above.
        //any view setup here (view lookups, view listener attach)

        //setup any handles to view objects here (?)

        super.onViewCreated(view, savedInstanceState);
    }
}
