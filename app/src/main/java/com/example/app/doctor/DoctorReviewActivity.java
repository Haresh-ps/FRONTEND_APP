package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_review);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Setup Edit button listeners to navigate back to specific questions
        findViewById(R.id.iv_edit_q1).setOnClickListener(v -> navigateTo(DoctorQuestion1Activity.class));
        findViewById(R.id.iv_edit_q2).setOnClickListener(v -> navigateTo(DoctorQuestion2Activity.class));
        findViewById(R.id.iv_edit_q3).setOnClickListener(v -> navigateTo(DoctorQuestion3Activity.class));
        findViewById(R.id.iv_edit_q4).setOnClickListener(v -> navigateTo(DoctorQuestion4Activity.class));
        findViewById(R.id.iv_edit_q5).setOnClickListener(v -> navigateTo(DoctorQuestion5Activity.class));
        findViewById(R.id.iv_edit_q6).setOnClickListener(v -> navigateTo(DoctorQuestion6Activity.class));

        findViewById(R.id.btn_continue).setOnClickListener(v -> {
            DoctorAssessmentData data = DoctorAssessmentData.getInstance();
            
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

            ApiService apiService = RetrofitClient.getApiService();
            retrofit2.Call<AssessmentResponse> call = apiService.createAssessment(request);
            
            call.enqueue(new retrofit2.Callback<AssessmentResponse>() {
                @Override
                public void onResponse(retrofit2.Call<AssessmentResponse> call, retrofit2.Response<AssessmentResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // SAVE THE ID!
                        data.setAssessmentId(response.body().id);
                        android.widget.Toast.makeText(DoctorReviewActivity.this, "Assessment Created! ID: " + response.body().id, android.widget.Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DoctorReviewActivity.this, DoctorEvidenceActivity.class);
                        startActivity(intent);
                    } else {
                        android.widget.Toast.makeText(DoctorReviewActivity.this, "Failed to create assessment: " + response.message(), android.widget.Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<AssessmentResponse> call, Throwable t) {
                    android.widget.Toast.makeText(DoctorReviewActivity.this, "Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
