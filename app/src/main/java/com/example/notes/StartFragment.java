package com.example.notes;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;


public class StartFragment extends Fragment {

    private static final int RC_SING_IN = 8888;
    private static final String TAG = "GoogleAuth";


    private GoogleSignInClient googleSignInClient;

    private com.google.android.gms.common.SignInButton signInButton;
    private TextView emailView;
    private MaterialButton continue_;
    private MaterialButton buttonSingOut;

    public static StartFragment newInstance() {
        StartFragment fragment = new StartFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        initGoogleSign();
        initView(view);
        enableSing();
        return view;
    }

    private void initGoogleSign() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }

    private void initView(View view) {
        signInButton = view.findViewById(R.id.sing_in_button);
        signInButton.setOnClickListener(v -> signIn());

        emailView = view.findViewById(R.id.email);

        continue_ = view.findViewById(R.id.continue_);
        continue_.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

        });
        buttonSingOut = view.findViewById(R.id.sing_out_button);
        buttonSingOut.setOnClickListener(v -> signOut());

    }
    @Override
    public void onStart(){
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account != null){
            disableSign();
            updateUI(account.getEmail());
        }
    }

    private void signOut(){
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            updateUI("");
            enableSing();
        });
    }
    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SING_IN);
    }
    @Override
    public void onActivityResult (int requestCode, int resultCode, @NonNull Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SING_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            disableSign();
            updateUI(account.getEmail());
        } catch (ApiException e) {
            Log.w(TAG, "ОШИБКА  code=" + e.getStatusCode());
        }
    }
    private void updateUI(String email) {
        emailView.setText(email);
    }

    private void enableSing() {
        signInButton.setEnabled(true);
        continue_.setEnabled(false);
        buttonSingOut.setEnabled(false);
    }

    private void disableSign() {
        signInButton.setEnabled(false);
        continue_.setEnabled(true);
        buttonSingOut.setEnabled(true);
    }
}



