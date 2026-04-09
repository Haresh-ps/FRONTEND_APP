package com.example.app.doctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        Button btnSignIn = findViewById(R.id.btn_sign_in);
        android.widget.EditText etEmail = findViewById(R.id.et_email);
        android.widget.EditText etPassword = findViewById(R.id.et_password);
        Button btnPatientLogin = findViewById(R.id.btn_patient_login);
        android.widget.TextView tvEmailError = findViewById(R.id.tv_email_error);
        android.widget.TextView tvPasswordError = findViewById(R.id.tv_password_error);

        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Reset errors
            tvEmailError.setVisibility(android.view.View.GONE);
            tvPasswordError.setVisibility(android.view.View.GONE);

            boolean hasError = false;
            if (email.isEmpty()) {
                tvEmailError.setText("Please enter your email");
                tvEmailError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            } else if (!email.endsWith("@gmail.com")) {
                tvEmailError.setText("Only @gmail.com is allowed.");
                tvEmailError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            }

            if (password.isEmpty()) {
                tvPasswordError.setText("Please enter your password");
                tvPasswordError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            } else if (password.length() < 8) {
                // Expanded professional text
                tvPasswordError.setText("Requirement: 1 Uppercase, 1 Special character, 1 Number & 8 characters atleast");
                tvPasswordError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            }

            if (hasError) return;

            ApiService apiService = RetrofitClient.getApiService();
            retrofit2.Call<LoginResponse> call = apiService.login(new LoginRequest(email, password));

            call.enqueue(new retrofit2.Callback<LoginResponse>() {
                @Override
                public void onResponse(retrofit2.Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Save token and email to SharedPreferences
                        SharedPreferences sharedPref = getSharedPreferences("DoctorProfile", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("email", email);
                        editor.putString("auth_token", response.body().access);
                        editor.apply();

                        RetrofitClient.setAuthToken(response.body().access);

                        Intent intent = new Intent(DoctorLoginActivity.this, DoctorMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        tvPasswordError.setText("Invalid email or password");
                        tvPasswordError.setVisibility(android.view.View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<LoginResponse> call, Throwable t) {
                    tvPasswordError.setText("Network error: " + t.getMessage());
                    tvPasswordError.setVisibility(android.view.View.VISIBLE);
                }
            });
        });

        btnPatientLogin.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorLoginActivity.this, PatientLoginActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_sign_up).setOnClickListener(v -> startActivity(new Intent(this, DoctorSignUpActivity.class)));
    }
}
