package com.example.solve;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PastProblems {

    private RelativeLayout pastProblemsLayout;
    private final int displayNum=10;

    private DatabaseReference userData;


    public PastProblems(RelativeLayout pastProblemsLayout, DatabaseReference userData){
        this.pastProblemsLayout=pastProblemsLayout;
        this.userData=userData;


    }



    public void populateLayout(){


    }

}
