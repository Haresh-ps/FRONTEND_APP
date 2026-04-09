package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorAIResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_ai_results);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        DoctorAssessmentData data = DoctorAssessmentData.getInstance();
        
        android.widget.TextView tvId = findViewById(R.id.tv_assessment_id);
        android.widget.TextView tvSummary = findViewById(R.id.tv_summary_text);
        android.widget.TextView tvDate = findViewById(R.id.tv_date);
        android.widget.ImageView ivUploadedImage = findViewById(R.id.iv_uploaded_image);

        // Display Patient ID instead of Assessment ID
        String patientId = data.getPatientId();
        if (patientId != null && !patientId.isEmpty()) {
            tvId.setText("Patient ID: " + patientId);
        } else {
            tvId.setText("Patient ID: N/A");
        }

        // Display uploaded image if available
        String imageUriString = data.getUploadedImageUri();
        if (imageUriString != null && !imageUriString.isEmpty()) {
            ivUploadedImage.setImageURI(android.net.Uri.parse(imageUriString));
        }

        String feedback = data.getAiFeedback();
        if (feedback != null && !feedback.isEmpty()) {
            tvSummary.setText(feedback);
        }

        String date = data.getAssessmentDate();
        if (date == null || date.isEmpty()) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.getDefault());
            date = sdf.format(new java.util.Date());
        }
        tvDate.setText("Date: " + date);

        findViewById(R.id.btn_view_report).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorDetectedComponentsActivity.class);
            startActivity(intent);
        });
    }
}
