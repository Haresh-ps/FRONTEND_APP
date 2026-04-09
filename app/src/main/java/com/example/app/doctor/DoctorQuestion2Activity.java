package com.example.app.doctor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorQuestion2Activity extends AppCompatActivity {

    private Button btnHours, btnDays;
    private EditText etDuration;
    private String selectedUnit = "Hours";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_question_2);

        btnHours = findViewById(R.id.btn_hours);
        btnDays = findViewById(R.id.btn_days);
        etDuration = findViewById(R.id.et_duration);

        btnHours.setOnClickListener(v -> selectUnit("Hours"));
        btnDays.setOnClickListener(v -> selectUnit("Days"));

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_back_bottom).setOnClickListener(v -> finish());
        
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            String value = etDuration.getText().toString().trim();
            if (value.isEmpty()) {
                android.widget.Toast.makeText(this, "Please enter the duration", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            DoctorAssessmentData.getInstance().setCultureDuration(value + " " + selectedUnit.toLowerCase());

            Intent intent = new Intent(this, DoctorQuestion3Activity.class);
            startActivity(intent);
        });
        
        // Initial state
        selectUnit("Hours");
    }

    private void selectUnit(String unit) {
        selectedUnit = unit;
        if (unit.equals("Hours")) {
            btnHours.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#BFDBFE")));
            btnDays.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.WHITE));
        } else {
            btnHours.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.WHITE));
            btnDays.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#BFDBFE")));
        }
    }
}
