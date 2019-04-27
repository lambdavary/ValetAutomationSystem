package com.example.valetautomationsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailField = null;
    private EditText passwordField = null;
    private Button loginButton = null;
    private Button signupButton = null;
    private FirebaseAuth mAuth = null;
    private FirebaseAuth.AuthStateListener mAuthStateListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null)
                    Toast.makeText(getApplicationContext(), "yeyy", Toast.LENGTH_LONG).show();
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == loginButton.getId()){
            signIn();
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
        if (v.getId() == signupButton.getId()){
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    public void signIn(){

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful())
                    Toast.makeText(getApplicationContext(), "Problem", Toast.LENGTH_LONG).show();
            }
        });
    }
}