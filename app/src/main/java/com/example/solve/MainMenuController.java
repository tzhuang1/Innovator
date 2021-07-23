package com.example.solve;

import java.io.*;
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

import org.jetbrains.annotations.NotNull;

public class MainMenuController extends AppCompatActivity{

    private static final int RC_SIGN_IN = 0;
    private NavigationBarView bottomNavBar;
    private int selectedItemID;

    private FirebaseAuth auth;

    private GoogleSignInClient signInClient;
    private GoogleSignInAccount googleAccount;

    private String email;

    protected void onStart(){
        super.onStart();
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            Toast.makeText(this, "User is already signed in", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.angela_activity_main);

        auth=FirebaseAuth.getInstance();

        bottomNavBar=findViewById(R.id.nav_view);

        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragment_container, HomeController.class, null).commit();

        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                selectedItemID=item.getItemId();

                if(selectedItemID==R.id.navigation_home){
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, HomeController.class, null).commit();
                }
                if(selectedItemID==R.id.navigation_account){
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, AccountController.class, null).commit();
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
        configureSignIn();
    }

    // OnClick listeners
    public void buttonB(View view){

    }

    public void buttonA(View view){

    }

    public void initiateSignIn(View view){
        signIn();
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
                            if(currentUser!=null)
                                Toast.makeText(MainMenuController.this, "Signed into google: "+googleAccount.getEmail(), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MainMenuController.this, "Sign in error", Toast.LENGTH_SHORT).show();
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
    }

}
