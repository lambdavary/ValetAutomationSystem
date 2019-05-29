package com.example.valetautomationsystem;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ValetMainActivity extends AppCompatActivity {

    private String valetUsername = null;
    private TextView valetWelcomeTextField = null;
    private Intent intent = null;
    private final String id = "not";
    private final int not_id = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valet_main);

        valetWelcomeTextField = findViewById(R.id.valetWelcomeTextField);
        intent = getIntent();
        valetUsername = intent.getStringExtra("valetUsername");
        valetWelcomeTextField.setText("Hi Tarik!, welcome to VAS.");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                display();
            }
        }, 5000);

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
        notificationManager.notify(not_id, builder.build());


    }
}
