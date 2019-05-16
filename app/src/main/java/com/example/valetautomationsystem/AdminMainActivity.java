package com.example.valetautomationsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button manageValetsButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        manageValetsButton = findViewById(R.id.manageValetsButton);
        manageValetsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == manageValetsButton.getId()){
            Intent intent = new Intent(AdminMainActivity.this, DetailedValetActivity.class);
            startActivity(intent);
        }
    }
}
