package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorWorkflowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_clinicworkflow);

        Button btnContinue = findViewById(R.id.btn_continue_workflow);
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorWorkflowActivity.this, DoctorDisclaimerActivity.class);
            startActivity(intent);
        });
    }
}
