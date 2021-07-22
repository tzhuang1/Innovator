package com.example.solve;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;

public class AccountController extends Fragment{


    private TextView nameText, emailText;

    public AccountController(){

        super(R.layout.account_fragment);


    }

    public void displayUserData(String userName, String userEmail){

    }





}
