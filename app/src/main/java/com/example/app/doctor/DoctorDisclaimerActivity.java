package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorDisclaimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_disclaimer);

        ImageButton btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorDisclaimerActivity.this, DoctorGetStartedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorDisclaimerActivity.this, DoctorWorkflowActivity.class);
            startActivity(intent);
            finish();
        });

        Button btnUnderstand = findViewById(R.id.btn_understand);
        btnUnderstand.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorDisclaimerActivity.this, DoctorLoginActivity.class);
            startActivity(intent);
        });
    }
}
