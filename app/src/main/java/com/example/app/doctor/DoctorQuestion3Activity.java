package com.example.app.doctor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import java.util.ArrayList;
import java.util.List;

public class DoctorQuestion3Activity extends AppCompatActivity {

    private List<Button> optionButtons = new ArrayList<>();
    private String selectedOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_question_3);

        optionButtons.add(findViewById(R.id.btn_option_1));
        optionButtons.add(findViewById(R.id.btn_option_2));
        optionButtons.add(findViewById(R.id.btn_option_3));
        optionButtons.add(findViewById(R.id.btn_option_4));
        optionButtons.add(findViewById(R.id.btn_option_5));

        for (Button btn : optionButtons) {
            btn.setOnClickListener(v -> selectOption(btn));
        }

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_back_bottom).setOnClickListener(v -> finish());
        
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (selectedOption.isEmpty()) {
                android.widget.Toast.makeText(this, "Please select an option to continue", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            DoctorAssessmentData.getInstance().setQuestionAnswer("culture_medium", selectedOption);
            Intent intent = new Intent(this, DoctorQuestion4Activity.class);
            startActivity(intent);
        });
    }

    private void selectOption(Button selectedBtn) {
        selectedOption = selectedBtn.getText().toString();
        
        for (Button btn : optionButtons) {
            if (btn == selectedBtn) {
                btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#BFDBFE")));
            } else {
                btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#F1F5F9")));
            }
        }
    }
}
