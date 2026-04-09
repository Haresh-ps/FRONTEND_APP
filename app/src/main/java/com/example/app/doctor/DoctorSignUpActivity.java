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
        android.widget.EditText etConfirmPassword = findViewById(R.id.et_confirm_password_signup);
        Button btnSignUpSubmit = findViewById(R.id.btn_sign_up_submit);
        android.widget.TextView tvEmailError = findViewById(R.id.tv_email_error_signup);
        android.widget.TextView tvPasswordError = findViewById(R.id.tv_password_error_signup);
        android.widget.TextView tvConfirmPasswordError = findViewById(R.id.tv_confirm_password_error_signup);

        btnSignUpSubmit.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Reset errors
            tvEmailError.setVisibility(android.view.View.GONE);
            tvPasswordError.setVisibility(android.view.View.GONE);
            tvConfirmPasswordError.setVisibility(android.view.View.GONE);

            boolean hasError = false;

            if (email.isEmpty()) {
                tvEmailError.setText("Please enter your email");
                tvEmailError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            } else if (!email.endsWith("@gmail.com")) {
                tvEmailError.setText("Only @gmail.com is allowed.");
                tvEmailError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            } else {
                // Prefix validation
                String prefix = email.split("@")[0];
                if (prefix.length() < 3 || Character.isDigit(prefix.charAt(0))) {
                   tvEmailError.setText("Start prefix with letters & min 3 chars.");
                   tvEmailError.setVisibility(android.view.View.VISIBLE);
                   hasError = true;
                }
            }

            if (password.isEmpty()) {
                tvPasswordError.setText("Please enter a password");
                tvPasswordError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            } else if (!validatePasswordComplexity(password)) {
                tvPasswordError.setText("Requirement: 1 Uppercase, 1 Special character, 1 Number & 8 characters atleast");
                tvPasswordError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            }

            if (confirmPassword.isEmpty()) {
                tvConfirmPasswordError.setText("Please confirm your password");
                tvConfirmPasswordError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            } else if (!password.equals(confirmPassword)) {
                tvConfirmPasswordError.setText("Passwords do not match");
                tvConfirmPasswordError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            }

            if (hasError) return;

            ApiService apiService = RetrofitClient.getApiService();
            retrofit2.Call<Void> call = apiService.signup(new SignupRequest(email, email, password));

            call.enqueue(new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Auto-Login logic remains same but uses inline feedback for errors
                        retrofit2.Call<LoginResponse> loginCall = apiService.login(new LoginRequest(email, password));
                        loginCall.enqueue(new retrofit2.Callback<LoginResponse>() {
                            @Override
                            public void onResponse(retrofit2.Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                                if (response.isSuccessful()) {
                                    android.content.SharedPreferences sharedPref = getSharedPreferences("DoctorProfile", android.content.Context.MODE_PRIVATE);
                                    android.content.SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("email", email);
                                    editor.putString("auth_token", response.body().access);
                                    editor.apply();
                                    RetrofitClient.setAuthToken(response.body().access);
                                    Intent intent = new Intent(DoctorSignUpActivity.this, DoctorProfileDetailsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                     tvPasswordError.setText("Auto-login failed. Use login screen.");
                                     tvPasswordError.setVisibility(android.view.View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<LoginResponse> call, Throwable t) {
                                tvPasswordError.setText("Login Error: " + t.getMessage());
                                tvPasswordError.setVisibility(android.view.View.VISIBLE);
                            }
                        });

                    } else {
                        String errorMsg = "Signup Failed";
                        try {
                            String errorBody = response.errorBody().string();
                            org.json.JSONObject errorJson = new org.json.JSONObject(errorBody);
                            if (errorJson.has("error")) {
                                errorMsg = errorJson.getString("error");
                            }
                        } catch (Exception e) {}
                        tvEmailError.setText(errorMsg);
                        tvEmailError.setVisibility(android.view.View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                    tvEmailError.setText("Network error: " + t.getMessage());
                    tvEmailError.setVisibility(android.view.View.VISIBLE);
                }
            });
        });
    }

    private boolean validatePasswordComplexity(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[0-9].*") &&
               password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }
}
