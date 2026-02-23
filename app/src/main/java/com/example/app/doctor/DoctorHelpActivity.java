package com.example.app.doctor;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_help);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        setupAccordion(R.id.header_how_to_use, R.id.tv_content_how_to_use, R.id.iv_arrow_how_to_use);
        setupAccordion(R.id.header_clinical_scope, R.id.tv_content_clinical_scope, R.id.iv_arrow_clinical_scope);
        setupAccordion(R.id.header_best_practices, R.id.tv_content_best_practices, R.id.iv_arrow_best_practices);
    }

    private void setupAccordion(int headerId, int contentId, int arrowId) {
        View header = findViewById(headerId);
        TextView content = findViewById(contentId);
        ImageView arrow = findViewById(arrowId);

        header.setOnClickListener(v -> {
            if (content.getVisibility() == View.GONE) {
                content.setVisibility(View.VISIBLE);
                arrow.setRotation(270); // Rotate arrow up
            } else {
                content.setVisibility(View.GONE);
                arrow.setRotation(90); // Rotate arrow down
            }
        });
    }
}
