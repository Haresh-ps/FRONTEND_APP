package com.example.app.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorProfileActivity extends AppCompatActivity {

    private TextView tvName, tvSpecialty, tvExperience, tvClinicName, tvAddress, tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        tvName = findViewById(R.id.tv_doctor_name);
        tvSpecialty = findViewById(R.id.tv_doctor_specialty);
        tvExperience = findViewById(R.id.tv_doctor_experience);
        tvClinicName = findViewById(R.id.tv_clinic_name);
        tvAddress = findViewById(R.id.tv_doctor_address);
        tvPhone = findViewById(R.id.tv_doctor_phone);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_edit_profile).setOnClickListener(v -> {
            Intent intent = new Intent(DoctorProfileActivity.this, DoctorEditProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.layout_app_info).setOnClickListener(v -> {
            Intent intent = new Intent(DoctorProfileActivity.this, DoctorAppInfoActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.layout_help).setOnClickListener(v -> {
            Intent intent = new Intent(DoctorProfileActivity.this, DoctorHelpActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.layout_logout).setOnClickListener(v -> {
            Intent intent = new Intent(DoctorProfileActivity.this, DoctorLogoutActivity.class);
            startActivity(intent);
        });

        ImageButton btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorProfileActivity.this, DoctorMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        ImageButton btnAddAssessment = findViewById(R.id.btn_add_assessment);
        btnAddAssessment.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorProfileActivity.this, DoctorNewAssessmentActivity.class);
            startActivity(intent);
        });

        ImageButton btnReports = findViewById(R.id.btn_reports);
        btnReports.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorProfileActivity.this, DoctorReportsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileData();
    }

    private void loadProfileData() {
        SharedPreferences sharedPreferences = getSharedPreferences("DoctorProfile", MODE_PRIVATE);
        
        String name = sharedPreferences.getString("name", "Dr. Ethan Carter");
        String specialty = sharedPreferences.getString("specialty", "Reproductive Endocrinologist");
        String experience = sharedPreferences.getString("experience", "10+ years of experience");
        String phone = sharedPreferences.getString("phone", "+1 (555) 123-4567");
        String address = sharedPreferences.getString("address", "123 Main Street, Anytown, USA");
        String clinicName = sharedPreferences.getString("clinicName", "Advanced Fertility Center");

        tvName.setText(name);
        tvSpecialty.setText(specialty);
        tvExperience.setText(experience.contains("experience") ? experience : experience + " of experience");
        tvPhone.setText(phone);
        tvAddress.setText(address);
        tvClinicName.setText(clinicName);
    }
}
