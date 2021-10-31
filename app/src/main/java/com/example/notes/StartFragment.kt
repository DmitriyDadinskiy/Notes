package com.example.notes

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class StartFragment : Fragment() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInButton: SignInButton
    private lateinit var emailView: TextView
    private lateinit var next: MaterialButton
    private lateinit var buttonSingOut: MaterialButton
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)
        initGoogleSign()
        initView(view)
        enableSing()
        return view
    }

    private fun initGoogleSign() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    private fun initView(view: View) {
        signInButton = view.findViewById(R.id.sing_in_button)
        signInButton.setOnClickListener(View.OnClickListener { signIn() })
        emailView = view.findViewById(R.id.email)
        next = view.findViewById(R.id.continue_)
        next.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
        buttonSingOut = view.findViewById(R.id.sing_out_button)
        buttonSingOut.setOnClickListener { signOut() }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            disableSign()
            updateUI(account.email)
        }
    }

    private fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener {
            updateUI("")
            enableSing()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SING_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SING_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            disableSign()
            updateUI(account.email)
        } catch (e: ApiException) {
            Log.w(TAG, "ОШИБКА  code=" + e.statusCode)
        }
    }

    private fun updateUI(email: String) {
        emailView.text = email
    }

    private fun enableSing() {
        signInButton.isEnabled = true
        next.isEnabled = false
        buttonSingOut.isEnabled = false
    }

    private fun disableSign() {
        signInButton.isEnabled = false
        next.isEnabled = true
        buttonSingOut.isEnabled = true
    }

    companion object {
        private const val RC_SING_IN = 8888
        private const val TAG = "GoogleAuth"
        fun newInstance(): StartFragment {
            return StartFragment()
        }
    }
}