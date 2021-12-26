package com.innovator.solve;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.innovator.solve.R;

public class SettingsController extends Fragment{

    // Settings data
    public static int gradeLevel;
    public static int numActivities;
    public static boolean notifsOn;

    public static EditText gradeSettings, activitiesSettings;

    public SettingsController(){
        super(R.layout.settings_fragment);


    }
    
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);


    }
}
