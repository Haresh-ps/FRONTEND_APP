package com.example.app.doctor;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorAppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_app_info);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        TextView tvBuildDate = findViewById(R.id.tv_build_date);
        tvBuildDate.setText("2026-02-07");
    }
}
