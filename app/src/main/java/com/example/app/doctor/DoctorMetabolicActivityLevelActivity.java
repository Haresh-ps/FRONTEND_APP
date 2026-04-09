package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorMetabolicActivityLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_metabolic_activity_level);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        TextView tvLevel = findViewById(R.id.tv_level_heading);
        ProgressBar pbLevel = findViewById(R.id.pb_activity_level);
        TextView tvDesc = findViewById(R.id.tv_description);

        // Logic to determine activity level from real AI results
        DoctorAssessmentData data = DoctorAssessmentData.getInstance();
        String level = data.getViabilityPrediction(); 
        if (level == null || level.isEmpty()) level = "Moderate Viability"; 

        tvLevel.setText(level);

        if (level.contains("Moderate")) {
            pbLevel.setProgress(50);
            tvDesc.setText("The embryo's metabolic activity is within the expected range, indicating healthy development. Continue monitoring for any significant changes.");
        } else if (level.contains("Good")) {
            pbLevel.setProgress(85);
            tvDesc.setText("The embryo shows good metabolic activity, suggesting robust growth. High probability of continued healthy progression.");
        } else if (level.contains("Low") || level.contains("Poor")) {
            pbLevel.setProgress(25);
            tvDesc.setText("Reduced metabolic activity detected. The embryo may require further stabilization or extended culture time.");
        } else {
            pbLevel.setProgress(70);
            tvDesc.setText("General assessment indicates stable developmental markers. Proceed with standard clinical protocol.");
        }

        findViewById(R.id.btn_next).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorConfidenceScoreActivity.class);
            startActivity(intent);
        });
    }
}
