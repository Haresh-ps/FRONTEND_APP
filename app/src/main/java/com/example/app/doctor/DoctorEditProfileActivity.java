package com.example.app.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorEditProfileActivity extends AppCompatActivity {

    private EditText etFullName, etSpecialty, etExperience, etPhone, etEmail, etAddress, etClinicName;
    private TextView tvHeaderName, tvHeaderSpecialty;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit_profile);

        sharedPreferences = getSharedPreferences("DoctorProfile", MODE_PRIVATE);

        etFullName = findViewById(R.id.et_full_name);
        etSpecialty = findViewById(R.id.et_specialty);
        etExperience = findViewById(R.id.et_experience);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        etClinicName = findViewById(R.id.et_clinic_name);
        tvHeaderName = findViewById(R.id.tv_header_name);
        tvHeaderSpecialty = findViewById(R.id.tv_header_specialty);

        loadProfileData();

        findViewById(R.id.iv_close).setOnClickListener(v -> finish());
        findViewById(R.id.btn_cancel).setOnClickListener(v -> finish());
        
        findViewById(R.id.btn_save_changes).setOnClickListener(v -> {
            saveProfileData();
            Intent intent = new Intent(DoctorEditProfileActivity.this, DoctorSavedSuccessActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadProfileData() {
        String name = sharedPreferences.getString("name", "Dr. Amelia Chen");
        String specialty = sharedPreferences.getString("specialty", "Reproductive Endocrinologist");
        String experience = sharedPreferences.getString("experience", "10+ years");
        String phone = sharedPreferences.getString("phone", "+1 (555) 123-4567");
        String email = sharedPreferences.getString("email", "amelia.chen@clinic.com");
        String address = sharedPreferences.getString("address", "123 Main Street, Anytown, USA");
        String clinicName = sharedPreferences.getString("clinicName", "Advanced Fertility Center");

        etFullName.setText(name);
        etSpecialty.setText(specialty);
        etExperience.setText(experience);
        etPhone.setText(phone);
        etEmail.setText(email);
        etAddress.setText(address);
        etClinicName.setText(clinicName);

        tvHeaderName.setText(name);
        tvHeaderSpecialty.setText(specialty);
    }

    private void saveProfileData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", etFullName.getText().toString());
        editor.putString("specialty", etSpecialty.getText().toString());
        editor.putString("experience", etExperience.getText().toString());
        editor.putString("phone", etPhone.getText().toString());
        editor.putString("email", etEmail.getText().toString());
        editor.putString("address", etAddress.getText().toString());
        editor.putString("clinicName", etClinicName.getText().toString());
        editor.apply();
    }
}
