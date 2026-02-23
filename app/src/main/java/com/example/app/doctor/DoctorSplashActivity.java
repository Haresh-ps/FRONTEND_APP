package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_splash);

        View blueShape = findViewById(R.id.blue_shape);
        View pinkShape = findViewById(R.id.pink_shape);
        View logoContainer = findViewById(R.id.logo_container);
        View progressBar = findViewById(R.id.splash_progress);

        // Initial positions for background shapes
        blueShape.setTranslationY(-500f);
        blueShape.setTranslationX(-200f);
        pinkShape.setTranslationY(500f);
        pinkShape.setTranslationX(200f);

        // Animate background shapes sliding in
        blueShape.animate()
                .translationY(0)
                .translationX(0)
                .setDuration(1200)
                .setStartDelay(200)
                .start();

        pinkShape.animate()
                .translationY(0)
                .translationX(0)
                .setDuration(1200)
                .setStartDelay(200)
                .start();

        // Animate Logo Container (Fade in + Scale up)
        logoContainer.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1000)
                .setStartDelay(800)
                .start();

        // Animate Progress Bar
        progressBar.animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(1500)
                .start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(DoctorSplashActivity.this, DoctorGetStartedActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 4000); // Increased to 4 seconds to show off animations
    }
}
