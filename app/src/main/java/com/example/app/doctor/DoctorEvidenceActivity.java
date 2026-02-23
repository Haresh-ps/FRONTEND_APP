package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorEvidenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_evidence);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_continue_evidence).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorUploadPicActivity.class);
            startActivity(intent);
        });
    }
}
