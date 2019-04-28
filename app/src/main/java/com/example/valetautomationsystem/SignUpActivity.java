package com.example.valetautomationsystem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameField = null;
    private EditText emailField = null;
    private EditText passwordField = null;
    private EditText passwordCheckField = null;
    private Button signUpButton = null;
    private FirebaseAuth mAuth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        usernameField = findViewById(R.id.usernameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        passwordCheckField = findViewById(R.id.userPasswordCheckField);
        signUpButton  = findViewById(R.id.userSignUpButton);
        signUpButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == signUpButton.getId()){
            Log.v("sifre", passwordField.getText().toString());
            signUp();
        }
    }

    public void signUp(){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        Log.v("password", password);

        try {
            mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "yeyy", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
