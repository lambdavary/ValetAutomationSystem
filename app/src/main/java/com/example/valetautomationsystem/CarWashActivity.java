package com.example.valetautomationsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class CarWashActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinner = null;
    private String[] names = {"Gold Plus Wash", "Polish Plus Wash", "Eco Wash"};
    private Button button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash);

        spinner = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, names);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        button = findViewById(R.id.button);

        button.setOnClickListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        final int pos = parent.getSelectedItemPosition();
        Log.v("position", "pos:" + pos);

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == button.getId()){
            Toast.makeText(getApplicationContext(), "Your car will be wash" , Toast.LENGTH_LONG).show();
        }
    }
}
