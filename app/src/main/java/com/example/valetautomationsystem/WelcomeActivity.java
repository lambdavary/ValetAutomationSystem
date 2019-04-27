package com.example.valetautomationsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button qrButton = null;
    private TextView qrCode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        qrButton = findViewById(R.id.qrButton);
        qrButton.setOnClickListener(this);
        qrCode = findViewById(R.id.qrCode);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == qrButton.getId()){
            Intent intent = new Intent(WelcomeActivity.this, BarcodeScanActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0){
            if (resultCode == CommonStatusCodes.SUCCESS){
                if (data!= null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    qrCode.setText("barcode value is:" + barcode.displayValue);
                }else {
                    qrCode.setText("no result!");
                }
            }
        }
    }
}
