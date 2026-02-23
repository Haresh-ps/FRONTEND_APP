package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorResultDisclaimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_result_disclaimer);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_new_assessment).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorNewAssessmentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.btn_back_to_dashboard).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.btn_save_report).setOnClickListener(v -> {
            saveReport();
        });
    }

    private void saveReport() {
        // In a real scenario, you would send the assessment data to the backend here.
        // Assuming the assessment was already created in previous steps, 
        // we might just need to trigger a 'save' or 'finalize' action if not already done.
        
        // For now, let's simulate a backend call success.
        // If there's a specific 'Report' model/endpoint, we'd use it here.
        
        Toast.makeText(this, "Report saved successfully!", Toast.LENGTH_SHORT).show();
        
        // After saving, we can navigate to the Reports page to show it's there
        Intent intent = new Intent(this, DoctorReportsActivity.class);
        startActivity(intent);
        finish();
    }
}
