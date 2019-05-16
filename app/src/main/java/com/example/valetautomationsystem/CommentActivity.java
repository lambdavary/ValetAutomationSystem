package com.example.valetautomationsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    private Button sendButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        sendButton = findViewById(R.id.commentButton);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == sendButton.getId()){
            Intent intent = new Intent(CommentActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
    }
}
