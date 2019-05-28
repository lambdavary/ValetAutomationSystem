package com.example.valetautomationsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button valetInfoButton = null;
    private Button appInfoButton = null;
    private FirebaseDatabase database = null;
    private DatabaseReference reference = null;
    private long count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        valetInfoButton = findViewById(R.id.valetInfo);
        appInfoButton = findViewById(R.id.appInfo);
        valetInfoButton.setOnClickListener(this);
        appInfoButton.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("valetUsers");


    }

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(AdminMainActivity.this, DetailedValetActivity.class);
        if (v.getId() == valetInfoButton.getId()) {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    count = dataSnapshot.getChildrenCount();
                    Log.v("count", "" + count);
                    intent.putExtra("valetCount", String.valueOf(count));
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (v.getId() == appInfoButton.getId()) {

        } else {

        }
    }
}
