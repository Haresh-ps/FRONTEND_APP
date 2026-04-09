package com.example.app.doctor;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import java.util.Calendar;
import java.util.Locale;

public class PatientLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        EditText etPatientId = findViewById(R.id.et_patient_id);
        EditText etDob = findViewById(R.id.et_dob);
        Button btnLogin = findViewById(R.id.btn_patient_login);
        android.widget.TextView tvIdError = findViewById(R.id.tv_patient_id_error);
        android.widget.TextView tvDobError = findViewById(R.id.tv_dob_error);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        etDob.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year1);
                        etDob.setText(formattedDate);
                        tvDobError.setVisibility(android.view.View.GONE);
                    }, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        btnLogin.setOnClickListener(v -> {
            String inputId = etPatientId.getText().toString().trim();
            String inputDob = etDob.getText().toString().trim();

            tvIdError.setVisibility(android.view.View.GONE);
            tvDobError.setVisibility(android.view.View.GONE);

            boolean hasError = false;
            if (inputId.isEmpty()) {
                tvIdError.setText("Patient ID is required");
                tvIdError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            }

            if (inputDob.isEmpty()) {
                tvDobError.setText("Select your Date of Birth");
                tvDobError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            }

            if (hasError) return;

            Intent intent = new Intent(PatientLoginActivity.this, PatientDashboardActivity.class);
            intent.putExtra("patient_id", inputId);
            intent.putExtra("patient_dob", inputDob);
            startActivity(intent);
            finish();
        });
    }
}
