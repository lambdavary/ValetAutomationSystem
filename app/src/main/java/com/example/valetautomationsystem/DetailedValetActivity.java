package com.example.valetautomationsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailedValetActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent = null;
    private TextView valetCountField = null;
    private EditText valetEmailField = null;
    private EditText valetPasswordField = null;
    private EditText valetQrField = null;
    private Button addValetButton = null;
    private FirebaseAuth mAuth = null;
    private FirebaseDatabase database = null;
    private DatabaseReference reference = null;
    private DatabaseReference reference2 = null;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_valet);

        valetCountField = findViewById(R.id.valetCountField);
        valetEmailField = findViewById(R.id.valetEmailField);
        valetPasswordField = findViewById(R.id.valetPasswordField);
        valetQrField = findViewById(R.id.valetQrField);
        addValetButton = findViewById(R.id.addValetButton);
        addValetButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("valets");
        reference2 = database.getReference("valetUsers");
        intent = getIntent();

        valetCountField.setText("Current valet count:" + intent.getStringExtra("valetCount"));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == addValetButton.getId()){
            signUp();
            addValetIdAndUsername();
        }
    }

    public void signUp() {
        final String email = valetEmailField.getText().toString();
        final String password = valetPasswordField.getText().toString();

        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Valet has been added successfully!", Toast.LENGTH_LONG).show();
                            } else {
                                Log.v("ggstatus", "gg");
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addValetIdAndUsername() {
        index = Integer.parseInt(intent.getStringExtra("valetCount"));
        reference.child(valetQrField.getText().toString()).setValue(valetQrField.getText().toString());
        reference2.child(String.valueOf(index++)).setValue(valetEmailField.getText().toString());
        valetCountField.setText("Current valet count:" + String.valueOf(index));
    }
}
