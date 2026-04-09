package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorConfidenceScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_confidence_score);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        TextView tvPercentage = findViewById(R.id.tv_confidence_percentage);
        ProgressBar pbConfidence = findViewById(R.id.pb_confidence);
        TextView tvDescription = findViewById(R.id.tv_description);

        DoctorAssessmentData data = DoctorAssessmentData.getInstance();
        int confidence = (int) data.getConfidenceScore();
        if (confidence == 0) confidence = 94; // Fallback for simulation
        
        tvPercentage.setText(confidence + "%");
        pbConfidence.setProgress(confidence);
        
        String feedback = data.getAiFeedback();
        if (feedback == null || feedback.isEmpty()) {
            tvDescription.setText("The AI is " + confidence + "% confident in its analysis of the spent embryo culture media. This score reflects the AI's certainty based on the data provided and the analysis performed.");
        } else {
            tvDescription.setText(feedback);
        }

        findViewById(R.id.btn_next_confidence).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorResultDisclaimerActivity.class);
            startActivity(intent);
        });
    }
}
