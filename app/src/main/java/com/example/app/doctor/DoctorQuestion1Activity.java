package com.example.app.doctor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorQuestion1Activity extends AppCompatActivity {

    private Button btnD3, btnD5, btnD6;
    private String selectedOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_question_1);

        btnD3 = findViewById(R.id.btn_d3);
        btnD5 = findViewById(R.id.btn_d5);
        btnD6 = findViewById(R.id.btn_d6);

        btnD3.setOnClickListener(v -> selectOption("D3"));
        btnD5.setOnClickListener(v -> selectOption("D5"));
        btnD6.setOnClickListener(v -> selectOption("D6"));

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_back_bottom).setOnClickListener(v -> finish());
        
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (!selectedOption.isEmpty()) {
                DoctorAssessmentData.getInstance().setEmbryoDay(selectedOption);
            }
            Intent intent = new Intent(this, DoctorQuestion2Activity.class);
            startActivity(intent);
        });
    }

    private void selectOption(String option) {
        selectedOption = option;
        
        // Reset all buttons to default state
        resetButton(btnD3);
        resetButton(btnD5);
        resetButton(btnD6);

        // Highlight selected button
        if (option.equals("D3")) highlightButton(btnD3);
        else if (option.equals("D5")) highlightButton(btnD5);
        else if (option.equals("D6")) highlightButton(btnD6);
    }

    private void resetButton(Button button) {
        button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.WHITE));
        button.setTextColor(Color.BLACK);
    }

    private void highlightButton(Button button) {
        button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#BFDBFE")));
        button.setTextColor(Color.BLACK);
    }
}
