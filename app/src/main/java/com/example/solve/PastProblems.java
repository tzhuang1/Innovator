package com.example.solve;

import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PastProblems {

    private RelativeLayout pastProblemsLayout;
    private final int displayNum=10;

    private DatabaseReference userData;

    public PastProblems(RelativeLayout pastProblemsLayout, DatabaseReference userData){
        this.pastProblemsLayout=pastProblemsLayout;
        this.userData=userData;


    }

    public void populateLayout(){
        userData.child("UserData").child("Profile").child("103788776077754662736").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    HashMap<String, String> userInfo=(HashMap<String, String>)task.getResult().getValue();
                    Log.i("User Info", userInfo.get("email"));
                }
            }
        });
    }

}
