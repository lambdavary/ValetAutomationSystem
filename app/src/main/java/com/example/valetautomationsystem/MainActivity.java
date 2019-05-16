package com.example.valetautomationsystem;

import android.content.Intent;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String email = null;
    private EditText emailField = null;
    private EditText passwordField = null;
    private Button loginButton = null;
    private Button signupButton = null;
    private FirebaseAuth mAuth = null;
    private FirebaseAuth.AuthStateListener mAuthStateListener = null;
    private FirebaseUser firebaseUser = null;
    private FirebaseDatabase database = null;
    private DatabaseReference reference = null;
    private DatabaseReference reference2 = null;
    private Intent intent = null;
    private ArrayList<String> valets = null;
    private ArrayList<String> admins = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        intent = getIntent();
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);
        valets = new ArrayList<>();
        admins = new ArrayList<>();

        initalize();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("valetUsers");
        reference2 = database.getReference("admins");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    valets.add(child.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    admins.add(child.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.signOut();
    }

    @Override
    public void onClick(View v) {
        boolean isValet = false;
        boolean isAdmin = false;
        String valetUsername = "";
        String adminUsername = "";

        if (v.getId() == loginButton.getId()) {
            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        email = firebaseUser.getEmail();
                        Log.v("childEmail", "" + email);
                    }

                }
            };
            mAuth.addAuthStateListener(mAuthStateListener);

            signIn();

            for (String valetName : valets) {
                if (valetName.equals(emailField.getText().toString())) {
                    valetUsername = valetName;
                    isValet = true;
                    break;
                }
            }

            for (String adminName : admins) {
                if (adminName.equals(emailField.getText().toString())) {
                    adminUsername = adminName;
                    isAdmin = true;
                    break;
                }
            }

            if (isValet) {
                Intent intent2 = new Intent(MainActivity.this, ValetMainActivity.class);
                intent2.putExtra("valetUsername", valetUsername);
                startActivity(intent2);
            } else if (isAdmin) {
                Intent intent3 = new Intent(MainActivity.this, AdminMainActivity.class);
                intent3.putExtra("adminUsername", adminUsername);
                startActivity(intent3);
            } else {
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }

        }

        if (v.getId() == signupButton.getId()) {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        }

    }

    public void signIn() {

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

    public void initalize() {
        if (intent.hasExtra("userEmail"))
            emailField.setText(intent.getStringExtra("userEmail"));
        if (intent.hasExtra("userPassword"))
            passwordField.setText(intent.getStringExtra("userPassword"));
    }
}