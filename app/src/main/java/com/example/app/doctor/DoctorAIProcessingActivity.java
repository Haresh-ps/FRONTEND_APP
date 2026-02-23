package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorAIProcessingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button btnNext;
    private int progressStatus = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_ai_processing);

        progressBar = findViewById(R.id.pb_ai_processing);
        btnNext = findViewById(R.id.btn_next);

        // Simulate AI Processing
        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;
                handler.post(() -> {
                    progressBar.setProgress(progressStatus);
                    if (progressStatus == 100) {
                        btnNext.setEnabled(true);
                    }
                });
                try {
                    Thread.sleep(60); // Processing speed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorMetabolicIndicatorActivity.class);
            startActivity(intent);
        });
    }
}
