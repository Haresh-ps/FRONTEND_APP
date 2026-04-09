package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import com.google.android.material.card.MaterialCardView;

public class DoctorReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_reports);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Setup Floating Action Button (Plus icon)
        findViewById(R.id.fab_add_report).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorNewAssessmentActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_nav_dashboard).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.btn_nav_new).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorNewAssessmentActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_nav_profile).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchReports();
    }

    private void fetchReports() {
        LinearLayout container = findViewById(R.id.ll_reports_container);
        if (container == null) return;
        
        container.removeAllViews(); // Clear previous items (including hardcoded ones if any)
        
        ApiService apiService = RetrofitClient.getApiService();
        apiService.listReports().enqueue(new retrofit2.Callback<java.util.List<AssessmentResponse>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.List<AssessmentResponse>> call, retrofit2.Response<java.util.List<AssessmentResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (AssessmentResponse report : response.body()) {
                        addReportToUI(container, report);
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.List<AssessmentResponse>> call, Throwable t) {
                android.widget.Toast.makeText(DoctorReportsActivity.this, "Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addReportToUI(LinearLayout container, AssessmentResponse report) {
        View view = getLayoutInflater().inflate(R.layout.item_report, container, false);
        MaterialCardView card = view.findViewById(R.id.card_report_item);
        
        TextView tvName = view.findViewById(R.id.tv_patient_name);
        TextView tvIdBadge = view.findViewById(R.id.tv_patient_id_badge);
        TextView tvDate = view.findViewById(R.id.tv_report_date);
        
        if (tvName != null) tvName.setText(report.patientName != null ? report.patientName : "Unknown Patient");
        if (tvIdBadge != null) tvIdBadge.setText("ID: " + (report.patientId != null ? report.patientId : "N/A"));
        if (tvDate != null) {
            String dateStr = "Created: " + (report.createdAt != null ? report.createdAt.split("T")[0] : "N/A");
            tvDate.setText(dateStr);
        }

        card.setOnClickListener(v -> {
            // Populate DoctorAssessmentData singleton with the report's data
            DoctorAssessmentData data = DoctorAssessmentData.getInstance();
            data.clear();
            data.setAssessmentId(report.id);
            data.setPatientId(report.patientId != null ? report.patientId : "");
            data.setPatientName(report.patientName != null ? report.patientName : "");
            data.setPatientDob(report.patientDob != null ? report.patientDob : "");
            data.setPatientAge(report.patientAge);
            data.setEmbryoCount(report.embryoCount);
            data.setEmbryoDay(report.embryoDay != null ? report.embryoDay : "");
            data.setCultureDuration(report.cultureDuration != null ? report.cultureDuration : "");
            data.setQuestionAnswer("doctor_notes", report.doctorNotes != null ? report.doctorNotes : "");
            
            if (report.analysis != null && !report.analysis.isEmpty()) {
                AssessmentResponse.AnalysisData analysis = report.analysis.get(0);
                data.setConfidenceScore(analysis.confidenceScore);
                data.setViabilityPrediction(analysis.viabilityPrediction);
                data.setAiFeedback(analysis.aiFeedback);
                data.setGlucoseLevel(analysis.glucoseLevel);
                data.setLactateLevel(analysis.lactateLevel);
                data.setPyruvateLevel(analysis.pyruvateLevel);
                data.setOxidativeStress(analysis.oxidativeStress);
                data.setAminoAcids(analysis.aminoAcids);
                data.setVitamins(analysis.vitamins);
                data.setAmmonia(analysis.ammonia);
                data.setPhChange(analysis.phChange);
                data.setOxygenUptake(analysis.oxygenUptake);
                data.setCo2Release(analysis.co2Release);
            }

            Intent intent = new Intent(this, DoctorResultsReportActivity.class);
            startActivity(intent);
        });

        container.addView(view);
    }
}
