package com.example.valetautomationsystem;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView welcomeTextField = null;
    private TextView spaceTextField = null;
    private TextView statusTextField = null;
    private Button qrButton = null;
    private Button prepareCarButton = null;
    private FirebaseDatabase database = null;
    private DatabaseReference reference = null;
    private DatabaseReference reference2 = null;
    private DatabaseReference reference3 = null;
    private String barcodeValue = null;
    private Intent intent = null;
    private StringBuilder stringBuilder = null;
    private String welcomeText = "";
    private Spinner spinner = null;
    private int index = 0;
    private String[] names = {"", "", "", ""};
    private ArrayList<String> freeSpaces = null;
    private ArrayList<String> totalSpaces = null;
    private boolean status = false;
    private final String id = "not";
    private final int not_id = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeTextField = findViewById(R.id.welcomeTextField);
        spaceTextField = findViewById(R.id.spaceTextField);
        statusTextField = findViewById(R.id.statusTextField);
        qrButton = findViewById(R.id.qrButton);
        qrButton.setOnClickListener(this);
        prepareCarButton = findViewById(R.id.prepareCarButton);
        prepareCarButton.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("valets");

        spaceTextField.setText("0/0");

        stringBuilder = new StringBuilder();
        intent = getIntent();
        Log.v("intent message", "mes:" + intent.getStringExtra("zimbirti"));

        //stringBuilder.append("Hi ").append(intent.getStringExtra("zimbirti")).append("! Welcome to VAS.");
        //welcomeText = stringBuilder.toString();
        welcomeTextField.setText("Hi!, welcome to VAS.");

        reference2 = database.getReference("restaurants");
        reference3 = database.getReference("restaurants");

        freeSpaces = new ArrayList<>();
        totalSpaces = new ArrayList<>();
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                int index = 0;
                for (DataSnapshot child : children) {
                    names[index++] = child.getKey();
                    Log.v("yemek", "" + child.toString());
                    String totalSpace = child.child("totalSpace").getValue().toString();
                    String freeSpace = child.child("freeSpace").getValue().toString();
                    totalSpaces.add(totalSpace);
                    freeSpaces.add(freeSpace);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, names);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        checkStatus("");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == qrButton.getId()) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    display();
                }
            }, 10000);


            index = spinner.getSelectedItemPosition();
            Intent intent = new Intent(WelcomeActivity.this, BarcodeScanActivity.class);
            startActivityForResult(intent, 0);
        } else if (v.getId() == prepareCarButton.getId()) {
            freeSpaces.set(index, String.valueOf(Integer.parseInt(freeSpaces.get(index)) + 1));
            reference2.child(names[index]).child("freeSpace").setValue(freeSpaces.get(index));
            if (index == 0)
                spaceTextField.setText(freeSpaces.get(0) + "/" + totalSpaces.get(0));
            else if (index == 1)
                spaceTextField.setText(freeSpaces.get(1) + "/" + totalSpaces.get(1));
            else if (index == 2)
                spaceTextField.setText(freeSpaces.get(2) + "/" + totalSpaces.get(2));
            else if (index == 3)
                spaceTextField.setText(freeSpaces.get(3) + "/" + totalSpaces.get(3));
            Intent intent = new Intent(WelcomeActivity.this, PrepareCarActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    barcodeValue = barcode.displayValue;
                    Log.v("barcode value:", "" + barcodeValue);


                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for (DataSnapshot child : children) {
                                if (child.getValue().equals(barcodeValue)) {
                                    freeSpaces.set(index, String.valueOf(Integer.parseInt(freeSpaces.get(index)) - 1));
                                    reference2.child(names[index]).child("freeSpace").setValue(freeSpaces.get(index));
                                    Log.v("bos deger", "" + freeSpaces.get(index));
                                    Toast.makeText(getApplicationContext(), "valet is found!!!", Toast.LENGTH_LONG).show();
                                    status = true;
                                    checkStatus(child.getValue().toString());
                                    activateButton();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {

                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        final int pos = parent.getSelectedItemPosition();
        Log.v("position", "pos:" + pos);

        if (freeSpaces.size() != 0 && totalSpaces.size() != 0) {
            if (pos == 0)
                spaceTextField.setText(freeSpaces.get(0) + "/" + totalSpaces.get(0));
            else if (pos == 1)
                spaceTextField.setText(freeSpaces.get(1) + "/" + totalSpaces.get(1));
            else if (pos == 2)
                spaceTextField.setText(freeSpaces.get(2) + "/" + totalSpaces.get(2));
            else if (pos == 3)
                spaceTextField.setText(freeSpaces.get(3) + "/" + totalSpaces.get(3));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void checkStatus(String name) {
        if (status) {
            statusTextField.setTextSize(12);
            statusTextField.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            statusTextField.setText("Your car is taken by " + name + " valet.");
            if (index == 0)
                spaceTextField.setText(freeSpaces.get(0) + "/" + totalSpaces.get(0));
            else if (index == 1)
                spaceTextField.setText(freeSpaces.get(1) + "/" + totalSpaces.get(1));
            else if (index == 2)
                spaceTextField.setText(freeSpaces.get(2) + "/" + totalSpaces.get(2));
            else if (index == 3)
                spaceTextField.setText(freeSpaces.get(3) + "/" + totalSpaces.get(3));

        } else
            statusTextField.setText("You are on idle state.");
    }

    public void activateButton() {
        if (status) {
            qrButton.setVisibility(View.INVISIBLE);
            prepareCarButton.setVisibility(View.VISIBLE);
        }
    }

    public void display(){


        Intent notificationIntent = new Intent(this, CarWashActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);
        builder.setSmallIcon(R.drawable.common_google_signin_btn_text_dark);
        builder.setContentTitle("Do you want to wash your car?");
        builder.setContentText("Click to see the prices");
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(not_id, builder.build());


    }

}
