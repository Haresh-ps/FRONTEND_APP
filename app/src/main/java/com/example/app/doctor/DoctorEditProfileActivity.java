package com.example.app.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DoctorEditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;
    private EditText etFullName, etPhone, etEmail, etAddress, etClinicName;
    private Spinner spinnerQualification, spinnerExperience;
    private TextView tvHeaderName, tvHeaderSpecialty;
    private ImageView ivProfilePic;
    private SharedPreferences sharedPreferences;
    private String encodedImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit_profile);

        sharedPreferences = getSharedPreferences("DoctorProfile", MODE_PRIVATE);

        etFullName = findViewById(R.id.et_full_name);
        spinnerQualification = findViewById(R.id.spinner_qualification);
        spinnerExperience = findViewById(R.id.spinner_experience);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        etClinicName = findViewById(R.id.et_clinic_name);
        tvHeaderName = findViewById(R.id.tv_header_name);
        tvHeaderSpecialty = findViewById(R.id.tv_header_specialty);
        ivProfilePic = findViewById(R.id.iv_profile_pic);

        setupSpinners();
        loadProfileData();
        
        findViewById(R.id.iv_close).setOnClickListener(v -> finish());
        findViewById(R.id.btn_cancel).setOnClickListener(v -> finish());
        
        findViewById(R.id.fab_edit_pic).setOnClickListener(v -> openGallery());
        
        findViewById(R.id.btn_save_changes).setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            if (!fullName.matches("^[a-zA-Z.\\s]+$")) {
                Toast.makeText(this, "Name should only contain letters and spaces", Toast.LENGTH_SHORT).show();
                return;
            }

            String qualification = spinnerQualification.getSelectedItem().toString();
            if (qualification.equals("Select Qualification")) {
                Toast.makeText(this, "Please select qualification", Toast.LENGTH_SHORT).show();
                return;
            }

            String experience = spinnerExperience.getSelectedItem().toString();
            if (experience.equals("Select Experience")) {
                Toast.makeText(this, "Please select years of experience", Toast.LENGTH_SHORT).show();
                return;
            }

            String phone = etPhone.getText().toString();
            if (phone.length() != 10 || !android.text.TextUtils.isDigitsOnly(phone)) {
                Toast.makeText(this, "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
                return;
            } else if (!email.endsWith("@gmail.com")) {
                Toast.makeText(this, "Only @gmail.com is allowed.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                String prefix = email.split("@")[0];
                if (prefix.length() < 3 || Character.isDigit(prefix.charAt(0))) {
                    Toast.makeText(this, "Prefix must be min 3 chars & not start with a number.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            saveProfileData();
            Intent intent = new Intent(DoctorEditProfileActivity.this, DoctorSavedSuccessActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    ivProfilePic.setImageBitmap(bitmap);
                    encodedImage = encodeImage(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void setupSpinners() {
        String[] qualifications = new String[]{
            "Select Qualification", "MBBS", "MD", "MS", "DGO", 
            "MSc Clinical Embryology", "Clinical Embryologist", 
            "IVF Specialist", "Fertility Specialist", 
            "Lab Technician", "Other"
        };
        ArrayAdapter<String> qualAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, qualifications);
        qualAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQualification.setAdapter(qualAdapter);

        String[] experienceYears = new String[]{"Select Experience", "1 year", "2 years", "3 years", "4 years", "5 years", "10+ years", "15+ years", "20+ years"};
        ArrayAdapter<String> expAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, experienceYears);
        expAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExperience.setAdapter(expAdapter);
    }

    private void loadProfileData() {
        String currentUserEmail = sharedPreferences.getString("email", "");
        String imageKey = "profile_image_" + currentUserEmail;

        String name = sharedPreferences.getString("name", "Dr. Amelia Chen");
        String specialty = sharedPreferences.getString("specialty", "Reproductive Endocrinologist");
        String experience = sharedPreferences.getString("experience", "10+ years");
        String phone = sharedPreferences.getString("phone", "+1 (555) 123-4567");
        String email = sharedPreferences.getString("email", "amelia.chen@clinic.com");
        String address = sharedPreferences.getString("address", "123 Main Street, Anytown, USA");
        String clinicName = sharedPreferences.getString("clinicName", "Advanced Fertility Center");
        String base64Image = sharedPreferences.getString(imageKey, "");

        etFullName.setText(name);
        setSpinnerValue(spinnerQualification, specialty);
        setSpinnerValue(spinnerExperience, experience);
        etPhone.setText(phone);
        etEmail.setText(email);
        etAddress.setText(address);
        etClinicName.setText(clinicName);

        tvHeaderName.setText(name);
        tvHeaderSpecialty.setText(specialty);

        if (!base64Image.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (decodedByte != null) {
                    ivProfilePic.setImageBitmap(decodedByte);
                    encodedImage = base64Image;
                } else {
                    ivProfilePic.setImageResource(R.drawable.ic_person);
                }
            } catch (Exception e) {
                ivProfilePic.setImageResource(R.drawable.ic_person);
            }
        } else {
            ivProfilePic.setImageResource(R.drawable.ic_person);
        }
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void saveProfileData() {
        String currentUserEmail = sharedPreferences.getString("email", "");
        String imageKey = "profile_image_" + currentUserEmail;

        String name = etFullName.getText().toString();
        String qualification = spinnerQualification.getSelectedItem().toString();
        String experience = spinnerExperience.getSelectedItem().toString();
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        String address = etAddress.getText().toString();
        String clinicName = etClinicName.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("specialty", qualification);
        editor.putString("experience", experience);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putString("address", address);
        editor.putString("clinicName", clinicName);
        if (!encodedImage.isEmpty()) {
            editor.putString(imageKey, encodedImage);
        }
        editor.apply();

        ApiService apiService = RetrofitClient.getApiService();
        retrofit2.Call<ProfileResponse> call = apiService.updateProfile(new ProfileRequest(clinicName, qualification, phone, name, address, experience, email));
        call.enqueue(new retrofit2.Callback<ProfileResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ProfileResponse> call, retrofit2.Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DoctorEditProfileActivity.this, "Profile updated on server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ProfileResponse> call, Throwable t) {
            }
        });
    }
}
