package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorUploadProgressActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvPercentage;
    private Button btnNext;
    private int progressStatus = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_upload_progress);

        progressBar = findViewById(R.id.progress_bar);
        tvPercentage = findViewById(R.id.tv_percentage);
        btnNext = findViewById(R.id.btn_next);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Simulate upload progress
        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;
                handler.post(() -> {
                    progressBar.setProgress(progressStatus);
                    tvPercentage.setText(progressStatus + "%");
                    if (progressStatus == 100) {
                        btnNext.setEnabled(true);
                    }
                });
                try {
                    Thread.sleep(50); // Adjust speed of progress
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorReadyAnalyzeActivity.class);
            startActivity(intent);
        });
    }
}
