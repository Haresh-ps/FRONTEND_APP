package com.example.app.doctor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import java.util.ArrayList;
import java.util.List;

public class DoctorQuestion6Activity extends AppCompatActivity {

    private List<Button> optionButtons = new ArrayList<>();
    private String selectedOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_question_6);

        optionButtons.add(findViewById(R.id.btn_clear));
        optionButtons.add(findViewById(R.id.btn_slightly_turbid));
        optionButtons.add(findViewById(R.id.btn_turbid));

        for (Button btn : optionButtons) {
            btn.setOnClickListener(v -> selectOption(btn));
        }

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_back_bottom).setOnClickListener(v -> finish());
        
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (!selectedOption.isEmpty()) {
                DoctorAssessmentData.getInstance().setQuestionAnswer("visual_clarity", selectedOption);
            }
            Intent intent = new Intent(this, DoctorNotesActivity.class);
            startActivity(intent);
        });
    }

    private void selectOption(Button selectedBtn) {
        selectedOption = selectedBtn.getText().toString();
        
        for (Button btn : optionButtons) {
            if (btn == selectedBtn) {
                btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#BFDBFE")));
            } else {
                btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.WHITE));
            }
        }
    }
}
