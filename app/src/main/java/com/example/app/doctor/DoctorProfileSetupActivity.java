package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorProfileSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_setup);

        TextView tvWelcome = findViewById(R.id.tv_welcome_doctor);
        String userName = getIntent().getStringExtra("USER_NAME");
        
        if (userName != null && !userName.isEmpty()) {
            // Extracting name from email if needed, or just showing the username
            String displayName = userName.split("@")[0]; // Simple logic to get name before @
            tvWelcome.setText("Welcome, Dr. " + displayName);
        }

        ImageButton btnBack = findViewById(R.id.btn_back_profile);
        btnBack.setOnClickListener(v -> finish());

        Button btnContinue = findViewById(R.id.btn_continue_profile);
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorProfileSetupActivity.this, DoctorProfileDetailsActivity.class);
            startActivity(intent);
        });
    }
}
