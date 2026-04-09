package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import com.github.chrisbanes.photoview.PhotoView;


public class DoctorPicPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_pic_preview);
        
        PhotoView ivPreview = findViewById(R.id.iv_preview);
        String imageUriString = getIntent().getStringExtra("image_uri");
        if (imageUriString != null) {
            android.net.Uri imageUri = android.net.Uri.parse(imageUriString);
            ivPreview.setImageURI(imageUri);
        }



        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_replace).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorUploadPicActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.btn_continue).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorReadyAnalyzeActivity.class);
            startActivity(intent);
        });
    }
}
