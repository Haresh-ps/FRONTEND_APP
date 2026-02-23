package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorAnalysisCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_analysis_complete);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_view_results).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorAIResultsActivity.class);
            startActivity(intent);
        });
    }
}
