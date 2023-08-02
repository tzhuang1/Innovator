package com.innovator.solve;

import java.util.Iterator;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.innovator.solve.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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

public class MainMenuController extends AppCompatActivity {
    private NavigationBarView bottomNavBar;
    private int selectedItemID;

    private FirebaseAuth auth;
    private DatabaseReference userDatabase;


    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.angela_activity_main);


        auth=FirebaseAuth.getInstance();
        userDatabase=FirebaseDatabase.getInstance().getReference();

        //isUserSignedIn();


        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragment_container, HomeController.class, null).commit();


        establishNavBarTask();
        //addToMongoDB();
    }

    // OnClick listeners
    public void seePastProblems(View view){
        startActivity(new Intent(this, PastProblems.class));
    }

    public void returnToHome(View view){
        setContentView(R.layout.angela_activity_main);
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, HomeController.class, null).commit();
        establishNavBarTask();
    }

    public void dailyChallenge(View view){
        startActivity(new Intent(this, Review.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void establishNavBarTask(){
        bottomNavBar=findViewById(R.id.nav_view);

        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                selectedItemID=item.getItemId();

                if(selectedItemID==R.id.navigation_home){
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, HomeController.class, null).commit();
                }
                if(selectedItemID==R.id.navigation_mock){
                    Log.d("MainMenu", "Boop!");
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, MockSelectFragment.class, null).commit();
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
