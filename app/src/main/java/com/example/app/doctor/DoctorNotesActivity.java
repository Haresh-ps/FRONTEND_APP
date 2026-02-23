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

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        
        findViewById(R.id.btn_next_notes).setOnClickListener(v -> {
            String notes = etNotes.getText().toString();
            if (!notes.isEmpty()) {
                DoctorAssessmentData.getInstance().setQuestionAnswer("notes", notes);
            }
            Intent intent = new Intent(this, DoctorReviewActivity.class);
            startActivity(intent);
        });
    }
}
