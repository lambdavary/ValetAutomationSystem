package com.example.valetautomationsystem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppInfoActivity extends AppCompatActivity {

    private TextView ratingAverageField = null;
    private ListView commentList = null;
    private FirebaseDatabase database = null;
    private DatabaseReference reference = null;
    private DatabaseReference reference2 = null;
    private double ratingSum = 0;
    private double ratingAverage = 0;
    private ArrayList<String> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        ratingAverageField = findViewById(R.id.ratingAverageField);
        commentList = findViewById(R.id.commentList);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("comments").child("rating");
        reference2 = database.getReference("comments").child("comment");

        getAverageRating();
        getComments();

        list = new ArrayList<>();
        list.add("perfect");
        list.add("awesome");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
        commentList.setAdapter(adapter);

    }

    public void getAverageRating() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    ratingSum += Double.parseDouble(child.getValue().toString());
                }
                ratingAverage = ratingSum / Double.parseDouble(String.valueOf(dataSnapshot.getChildrenCount()));
                Log.v("ratingAverage" , "" + ratingSum + " " + ratingAverage);
                ratingAverageField.setText("" + ratingAverage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getComments() {
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    list.add(child.getValue().toString());
                    Log.v("comments:", "" + child.getValue().toString());
                    Log.v("LoopListesi:", "" + list.get(0));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
