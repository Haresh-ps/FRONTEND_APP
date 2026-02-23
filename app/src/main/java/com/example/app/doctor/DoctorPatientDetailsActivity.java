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

public class DoctorPatientDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patient_details);

        EditText etId = findViewById(R.id.et_patient_id);
        EditText etName = findViewById(R.id.et_patient_name);
        EditText etDob = findViewById(R.id.et_patient_dob);
        EditText etAge = findViewById(R.id.et_patient_age);
        Button btnStartArgs = findViewById(R.id.btn_start_assessment);

        findViewById(R.id.btn_back_patient_details).setOnClickListener(v -> finish());

        etDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", month1 + 1, dayOfMonth, year1);
                etDob.setText(selectedDate);
                
                // Optional: Auto-calculate age
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int age = currentYear - year1;
                etAge.setText(String.valueOf(age));
                
            }, year, month, day);
            datePickerDialog.show();
        });

        btnStartArgs.setOnClickListener(v -> {
            String id = etId.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String dob = etDob.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();

            if (id.isEmpty() || name.isEmpty() || ageStr.isEmpty() || dob.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid Age", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save to DoctorAssessmentData
            DoctorAssessmentData.getInstance().clear(); // Start fresh
            DoctorAssessmentData.getInstance().setPatientId(id);
            DoctorAssessmentData.getInstance().setPatientName(name);
            DoctorAssessmentData.getInstance().setPatientDob(dob);
            DoctorAssessmentData.getInstance().setPatientAge(age);

            // Navigate to Question 1
            Intent intent = new Intent(DoctorPatientDetailsActivity.this, DoctorQuestion1Activity.class);
            startActivity(intent);
        });
    }
}
