package com.example.app.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorProfileActivity extends AppCompatActivity {

    private TextView tvName, tvSpecialty, tvExperience, tvClinicName, tvAddress, tvPhone, tvEmail;
    private ImageView ivProfilePic;

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
        tvEmail = findViewById(R.id.tv_doctor_email);
        ivProfilePic = findViewById(R.id.iv_profile_pic);

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
        loadLocalProfileData();
        fetchProfileFromServer();
    }

    private void loadLocalProfileData() {
        SharedPreferences sharedPreferences = getSharedPreferences("DoctorProfile", MODE_PRIVATE);
        String currentUserEmail = sharedPreferences.getString("email", "");
        String imageKey = "profile_image_" + currentUserEmail;

        String name = sharedPreferences.getString("name", "Dr. Ethan Carter");
        String specialty = sharedPreferences.getString("specialty", "Reproductive Endocrinologist");
        String experience = sharedPreferences.getString("experience", "10+ years");
        String phone = sharedPreferences.getString("phone", "+1 (555) 123-4567");
        String address = sharedPreferences.getString("address", "123 Main Street, Anytown, USA");
        String clinicName = sharedPreferences.getString("clinicName", "Advanced Fertility Center");
        String email = sharedPreferences.getString("email", "doctor@example.com");
        String base64Image = sharedPreferences.getString(imageKey, "");

        updateUI(name, specialty, experience, phone, address, clinicName, email, base64Image);
    }

    private void fetchProfileFromServer() {
        ApiService apiService = RetrofitClient.getApiService();
        retrofit2.Call<ProfileResponse> call = apiService.getProfile();
        call.enqueue(new retrofit2.Callback<ProfileResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ProfileResponse> call, retrofit2.Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileResponse profile = response.body();
                    
                    SharedPreferences sharedPreferences = getSharedPreferences("DoctorProfile", MODE_PRIVATE);
                    String currentUserEmail = sharedPreferences.getString("email", "");
                    String imageKey = "profile_image_" + currentUserEmail;

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", profile.fullName);
                    editor.putString("specialty", profile.specialization);
                    editor.putString("experience", profile.experienceYears);
                    editor.putString("phone", profile.phoneNumber);
                    editor.putString("address", profile.address);
                    editor.putString("clinicName", profile.clinicName);
                    editor.putString("email", profile.email);
                    editor.apply();

                    updateUI(profile.fullName, profile.specialization, profile.experienceYears, profile.phoneNumber, profile.address, profile.clinicName, profile.email, sharedPreferences.getString(imageKey, ""));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ProfileResponse> call, Throwable t) {
            }
        });
    }

    private void updateUI(String name, String specialty, String experience, String phone, String address, String clinicName, String email, String base64Image) {
        tvName.setText(name);
        tvSpecialty.setText(specialty);
        tvExperience.setText(experience.contains("experience") ? experience : experience + " of experience");
        tvPhone.setText(phone);
        tvAddress.setText(address);
        tvClinicName.setText(clinicName);
        tvEmail.setText(email);

        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (decodedByte != null) {
                    ivProfilePic.setImageBitmap(decodedByte);
                } else {
                    ivProfilePic.setImageResource(R.drawable.ic_person); // Fallback image
                }
            } catch (Exception e) {
                ivProfilePic.setImageResource(R.drawable.ic_person); // Fallback image
            }
        } else {
            ivProfilePic.setImageResource(R.drawable.ic_person); // Default image
        }
    }
}
