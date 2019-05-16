package com.example.valetautomationsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView welcomeTextField = null;
    private TextView spaceTextField = null;
    private TextView statusTextField = null;
    private Button qrButton = null;
    private Button prepareCarButton = null;
    private FirebaseDatabase database = null;
    private DatabaseReference reference = null;
    private String barcodeValue = null;
    private Intent intent = null;
    private StringBuilder stringBuilder = null;
    private String welcomeText = "";
    private Spinner spinner = null;
    private String[] names = {"New York", "Las Vegas", "Los Angles", "San Francisco"};
    private boolean status = false;

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

        stringBuilder = new StringBuilder();
        intent = getIntent();
        Log.v("intent message", "mes:" + intent.getStringExtra("zimbirti"));

        stringBuilder.append("Hi ").append(intent.getStringExtra("zimbirti")).append("! Welcome to VAS.");
        welcomeText = stringBuilder.toString();
        welcomeTextField.setText(welcomeText);


        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, names);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        checkStatus("");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == qrButton.getId()) {
            Intent intent = new Intent(WelcomeActivity.this, BarcodeScanActivity.class);
            startActivityForResult(intent, 0);
        }else if (v.getId() == prepareCarButton.getId()){
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

        int pos = parent.getSelectedItemPosition();
        Log.v("position", "pos:" + pos);
        if (pos == 0)
            spaceTextField.setText("35/50");
        else if (pos == 1)
            spaceTextField.setText("25 / 35");
        else if (pos == 2)
            spaceTextField.setText("10 / 20");
        else if (pos == 3)
            spaceTextField.setText("100/150");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void checkStatus(String name){
        if (status){
            statusTextField.setTextSize(12);
            statusTextField.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            statusTextField.setText("Your car is taken by " + name + " valet.");
        } else
            statusTextField.setText("You are on idle state.");
    }

    public void activateButton(){
        if (status){
            qrButton.setVisibility(View.INVISIBLE);
            prepareCarButton.setVisibility(View.VISIBLE);
        }
    }

}
