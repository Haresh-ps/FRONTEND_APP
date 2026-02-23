package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorComponentIdentificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_component_identification);

        ImageButton btnClose = findViewById(R.id.btn_close_component);
        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorComponentIdentificationActivity.this, DoctorGetStartedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        Button btnNext = findViewById(R.id.btn_next_component);
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorComponentIdentificationActivity.this, DoctorWorkflowActivity.class);
            startActivity(intent);
        });
    }
}
