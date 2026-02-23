package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup);

        ImageButton btnBack = findViewById(R.id.btn_back_signup);
        btnBack.setOnClickListener(v -> finish());

        android.widget.EditText etEmail = findViewById(R.id.et_email_signup);
        android.widget.EditText etPassword = findViewById(R.id.et_password_signup);
        Button btnSignUpSubmit = findViewById(R.id.btn_sign_up_submit);

        btnSignUpSubmit.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                android.widget.Toast.makeText(this, "Please enter email and password", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getApiService();
            // Using email as username for simplicity
            retrofit2.Call<Void> call = apiService.signup(new SignupRequest(email, email, password));

            call.enqueue(new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                    if (response.isSuccessful()) {
                        android.widget.Toast.makeText(DoctorSignUpActivity.this, "Signup Successful! Logging in...", android.widget.Toast.LENGTH_SHORT).show();
                        
                        // Auto-Login
                        retrofit2.Call<LoginResponse> loginCall = apiService.login(new LoginRequest(email, password));
                        loginCall.enqueue(new retrofit2.Callback<LoginResponse>() {
                            @Override
                            public void onResponse(retrofit2.Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                                if (response.isSuccessful()) {
                        // Store the token!
                        RetrofitClient.setAuthToken(response.body().access);

                        // Navigate to Profile Setup or Details
                                    Intent intent = new Intent(DoctorSignUpActivity.this, DoctorProfileDetailsActivity.class);
                                    // Clear back stack so they can't go back to signup/login
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                     android.widget.Toast.makeText(DoctorSignUpActivity.this, "Auto-Login Failed. Please login manually.", android.widget.Toast.LENGTH_LONG).show();
                                     finish(); // Go back to login
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<LoginResponse> call, Throwable t) {
                                android.widget.Toast.makeText(DoctorSignUpActivity.this, "Auto-Login Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    } else {
                        android.widget.Toast.makeText(DoctorSignUpActivity.this, "Signup Failed: " + response.message(), android.widget.Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                    android.widget.Toast.makeText(DoctorSignUpActivity.this, "Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
