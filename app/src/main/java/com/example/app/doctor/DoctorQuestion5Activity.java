package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.app.R;
import java.util.ArrayList;
import java.util.List;

public class DoctorQuestion5Activity extends AppCompatActivity {

    private List<CardView> optionCards = new ArrayList<>();
    private String selectedOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_question_5);

        optionCards.add(findViewById(R.id.card_normal));
        optionCards.add(findViewById(R.id.card_slight));
        optionCards.add(findViewById(R.id.card_high));

        findViewById(R.id.btn_select_normal).setOnClickListener(v -> selectOption(findViewById(R.id.card_normal), "Normal"));
        findViewById(R.id.btn_select_slight).setOnClickListener(v -> selectOption(findViewById(R.id.card_slight), "Slight"));
        findViewById(R.id.btn_select_high).setOnClickListener(v -> selectOption(findViewById(R.id.card_high), "High"));

        // Also allow clicking the card itself
        findViewById(R.id.card_normal).setOnClickListener(v -> selectOption(findViewById(R.id.card_normal), "Normal"));
        findViewById(R.id.card_slight).setOnClickListener(v -> selectOption(findViewById(R.id.card_slight), "Slight"));
        findViewById(R.id.card_high).setOnClickListener(v -> selectOption(findViewById(R.id.card_high), "High"));

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_back_bottom).setOnClickListener(v -> finish());
        
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (!selectedOption.isEmpty()) {
                DoctorAssessmentData.getInstance().setQuestionAnswer("ph_deviation", selectedOption);
            }
            Intent intent = new Intent(this, DoctorQuestion6Activity.class);
            startActivity(intent);
        });
    }

    private void selectOption(CardView selectedItem, String option) {
        selectedOption = option;
        for (CardView card : optionCards) {
            card.setSelected(card == selectedItem);
        }
    }
}
