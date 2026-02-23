package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorReadyAnalyzeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_ready_analyze);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        TextView tvId = findViewById(R.id.tv_patient_id_value);
        TextView tvName = findViewById(R.id.tv_patient_name_value);
        TextView tvDob = findViewById(R.id.tv_patient_dob_value);
        TextView tvAge = findViewById(R.id.tv_patient_age_value);
        TextView tvEmbryoDay = findViewById(R.id.tv_embryo_day_value);
        TextView tvDuration = findViewById(R.id.tv_duration_value);

        // Retrieving data from the shared assessment data singleton
        DoctorAssessmentData data = DoctorAssessmentData.getInstance();
        
        tvId.setText(data.getPatientId());
        tvName.setText(data.getPatientName());
        tvDob.setText(data.getPatientDob());
        tvAge.setText(String.valueOf(data.getPatientAge()));
        tvEmbryoDay.setText(data.getEmbryoDay());
        tvDuration.setText(data.getCultureDuration());

        findViewById(R.id.btn_analyze).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorAIProcessingActivity.class);
            startActivity(intent);
        });
    }
}
