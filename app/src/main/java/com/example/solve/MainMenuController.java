package com.example.solve;

import java.io.*;
import java.util.HashMap;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class MainMenuController extends AppCompatActivity{

    private static final int RC_SIGN_IN = 0;
    private NavigationBarView bottomNavBar;
    private int selectedItemID;

    private FirebaseAuth auth;
    private DatabaseReference userDatabase;

    private GoogleSignInClient signInClient;
    private GoogleSignInAccount googleAccount;

    private boolean isUserSignedIn(){
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            return true;
        }
        return false;
    }

    protected void onStart(){
        super.onStart();
        if(isUserSignedIn()){
            Toast.makeText(this, "You are already signed in", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Not signed in", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.angela_activity_main);

        auth=FirebaseAuth.getInstance();
        googleAccount=GoogleSignIn.getLastSignedInAccount(this);
        userDatabase=FirebaseDatabase.getInstance().getReference();

        bottomNavBar=findViewById(R.id.nav_view);

        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragment_container, HomeController.class, null).commit();

        establishNavBarTask();
        configureSignIn();
    }

    // OnClick listeners
    public void seePastProblems(View view){
        setContentView(R.layout.all_past_problems);

        RelativeLayout problemsPastUI = (RelativeLayout)findViewById(R.id.past_problems_relative_layout);
        PastProblems userCompletedProblems= new PastProblems(problemsPastUI, userDatabase);

        userCompletedProblems.populateLayout();


    }

    public void returnToHome(View view){
        setContentView(R.layout.angela_activity_main);
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, HomeController.class, null).commit();
    }

    public void buttonA(View view){

    }

    public void initiateSignIn(View view){
        signIn();
    }

    public void signOut(View view){
        if(googleAccount!=null){
            auth.signOut();
            Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Not signed in yet", Toast.LENGTH_SHORT).show();
        }
    }

    // Google sign in feature
    public void configureSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage()+" errOR", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = auth.getCurrentUser();
                            googleAccount=GoogleSignIn.getLastSignedInAccount(MainMenuController.this);
                            setUserInfo(googleAccount.getDisplayName(), googleAccount.getEmail());
                        } else {
                        }
                    }
                });
    }

    private void setUserInfo(String userNameStr, String userEmailStr){
        TextView userEmail =findViewById(R.id.userEmail);
        TextView userName=findViewById(R.id.userName);
        if(userEmail!=null&&userName!=null){
            userName.setText(userNameStr);
            userEmail.setText(userEmailStr);
        }
        else{
            Toast.makeText(this, "text views are null", Toast.LENGTH_SHORT).show();
        }
    }

    private void establishNavBarTask(){
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                selectedItemID=item.getItemId();

                if(selectedItemID==R.id.navigation_home){
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, HomeController.class, null).commit();
                }
                if(selectedItemID==R.id.navigation_account){
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, AccountController.class, null).commit();
                    if(isUserSignedIn())
                        setUserInfo(googleAccount.getDisplayName(), googleAccount.getEmail());
                    else
                        setUserInfo("Not signed in", "Not signed in");
                }
                if(selectedItemID==R.id.navigation_dashboard){
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, TopicSelectFragment.class, null).commit();
                }
                if(selectedItemID==R.id.navigation_notifications){
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, SettingsFragment.class, null).commit();
                }
                if(selectedItemID==R.id.navigation_progress){
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, ProgressFragment.class, null).commit();
                }
                return true;
            }
        });
    }

}