package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import java.util.ArrayList;
import java.util.List;

public class DoctorQuestion4Activity extends AppCompatActivity {

    private List<LinearLayout> optionLayouts = new ArrayList<>();
    private String selectedOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_question_4);

        optionLayouts.add(findViewById(R.id.layout_none));
        optionLayouts.add(findViewById(R.id.layout_mild));
        optionLayouts.add(findViewById(R.id.layout_significant));

        for (LinearLayout layout : optionLayouts) {
            layout.setOnClickListener(v -> selectOption(layout));
        }

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_back_bottom).setOnClickListener(v -> finish());
        
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (selectedOption.isEmpty()) {
                android.widget.Toast.makeText(this, "Please select an option to continue", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            DoctorAssessmentData.getInstance().setQuestionAnswer("media_color_change", selectedOption);
            Intent intent = new Intent(this, DoctorQuestion5Activity.class);
            startActivity(intent);
        });
    }

    private void selectOption(LinearLayout selectedLayout) {
        for (LinearLayout layout : optionLayouts) {
            layout.setSelected(layout == selectedLayout);
        }
        
        if (selectedLayout.getId() == R.id.layout_none) selectedOption = "None";
        else if (selectedLayout.getId() == R.id.layout_mild) selectedOption = "Mild";
        else if (selectedLayout.getId() == R.id.layout_significant) selectedOption = "Significant";
    }
}
