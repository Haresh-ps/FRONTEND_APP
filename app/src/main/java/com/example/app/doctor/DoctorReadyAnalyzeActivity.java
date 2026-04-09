package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
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
        
        if (tvId != null) tvId.setText(data.getPatientId());
        if (tvName != null) tvName.setText(data.getPatientName());
        if (tvDob != null) tvDob.setText(data.getPatientDob());
        if (tvAge != null) tvAge.setText(String.valueOf(data.getPatientAge()));
        if (tvEmbryoDay != null) tvEmbryoDay.setText(data.getEmbryoDay());
        if (tvDuration != null) tvDuration.setText(data.getCultureDuration());

        findViewById(R.id.btn_analyze).setOnClickListener(v -> {
            if (data.getAssessmentId() != -1) {
                // Assessment already created in Review stage, proceed to AI Processing
                Intent intent = new Intent(DoctorReadyAnalyzeActivity.this, DoctorAIProcessingActivity.class);
                startActivity(intent);
            } else {
                // Fallback for cases where it wasn't created yet
                createAssessmentOnBackend();
            }
        });
    }

    private void createAssessmentOnBackend() {
        DoctorAssessmentData data = DoctorAssessmentData.getInstance();
        ApiService apiService = RetrofitClient.getApiService();

        AssessmentRequest request = new AssessmentRequest(
                data.getPatientId(),
                data.getPatientName(),
                data.getPatientDob(),
                data.getPatientAge(),
                data.getEmbryoCount(),
                data.getEmbryoDay(),
                data.getCultureDuration(),
                data.getQuestionsData()
        );

        apiService.createAssessment(request).enqueue(new retrofit2.Callback<AssessmentResponse>() {
            @Override
            public void onResponse(retrofit2.Call<AssessmentResponse> call, retrofit2.Response<AssessmentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setAssessmentId(response.body().id);
                    Intent intent = new Intent(DoctorReadyAnalyzeActivity.this, DoctorAIProcessingActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(DoctorReadyAnalyzeActivity.this, "Failed to create assessment: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AssessmentResponse> call, Throwable t) {
                Toast.makeText(DoctorReadyAnalyzeActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
