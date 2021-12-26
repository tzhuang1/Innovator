package com.innovator.solve

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.plus.Plus
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.account_fragment.*
import com.innovator.solve.R


class AccountFragment : GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    View.OnClickListener, Fragment(){
    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.w("AccountFragment", "connection failed" )
        //("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.i("AccountFragment", "connection succeeded." )
    }

    override fun onConnected(p0: Bundle?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.i("AccountFragment", "onConnected.")
    }

    companion object {
        fun newInstance() = AccountFragment()
    }

    private lateinit var viewOfLayout: View
    private var currentUser: FirebaseUser? = null
    /* Request code used to invoke sign in user interactions. */
    private val RC_SIGN_IN = 0


    /* Client used to interact with Google APIs. */
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null

    val SHARED_PREFERENCES_FILE = "com.example.solve.preferenceFile"
    val GRADE = "SelectGrade"
    val ACTIVITY = "SelectActivity"
    //var settings: SharedPreferences? = null
    //settings = getSharedPreferences(SetupMainActivity.SHARED_PREFERENCES_FILE, 0)
    val settings = activity?.getSharedPreferences(SHARED_PREFERENCES_FILE, 0)
    private lateinit var viewModel: AccountViewModel

    fun newInstance(defaultGradeNum: Int): AccountFragment {
        val fragment = AccountFragment()
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        viewOfLayout = inflater.inflate(R.layout.account_fragment, container, false)
        val sign_in_button = viewOfLayout.findViewById<SignInButton>(R.id.sign_in_button)
        sign_in_button.setOnClickListener(this);

        val sign_out_button = viewOfLayout.findViewById<Button>(R.id.sign_out_button)
        sign_out_button.setOnClickListener(this);


        //mGoogleApiClient.connect()
        //this.viewOfLayout.findViewById(R.id.sign_in_button).setOnClickListener(this)
        return viewOfLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = GoogleApiClient.Builder(this.requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Plus.API)
            .addScope(Scope(Scopes.PROFILE))
            .addScope(Scope(Scopes.EMAIL))
            .build()


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken("827086084343-csrc4qdiubqq0opt49c30dgamcl6mgei.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso);

    }

    public fun setCurrentUser(myUser: FirebaseUser?)
    {
       currentUser = myUser
    }

    override fun onClick(v: View) {
        Log.i("AccountFragment", "onClick invoked")
        when (v.id) {
            R.id.sign_in_button -> signIn()
            R.id.sign_out_button -> signOut()
        }// ...

    }
    public override fun onStart() {
        super.onStart()
        var accountt: GoogleSignInAccount? =null
        accountt= GoogleSignIn.getLastSignedInAccount(getActivity())
        if(accountt != null){
            updateUI(accountt)
        }

        mGoogleApiClient?.connect()
    }

    public override fun onStop() {
        super.onStop()
        mGoogleApiClient?.disconnect()
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val sign_in_button = viewOfLayout.findViewById<SignInButton>(R.id.sign_in_button)
                sign_in_button.visibility = View.VISIBLE
                val sign_out_button = viewOfLayout.findViewById<Button>(R.id.sign_out_button)
                sign_out_button.visibility = View.INVISIBLE
                var currentUserLabel = this.viewOfLayout.findViewById(R.id.userName) as TextView
                currentUserLabel.setText("")

            } else {
                Log.d("Google Sign-Out", "get failed with ", task.exception)
            }
        }

//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(Task<Void> task) {
//                        // ...
//                    }
//                });

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
        else
        {
            //sign out
        }
    }


    private fun updateUI ( account: GoogleSignInAccount?) {
        // TODO: Use the ViewModel
        val settings = activity?.getPreferences(0) ?: return
        val grade = settings.getInt(GRADE, 5)
        if(account == null )
        {
            // Initialize a new instance of
            val builder = AlertDialog.Builder(getActivity())

            // Set the alert dialog title
            builder.setTitle("Login failed")

            // Display a message on alert dialog
            builder.setMessage("Google sign-in failed")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("OK"){dialog, which ->


            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()

            return;


        }
        var currentUserLabel = this.viewOfLayout.findViewById(R.id.userName) as TextView
        currentUserLabel.setText("You are signed in as: " + account!!.displayName)
        val sign_in_button = viewOfLayout.findViewById<SignInButton>(R.id.sign_in_button)
        sign_in_button.visibility = View.INVISIBLE
        sign_out_button.visibility = View.VISIBLE
        var userData = UserData(account!!.id,account!!.email,account!!.displayName)
        InnovatorApplication.setUser(userData)
        if (userData != null && userData.getId() != null) {
            val qListRef = FirebaseDatabase.getInstance().reference.child("UserData").child("Profile").child(userData.getId())
            //qListRef?.(userData)
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    var myData =dataSnapshot.getValue()
                    if(myData != null) {
                        userData.grade = (myData as HashMap<*, *>)["grade"].toString().toInt()
                    }
                    else {
                        userData.grade = grade
                    }

                    //Toast.makeText(view!!.context,"Error setting grade, defualted to 5th grade", Toast.LENGTH_SHORT).show()
                    //userData.grade = (myData as HashMap<*, *>)["grade"].toString().toInt()
                    //userData = dataSnapshot.getValue() as UserData
                    InnovatorApplication.setUser(userData)
                    // ...
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                    // ...
                }
            }
            qListRef.addValueEventListener(postListener)

        }

    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {//code 10: "DEVELOPER ERROR"
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w("AccountFragment", "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }

    }
}
