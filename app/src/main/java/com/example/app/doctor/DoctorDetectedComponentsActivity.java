package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorDetectedComponentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detected_components);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_next).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorMetabolicActivityLevelActivity.class);
            intent.putExtra("ACTIVITY_LEVEL", "Moderate"); // This can be dynamic
            startActivity(intent);
        });
    }
}
