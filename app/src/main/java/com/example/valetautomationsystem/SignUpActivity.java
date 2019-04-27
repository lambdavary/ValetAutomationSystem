package com.example.valetautomationsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == signUpButton.getId()){
            signUp();
        }
    }

    public void signUp(){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        try {
            mAuth.createUserWithEmailAndPassword(email, password);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
