package com.example.app.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.app.R;

public class DoctorMainActivity extends AppCompatActivity {

    private TextView tvWelcomeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        tvWelcomeName = findViewById(R.id.tv_welcome_name);
        updateWelcomeMessage();

        ImageView ivChatbot = findViewById(R.id.iv_chatbot);
        ivChatbot.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorMainActivity.this, DoctorChatbotActivity.class);
            startActivity(intent);
        });

        CardView assessmentCard = findViewById(R.id.card_assessment);
        assessmentCard.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorMainActivity.this, DoctorNewAssessmentActivity.class);
            startActivity(intent);
        });

        CardView reportsCard = findViewById(R.id.card_reports);
        reportsCard.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorMainActivity.this, DoctorReportsActivity.class);
            startActivity(intent);
        });

        CardView profileCard = findViewById(R.id.card_profile_box);
        if (profileCard != null) {
            profileCard.setOnClickListener(v -> {
                Intent intent = new Intent(DoctorMainActivity.this, DoctorProfileActivity.class);
                startActivity(intent);
            });
        }

        ImageButton btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(v -> {
             // Already on Home
        });

        ImageButton btnAddAssessment = findViewById(R.id.btn_add_assessment);
        btnAddAssessment.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorMainActivity.this, DoctorNewAssessmentActivity.class);
            startActivity(intent);
        });

        ImageButton btnReports = findViewById(R.id.btn_reports);
        btnReports.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorMainActivity.this, DoctorReportsActivity.class);
            startActivity(intent);
        });

        ImageButton btnProfile = findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorMainActivity.this, DoctorProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWelcomeMessage();
    }

    private void updateWelcomeMessage() {
        SharedPreferences sharedPreferences = getSharedPreferences("DoctorProfile", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        if (!name.isEmpty()) {
            tvWelcomeName.setText("Welcome, Dr. " + name);
        }
    }
}
