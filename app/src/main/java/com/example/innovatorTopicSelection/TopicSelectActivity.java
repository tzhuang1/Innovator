package com.example.innovatorTopicSelection;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.innovatorsetup.R;

public class TopicSelectActivity extends FragmentActivity {
    //Activity has 2 fragments: the top one (topic select) and the bottom bar.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        TopicSelectFragment topFragment = new TopicSelectFragment();


        ft.add(R.id.topic_select_fragment_activity, topFragment);
        ft.commit();


    }
}
