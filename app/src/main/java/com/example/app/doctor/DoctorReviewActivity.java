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
        
        displayData();

        // Setup Edit button listeners to navigate back to specific questions
        findViewById(R.id.iv_edit_q1).setOnClickListener(v -> navigateTo(DoctorQuestion1Activity.class));
        findViewById(R.id.iv_edit_q2).setOnClickListener(v -> navigateTo(DoctorQuestion2Activity.class));
        findViewById(R.id.iv_edit_q3).setOnClickListener(v -> navigateTo(DoctorQuestion3Activity.class));
        findViewById(R.id.iv_edit_q4).setOnClickListener(v -> navigateTo(DoctorQuestion4Activity.class));
        findViewById(R.id.iv_edit_q5).setOnClickListener(v -> navigateTo(DoctorQuestion5Activity.class));
        findViewById(R.id.iv_edit_q6).setOnClickListener(v -> navigateTo(DoctorQuestion6Activity.class));
        findViewById(R.id.iv_edit_notes).setOnClickListener(v -> navigateTo(DoctorNotesActivity.class));

        findViewById(R.id.btn_continue).setOnClickListener(v -> {
            DoctorAssessmentData data = DoctorAssessmentData.getInstance();
            
            if (data.getAssessmentId() != -1) {
                // Already created, move to Evidence
                Intent intent = new Intent(DoctorReviewActivity.this, DoctorEvidenceActivity.class);
                startActivity(intent);
                return;
            }

            // Disable button to prevent double-clicks
            v.setEnabled(false);

            // Defensively fallback if user hot-reloaded past the Patient Details screen
            String pId = data.getPatientId();
            if (pId == null || pId.trim().isEmpty()) pId = "Unknown ID";
            
            String pName = data.getPatientName();
            if (pName == null || pName.trim().isEmpty()) pName = "Unknown Patient";
            
            String pDob = data.getPatientDob();
            if (pDob == null || pDob.trim().isEmpty()) pDob = "01/01/2000";

            AssessmentRequest request = new AssessmentRequest(
                pId,
                pName,
                pDob,
                data.getPatientAge() == 0 ? 30 : data.getPatientAge(),
                data.getEmbryoCount() == 0 ? 1 : data.getEmbryoCount(),
                data.getEmbryoDay(),
                data.getCultureDuration(),
                data.getQuestionsData()
            );

            ApiService apiService = RetrofitClient.getApiService(DoctorReviewActivity.this);
            retrofit2.Call<AssessmentResponse> call = apiService.createAssessment(request);
            
            call.enqueue(new retrofit2.Callback<AssessmentResponse>() {
                @Override
                public void onResponse(retrofit2.Call<AssessmentResponse> call, retrofit2.Response<AssessmentResponse> response) {
                    v.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null) {
                        // SAVE THE ID!
                        data.setAssessmentId(response.body().id);
                        android.widget.Toast.makeText(DoctorReviewActivity.this, "Assessment Created!", android.widget.Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DoctorReviewActivity.this, DoctorEvidenceActivity.class);
                        startActivity(intent);
                    } else {
                        String errorMsg = "Failed to create assessment";
                        try {
                            if (response.errorBody() != null) {
                                errorMsg = response.errorBody().string();
                            }
                        } catch (Exception e) {}
                        android.widget.Toast.makeText(DoctorReviewActivity.this, errorMsg, android.widget.Toast.LENGTH_LONG).show();
                    }
                }
                public void onFailure(retrofit2.Call<AssessmentResponse> call, Throwable t) {
                    v.setEnabled(true);
                    android.widget.Toast.makeText(DoctorReviewActivity.this, "Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void displayData() {
        DoctorAssessmentData data = DoctorAssessmentData.getInstance();
        
        android.widget.TextView tvEmbryoDay = findViewById(R.id.tv_review_embryo_day);
        android.widget.TextView tvDuration = findViewById(R.id.tv_review_duration);
        android.widget.TextView tvMediaType = findViewById(R.id.tv_review_media_type);
        android.widget.TextView tvColourChange = findViewById(R.id.tv_review_colour);
        android.widget.TextView tvPh = findViewById(R.id.tv_review_ph);
        android.widget.TextView tvVisualClarity = findViewById(R.id.tv_review_visual);
        android.widget.TextView tvNotes = findViewById(R.id.tv_review_notes);

        tvEmbryoDay.setText(data.getEmbryoDay());
        tvDuration.setText(data.getCultureDuration());
        tvMediaType.setText(data.getQuestionAnswer("culture_medium"));
        tvColourChange.setText(data.getQuestionAnswer("media_color_change"));
        tvPh.setText(data.getQuestionAnswer("ph_deviation"));
        tvVisualClarity.setText(data.getQuestionAnswer("visual_clarity"));
        tvNotes.setText(data.getQuestionAnswer("notes").isEmpty() ? "" : data.getQuestionAnswer("notes"));
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
