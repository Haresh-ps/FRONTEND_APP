package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_notes);

        android.widget.EditText etNotes = findViewById(R.id.et_notes);
        
        // Pre-fill previously entered notes if any
        String existingNotes = DoctorAssessmentData.getInstance().getQuestionAnswer("notes");
        if (!existingNotes.isEmpty()) {
            etNotes.setText(existingNotes);
        }

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        
        findViewById(R.id.btn_next_notes).setOnClickListener(v -> {
            String notes = etNotes.getText().toString();
            DoctorAssessmentData.getInstance().setQuestionAnswer("notes", notes);
            Intent intent = new Intent(this, DoctorReviewActivity.class);
            startActivity(intent);
        });
    }
}
