package com.example.app.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.app.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorMainActivity extends AppCompatActivity {

    private TextView tvWelcomeName;
    private TextView tvStatTotal, tvStatGood, tvStatModerate, tvStatAvgScore;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        tvWelcomeName = findViewById(R.id.tv_welcome_name);
        tvStatTotal = findViewById(R.id.tv_stat_total);
        tvStatGood = findViewById(R.id.tv_stat_good);
        tvStatModerate = findViewById(R.id.tv_stat_moderate);
        tvStatAvgScore = findViewById(R.id.tv_stat_avg_score);
        lineChart = findViewById(R.id.line_chart);

        updateWelcomeMessage();
        fetchDashboardData();

        View ivChatbot = findViewById(R.id.iv_chatbot);
        if (ivChatbot != null) {
            ivChatbot.setOnClickListener(v -> {
                Intent intent = new Intent(DoctorMainActivity.this, DoctorChatbotActivity.class);
                startActivity(intent);
            });
        }

        View assessmentCard = findViewById(R.id.card_assessment);
        if (assessmentCard != null) {
            assessmentCard.setOnClickListener(v -> {
                Intent intent = new Intent(DoctorMainActivity.this, DoctorNewAssessmentActivity.class);
                startActivity(intent);
            });
        }

        View reportsCard = findViewById(R.id.card_reports);
        if (reportsCard != null) {
            reportsCard.setOnClickListener(v -> {
                Intent intent = new Intent(DoctorMainActivity.this, DoctorReportsActivity.class);
                startActivity(intent);
            });
        }

        View profileCard = findViewById(R.id.card_profile_box);
        if (profileCard != null) {
            profileCard.setOnClickListener(v -> {
                Intent intent = new Intent(DoctorMainActivity.this, DoctorProfileActivity.class);
                startActivity(intent);
            });
        }

        View btnHome = findViewById(R.id.btn_home);
        if (btnHome != null) {
            btnHome.setOnClickListener(v -> {
                // Already on Home
            });
        }

        View btnAddAssessment = findViewById(R.id.btn_add_assessment);
        if (btnAddAssessment != null) {
            btnAddAssessment.setOnClickListener(v -> {
                Intent intent = new Intent(DoctorMainActivity.this, DoctorNewAssessmentActivity.class);
                startActivity(intent);
            });
        }

        View btnReports = findViewById(R.id.btn_reports);
        if (btnReports != null) {
            btnReports.setOnClickListener(v -> {
                Intent intent = new Intent(DoctorMainActivity.this, DoctorReportsActivity.class);
                startActivity(intent);
            });
        }

        View btnProfile = findViewById(R.id.btn_profile);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                Intent intent = new Intent(DoctorMainActivity.this, DoctorProfileActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWelcomeMessage();
        fetchDashboardData();
    }

    private void fetchDashboardData() {
        ApiService apiService = RetrofitClient.getApiService(this);
        apiService.listReports().enqueue(new Callback<List<AssessmentResponse>>() {
            @Override
            public void onResponse(Call<List<AssessmentResponse>> call, Response<List<AssessmentResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AssessmentResponse> reports = response.body();
                    updateStatistics(reports);
                    setupLineChart(reports);
                }
            }

            @Override
            public void onFailure(Call<List<AssessmentResponse>> call, Throwable t) {
                if (!isFinishing()) {
                    Toast.makeText(DoctorMainActivity.this, "Error fetching data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateStatistics(List<AssessmentResponse> reports) {
        int total = reports.size();
        int goodCount = 0;
        int moderateCount = 0;
        double sumScore = 0;
        int countWithScore = 0;

        for (AssessmentResponse report : reports) {
            if (report.analysis != null && !report.analysis.isEmpty()) {
                double score = report.analysis.get(0).confidenceScore;
                sumScore += score;
                countWithScore++;

                if (score >= 80) {
                    goodCount++;
                } else if (score >= 60) {
                    moderateCount++;
                }
            }
        }

        if (tvStatTotal != null) tvStatTotal.setText(String.valueOf(total));
        if (tvStatGood != null) tvStatGood.setText(String.valueOf(goodCount));
        if (tvStatModerate != null) tvStatModerate.setText(String.valueOf(moderateCount));
        
        if (tvStatAvgScore != null) {
            if (countWithScore > 0) {
                int avgScore = (int) (sumScore / countWithScore);
                tvStatAvgScore.setText(avgScore + "%");
            } else {
                tvStatAvgScore.setText("0%");
            }
        }
    }

    private void setupLineChart(List<AssessmentResponse> reports) {
        if (lineChart == null) return;

        List<Entry> entries = new ArrayList<>();
        List<AssessmentResponse> sortedReports = new ArrayList<>(reports);
        
        // Use Collections.sort for better compatibility
        Collections.sort(sortedReports, (r1, r2) -> Integer.compare(r1.id, r2.id));

        for (int i = 0; i < sortedReports.size(); i++) {
            AssessmentResponse report = sortedReports.get(i);
            if (report.analysis != null && !report.analysis.isEmpty()) {
                entries.add(new Entry(i, (float) report.analysis.get(0).confidenceScore));
            }
        }

        if (entries.isEmpty()) {
            lineChart.setNoDataText("No assessment data available");
            lineChart.invalidate();
            return;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Confidence Score (%)");
        dataSet.setColor(Color.parseColor("#4F46E5"));
        dataSet.setCircleColor(Color.parseColor("#4F46E5"));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawCircleHole(true);
        dataSet.setValueTextSize(10f);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#EEF2FF"));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Customize Chart
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < sortedReports.size()) {
                    String date = sortedReports.get(index).createdAt;
                    if (date != null && date.contains("T")) {
                        try {
                            return date.split("T")[0].substring(5); // Show MM-DD
                        } catch (Exception e) {
                            return "";
                        }
                    }
                }
                return "";
            }
        });

        lineChart.animateY(1000);
        lineChart.invalidate();
    }

    private void updateWelcomeMessage() {
        SharedPreferences sharedPreferences = getSharedPreferences("DoctorProfile", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        if (tvWelcomeName != null && !name.isEmpty()) {
            tvWelcomeName.setText("Welcome, Dr. " + name);
        }
    }
}
