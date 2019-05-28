package com.example.valetautomationsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    private Button sendButton = null;
    private RatingBar ratingBar = null;
    private EditText commentField = null;
    private FirebaseDatabase database = null;
    private DatabaseReference reference = null;
    private String comment = "";
    private float rating = 0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        ratingBar = findViewById(R.id.ratingBar);
        commentField = findViewById(R.id.commentField);
        sendButton = findViewById(R.id.commentButton);
        sendButton.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("comments");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == sendButton.getId()){
            comment = commentField.getText().toString();
            rating = ratingBar.getRating();
            writeToDatabase();
            Intent intent = new Intent(CommentActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
    }

    private void writeToDatabase() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int commentIndex = Integer.parseInt(dataSnapshot.child("index").child("commentIndex").getValue().toString());
                int ratingIndex = Integer.parseInt(dataSnapshot.child("index").child("ratingIndex").getValue().toString());
                reference.child("comment").child(String.valueOf(commentIndex++)).setValue(comment);
                reference.child("rating").child(String.valueOf(ratingIndex++)).setValue(rating);
                reference.child("index").child("commentIndex").setValue(commentIndex);
                reference.child("index").child("ratingIndex").setValue(ratingIndex);
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
