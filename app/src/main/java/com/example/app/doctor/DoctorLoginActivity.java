package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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

        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                android.widget.Toast.makeText(this, "Please enter email and password", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            // Call the API
            ApiService apiService = RetrofitClient.getApiService();
            retrofit2.Call<LoginResponse> call = apiService.login(new LoginRequest(email, password));

            call.enqueue(new retrofit2.Callback<LoginResponse>() {
                @Override
                public void onResponse(retrofit2.Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        DoctorLoginActivity.this.runOnUiThread(() -> 
                            android.widget.Toast.makeText(DoctorLoginActivity.this, "Login Successful!", android.widget.Toast.LENGTH_SHORT).show()
                        );
                        
                        // Store the token!
                        RetrofitClient.setAuthToken(response.body().access);

                        // Direct to Main Activity for existing users
                        Intent intent = new Intent(DoctorLoginActivity.this, DoctorMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        android.widget.Toast.makeText(DoctorLoginActivity.this, "Login Failed: " + response.message(), android.widget.Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<LoginResponse> call, Throwable t) {
                    android.widget.Toast.makeText(DoctorLoginActivity.this, "Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        });

        Button btnSignUp = findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorLoginActivity.this, DoctorSignUpActivity.class);
            startActivity(intent);
        });

        TextView tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorLoginActivity.this, DoctorForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
}
