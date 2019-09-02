package com.example.solve

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseUser
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.SignInButton
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.plus.Plus


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



    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        viewOfLayout = inflater.inflate(R.layout.account_fragment, container, false)
        val sign_in_button = viewOfLayout.findViewById<SignInButton>(R.id.sign_in_button)
        sign_in_button.setOnClickListener(this);

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
        }// ...
    }
    public override fun onStart() {
        super.onStart()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun updateUI ( account: GoogleSignInAccount?) {
        // TODO: Use the ViewModel
        var currentUserLabel = this.viewOfLayout.findViewById(R.id.currentUser) as TextView
        currentUserLabel.setText(account!!.displayName)

    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w("AccountFragment", "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }

    }
}
