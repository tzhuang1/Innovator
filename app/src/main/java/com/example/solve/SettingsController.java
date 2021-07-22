package com.example.solve;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SettingsController extends Fragment {

    private int gradeLevel;
    private int numActivities;
    private boolean notifsOn;


    public SettingsController(){
        super(R.layout.settings_fragment);
    }
    
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Toast.makeText(getActivity(), "kl;kljljkljk", Toast.LENGTH_SHORT).show();
    }


}
