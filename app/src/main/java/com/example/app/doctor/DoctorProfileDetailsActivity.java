package com.example.app.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorProfileDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_details);

        ImageButton btnBack = findViewById(R.id.btn_back_profile_details);
        btnBack.setOnClickListener(v -> finish());

        EditText etName = findViewById(R.id.et_doctor_name);
        EditText etClinic = findViewById(R.id.et_doctor_clinic);
        EditText etPhone = findViewById(R.id.et_doctor_phone);
        EditText etAddress = findViewById(R.id.et_doctor_address);
        Spinner spinnerExperience = findViewById(R.id.spinner_experience);
        Spinner spinnerQualification = findViewById(R.id.spinner_qualification);

        // Setup Spinner for Qualification
        String[] qualifications = new String[]{
            "Select Qualification", "MBBS", "MD", "MS", "DGO", 
            "MSc Clinical Embryology", "Clinical Embryologist", 
            "IVF Specialist", "Fertility Specialist", 
            "Lab Technician", "Other"
        };
        ArrayAdapter<String> qualAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, qualifications);
        qualAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQualification.setAdapter(qualAdapter);

        // Setup Spinner for Years of Experience
        String[] experienceYears = new String[]{"Select Experience", "1 year", "2 years", "3 years", "4 years", "5 years", "10+ years", "15+ years", "20+ years"};
        ArrayAdapter<String> expAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, experienceYears);
        expAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExperience.setAdapter(expAdapter);

        Button btnSaveContinue = findViewById(R.id.btn_save_continue);
        btnSaveContinue.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            // Character-only validation (allowing dots for Dr.)
            if (!name.matches("^[a-zA-Z.\\s]+$")) {
                Toast.makeText(this, "Doctor name should only contain letters and spaces", Toast.LENGTH_SHORT).show();
                return;
            }

            String clinic = etClinic.getText().toString();
            String qualification = spinnerQualification.getSelectedItem().toString();
            String phone = etPhone.getText().toString();
            String address = etAddress.getText().toString();
            String experience = spinnerExperience.getSelectedItem().toString();
            
            if (qualification.equals("Select Qualification")) {
                Toast.makeText(this, "Please select qualification", Toast.LENGTH_SHORT).show();
                return;
            }

            if (experience.equals("Select Experience")) {
                Toast.makeText(this, "Please select years of experience", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please enter phone and address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.length() != 10 || !android.text.TextUtils.isDigitsOnly(phone)) {
                Toast.makeText(this, "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save locally to SharedPreferences so it's visible in Profile Page
            SharedPreferences sharedPreferences = getSharedPreferences("DoctorProfile", MODE_PRIVATE);
            String email = sharedPreferences.getString("email", ""); // Retrieve email
            
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.putString("clinicName", clinic);
            editor.putString("specialty", qualification);
            editor.putString("experience", experience);
            editor.putString("phone", phone);
            editor.putString("address", address);
            editor.apply();

            ApiService apiService = RetrofitClient.getApiService();
            // Sending actual data to the backend including the email
            Call<ProfileResponse> call = apiService.updateProfile(new ProfileRequest(clinic, qualification, phone, name, address, experience, email));

            call.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(DoctorProfileDetailsActivity.this, "Profile Saved!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoctorProfileDetailsActivity.this, DoctorProfileSuccessActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(DoctorProfileDetailsActivity.this, "Save Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    Toast.makeText(DoctorProfileDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
