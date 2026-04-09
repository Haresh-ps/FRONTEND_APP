package com.example.app.doctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientDashboardActivity extends AppCompatActivity {

    private static final String TAG = "PatientDashboard";
    private String patientId;
    private LinearLayout reportsContainer;
    private TextView tvPatientName, tvCurrentScore, tvWelcome;
    private LineGraphView lineGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        patientId = getIntent().getStringExtra("patient_id");
        if (patientId != null) patientId = patientId.trim();

        tvPatientName = findViewById(R.id.tv_patient_name);
        tvCurrentScore = findViewById(R.id.tv_current_score);
        tvWelcome = findViewById(R.id.tv_welcome);
        lineGraphView = findViewById(R.id.line_graph_view);
        reportsContainer = findViewById(R.id.layout_reports_container);
        View btnSettings = findViewById(R.id.btn_patient_settings);

        btnSettings.setOnClickListener(v -> showSettingsBottomSheet());

        fetchAndFilterReports();
    }

    private void showSettingsBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.layout_patient_settings_bottom_sheet, null);
        
        TextView tvDoctorName = view.findViewById(R.id.tv_bs_doctor_name);
        TextView tvDoctorPhone = view.findViewById(R.id.tv_bs_doctor_phone);
        View btnLogout = view.findViewById(R.id.btn_bs_logout);
        View btnCall = view.findViewById(R.id.btn_call_doctor);

        SharedPreferences prefs = getSharedPreferences("DoctorProfile", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "Ethan Carter");
        String phone = prefs.getString("phone", "+1 (555) 123-4567");

        String displayName = name;
        if (name != null && !name.trim().toLowerCase().startsWith("dr.")) {
            displayName = "Dr. " + name;
        }

        tvDoctorName.setText(displayName);
        tvDoctorPhone.setText(phone);

        btnLogout.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            logout();
        });

        btnCall.setOnClickListener(v -> {
            if (phone != null && !phone.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void logout() {
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PatientDashboardActivity.this, DoctorLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void fetchAndFilterReports() {
        ApiService apiService = RetrofitClient.getApiService(this);
        apiService.listReports().enqueue(new Callback<List<AssessmentResponse>>() {
            @Override
            public void onResponse(Call<List<AssessmentResponse>> call, Response<List<AssessmentResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AssessmentResponse> allReports = response.body();
                    List<AssessmentResponse> filteredReports = new ArrayList<>();

                    for (AssessmentResponse report : allReports) {
                        if (isMatchingPatient(report)) {
                            filteredReports.add(report);
                        }
                    }
                    
                    Collections.sort(filteredReports, (r1, r2) -> {
                        if (r1.createdAt == null || r2.createdAt == null) return 0;
                        return r1.createdAt.compareTo(r2.createdAt);
                    });

                    updateUI(filteredReports);
                } else {
                    Log.e(TAG, "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AssessmentResponse>> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
            }
        });
    }

    private boolean isMatchingPatient(AssessmentResponse report) {
        if (report.patientId == null || patientId == null) return false;
        return report.patientId.trim().equalsIgnoreCase(patientId);
    }

    private void updateUI(List<AssessmentResponse> reports) {
        if (reports.isEmpty()) {
            if (tvWelcome != null) tvWelcome.setText("No records found");
            tvPatientName.setText("N/A");
            tvCurrentScore.setText("0%");
            lineGraphView.setData(new ArrayList<>(), new ArrayList<>());
            reportsContainer.removeAllViews();
            return;
        }

        if (tvWelcome != null) tvWelcome.setText("Welcome,");

        AssessmentResponse latest = reports.get(reports.size() - 1);
        tvPatientName.setText(latest.patientName != null ? latest.patientName : "Patient");
        
        List<Float> confidenceScores = new ArrayList<>();
        List<String> reportDates = new ArrayList<>();
        
        for (AssessmentResponse report : reports) {
            if (report.analysis != null && !report.analysis.isEmpty()) {
                confidenceScores.add(report.analysis.get(0).confidenceScore);
                
                String dateStr = "N/A";
                if (report.createdAt != null) {
                    try {
                        String rawDate = report.createdAt.split("T")[0];
                        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(rawDate);
                        dateStr = new SimpleDateFormat("dd/MM", Locale.US).format(d);
                    } catch (Exception e) {
                        dateStr = report.createdAt.substring(0, Math.min(report.createdAt.length(), 5));
                    }
                }
                reportDates.add(dateStr);
            }
        }
        
        if (!confidenceScores.isEmpty()) {
            tvCurrentScore.setText((int)(float)confidenceScores.get(confidenceScores.size() - 1) + "%");
            lineGraphView.setData(confidenceScores, reportDates);
        }

        reportsContainer.removeAllViews();
        List<AssessmentResponse> listOrder = new ArrayList<>(reports);
        Collections.reverse(listOrder);
        for (AssessmentResponse report : listOrder) {
            addReportCard(report);
        }
    }

    private void addReportCard(AssessmentResponse report) {
        View card = getLayoutInflater().inflate(R.layout.item_report, reportsContainer, false);
        TextView tvTitle = card.findViewById(R.id.tv_patient_name);
        TextView tvDate = card.findViewById(R.id.tv_report_date);
        TextView tvBadge = card.findViewById(R.id.tv_patient_id_badge);

        tvTitle.setText("AI Analysis Report");
        
        String displayDate = report.createdAt != null ? report.createdAt.split("T")[0] : "N/A";
        try {
            if (report.createdAt != null) {
                Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(displayDate);
                displayDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(d);
            }
        } catch (Exception ignored) {}

        tvDate.setText("Date: " + displayDate);
        tvBadge.setText("ID: " + report.id);

        card.setOnClickListener(v -> {
            DoctorAssessmentData data = DoctorAssessmentData.getInstance();
            data.clear();
            data.setAssessmentId(report.id);
            data.setPatientId(report.patientId);
            data.setPatientName(report.patientName);
            data.setPatientDob(report.patientDob);
            data.setPatientAge(report.patientAge);
            data.setEmbryoCount(report.embryoCount);
            data.setEmbryoDay(report.embryoDay);
            data.setCultureDuration(report.cultureDuration);
            data.setQuestionAnswer("doctor_notes", report.doctorNotes);
            
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

        reportsContainer.addView(card);
    }
}
