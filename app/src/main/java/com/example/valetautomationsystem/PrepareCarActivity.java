package com.example.valetautomationsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class PrepareCarActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView timerTextField = null;
    private TextView stateTextField = null;
    private Button carTakenButton = null;
    private long mTimeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_car);

        carTakenButton = findViewById(R.id.carTakenButton);
        carTakenButton.setOnClickListener(this);
        timerTextField = findViewById(R.id.timerTextField);
        stateTextField = findViewById(R.id.stateTextField);
        mTimeLeftInMillis = 120000;

        new CountDownTimer(mTimeLeftInMillis, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {

                timerTextField.setText("times up!");
                stateTextField.setText("Your car is ready you can take it!");
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == carTakenButton.getId()){
            Intent intent = new Intent(PrepareCarActivity.this, CommentActivity.class);
            startActivity(intent);
        }
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timerTextField.setText(timeLeftFormatted);
    }
}
