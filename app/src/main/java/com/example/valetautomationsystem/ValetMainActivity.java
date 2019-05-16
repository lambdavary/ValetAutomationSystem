package com.example.valetautomationsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ValetMainActivity extends AppCompatActivity {

    private String valetUsername = null;
    private TextView valetWelcomeTextField = null;
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valet_main);

        valetWelcomeTextField = findViewById(R.id.valetWelcomeTextField);
        intent = getIntent();
        valetUsername = intent.getStringExtra("valetUsername");
        valetWelcomeTextField.setText("Welcome" + valetUsername);

    }
}
