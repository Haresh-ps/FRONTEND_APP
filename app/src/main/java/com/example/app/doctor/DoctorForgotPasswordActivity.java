package com.example.app.doctor;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_forgot_password);

        findViewById(R.id.layout_back_login).setOnClickListener(v -> finish());
        
        findViewById(R.id.btn_send_link).setOnClickListener(v -> {
            // Logic to send reset link
        });
    }
}
