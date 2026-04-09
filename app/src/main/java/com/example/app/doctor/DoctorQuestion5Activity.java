package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.app.R;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;

public class DoctorQuestion5Activity extends AppCompatActivity {

    private final List<MaterialCardView> optionCards = new ArrayList<>();
    private String selectedOption = "";
    private TextView tvNotesDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_question_5);

        tvNotesDisplay = findViewById(R.id.tv_notes_display);

        optionCards.add(findViewById(R.id.card_normal));
        optionCards.add(findViewById(R.id.card_slight));
        optionCards.add(findViewById(R.id.card_high));

        findViewById(R.id.card_normal).setOnClickListener(v -> selectOption(findViewById(R.id.card_normal), "Normal"));
        findViewById(R.id.card_slight).setOnClickListener(v -> selectOption(findViewById(R.id.card_slight), "Slight"));
        findViewById(R.id.card_high).setOnClickListener(v -> selectOption(findViewById(R.id.card_high), "High"));

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_back_bottom).setOnClickListener(v -> finish());
        
        // Navigation to Notes Activity
        findViewById(R.id.card_notes).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorNotesActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (selectedOption.isEmpty()) {
                Toast.makeText(this, "Please select an option to continue", Toast.LENGTH_SHORT).show();
                return;
            }
            DoctorAssessmentData.getInstance().setQuestionAnswer("ph_deviation", selectedOption);
            Intent intent = new Intent(this, DoctorQuestion6Activity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Display notes if they have been entered
        String notes = DoctorAssessmentData.getInstance().getQuestionAnswer("notes");
        if (notes != null && !notes.isEmpty()) {
            tvNotesDisplay.setText(notes);
            tvNotesDisplay.setTextColor(ContextCompat.getColor(this, R.color.black));
        } else {
            tvNotesDisplay.setText("Tap to add or edit notes...");
            tvNotesDisplay.setTextColor(android.graphics.Color.parseColor("#64748B"));
        }
    }

    private void selectOption(MaterialCardView selectedItem, String option) {
        selectedOption = option;
        for (MaterialCardView card : optionCards) {
            boolean isSelected = (card == selectedItem);
            card.setSelected(isSelected);
            
            // Visual feedback for selection
            if (isSelected) {
                card.setStrokeColor(android.graphics.Color.parseColor("#4F46E5"));
                card.setStrokeWidth(4);
            } else {
                card.setStrokeColor(android.graphics.Color.TRANSPARENT);
                card.setStrokeWidth(0);
            }
        }
    }
}
