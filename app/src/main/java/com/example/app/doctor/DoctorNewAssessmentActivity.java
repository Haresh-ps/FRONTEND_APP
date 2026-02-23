package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorNewAssessmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_new_assessment);

        ImageButton btnClose = findViewById(R.id.btn_close_assessment);
        btnClose.setOnClickListener(v -> finish());

        Button btnStart = findViewById(R.id.btn_start_assessment);
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorNewAssessmentActivity.this, DoctorPatientDetailsActivity.class);
            startActivity(intent);
        });
    }
}
