package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorMetabolicIndicatorActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button btnNext;
    private int progressStatus = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_metabolic_indicator);

        progressBar = findViewById(R.id.pb_metabolic);
        btnNext = findViewById(R.id.btn_next);

        findViewById(R.id.btn_cancel).setOnClickListener(v -> finish());

        // Simulate Metabolic Indicator Extraction
        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;
                handler.post(() -> {
                    progressBar.setProgress(progressStatus);
                    if (progressStatus == 100) {
                        btnNext.setEnabled(true);
                        generateAndSendMetabolicData();
                    }
                });
                try {
                    Thread.sleep(50); // Simulation speed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorAnalysisCompleteActivity.class);
            startActivity(intent);
        });
    }

    private void generateAndSendMetabolicData() {
        // Generate simulated values
        // Glucose: 2.5 - 6.0 range
        float glucose = 2.5f + (float)(Math.random() * 3.5); 
        // Lactate: 1.0 - 3.0 range
        float lactate = 1.0f + (float)(Math.random() * 2.0);
        // Pyruvate: 0.1 - 0.5
        float pyruvate = 0.1f + (float)(Math.random() * 0.4);
        // Oxidative Stress: 1.0 - 10.0
        float stress = 1.0f + (float)(Math.random() * 9.0);

        DoctorAssessmentData data = DoctorAssessmentData.getInstance();
        data.setGlucoseLevel(glucose);
        data.setLactateLevel(lactate);
        data.setPyruvateLevel(pyruvate);
        data.setOxidativeStress(stress);
        
        // Send to Backend
        ApiService apiService = RetrofitClient.getApiService();
        AnalysisRequest request = new AnalysisRequest(glucose, lactate, pyruvate, stress);
        
        if (data.getAssessmentId() != -1) {
            apiService.analyzeAssessment(data.getAssessmentId(), request).enqueue(new retrofit2.Callback<AnalysisResponse>() {
                @Override
                public void onResponse(retrofit2.Call<AnalysisResponse> call, retrofit2.Response<AnalysisResponse> response) {
                   // Success - data saved and analyzed.
                   // We don't need to do anything here strictly, results will be fetched in Results screens OR we can pass them.
                   // Actually, usually results are fetched by ID.
                }

                @Override
                public void onFailure(retrofit2.Call<AnalysisResponse> call, Throwable t) {
                    // Fail silently for simulation or log
                }
            });
        }
    }
}
