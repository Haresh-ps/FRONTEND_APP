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
        // Results are already saved to the backend during analyzeAssessment call.
        // We just need to finalize on the client side and clear the current session.
        
        Toast.makeText(this, "Report saved successfully!", Toast.LENGTH_SHORT).show();
        
        // Clear current assessment data for next time
        DoctorAssessmentData.getInstance().clear();
        
        // Navigate to the Reports page to show it's there
        Intent intent = new Intent(this, DoctorReportsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
