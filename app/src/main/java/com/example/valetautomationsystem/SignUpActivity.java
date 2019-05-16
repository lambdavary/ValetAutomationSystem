package com.example.valetautomationsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private CheckBox checkBox = null;
    private Button signUpButton = null;
    private FirebaseAuth mAuth = null;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        usernameField = findViewById(R.id.usernameField);
        emailField = findViewById(R.id.userEmailField);
        passwordField = findViewById(R.id.userPasswordField);
        passwordCheckField = findViewById(R.id.userPasswordCheckField);
        checkBox = findViewById(R.id.userCheckBox);
        signUpButton = findViewById(R.id.userSignUpButton);
        signUpButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == signUpButton.getId()) {
            Log.v("sifre", passwordField.getText().toString());
            Log.v("username Field:", usernameField.getText().toString());
            if (checkEntries())
                signUp();
            else
                Toast.makeText(getApplicationContext(), "Please check the entries!", Toast.LENGTH_LONG).show();
        }
    }

    public void signUp() {
        final String email = emailField.getText().toString();
        final String password = passwordField.getText().toString();

        Log.v("password", password);

        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.v("status", "success");
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.putExtra("userEmail", email);
                                intent.putExtra("userPassword", password);
                                startActivity(intent);
                            } else {
                                Log.v("ggstatus", "gg");
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean checkEntries() {
        if (!(usernameField.getText().toString().equals(null))
                && !(emailField.getText().toString().equals(null))
                && passwordField.getText().toString().equals(passwordCheckField.getText().toString())
                && checkBox.isChecked())
            return true;
        else
            return false;
    }
}
