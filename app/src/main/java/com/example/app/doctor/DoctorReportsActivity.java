package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_reports);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        LinearLayout btnDashboard = findViewById(R.id.btn_nav_dashboard);
        btnDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        LinearLayout btnNew = findViewById(R.id.btn_nav_new);
        btnNew.setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorNewAssessmentActivity.class);
            startActivity(intent);
        });

        // Set listeners for each report item
        setupReportItem(R.id.rl_report_1);
        setupReportItem(R.id.rl_report_2);
        setupReportItem(R.id.rl_report_3);
        setupReportItem(R.id.rl_report_4);
    }

    private void setupReportItem(int resId) {
        RelativeLayout reportItem = findViewById(resId);
        if (reportItem != null) {
            reportItem.setOnClickListener(v -> {
                Intent intent = new Intent(this, DoctorResultsReportActivity.class);
                startActivity(intent);
            });
        }
    }
}
