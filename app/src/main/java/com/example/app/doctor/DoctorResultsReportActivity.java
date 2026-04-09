package com.example.app.doctor;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import com.example.app.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DoctorResultsReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_results_report);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        populateData();

        findViewById(R.id.btn_export).setOnClickListener(v -> {
            exportToPdf();
        });

        findViewById(R.id.btn_share).setOnClickListener(v -> {
            shareReportAsPdf();
        });
    }

    private void shareReportAsPdf() {
        NestedScrollView nestedScrollView = findViewById(R.id.nsv_report);
        if (nestedScrollView == null || nestedScrollView.getChildCount() == 0) {
            Toast.makeText(this, "Could not find report content", Toast.LENGTH_SHORT).show();
            return;
        }

        View view = nestedScrollView.getChildAt(0);
        PdfDocument document = generatePdfDocument(view);
        if (document == null) {
            Toast.makeText(this, "Layout not ready for PDF generation", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File cachePath = new File(getCacheDir(), "reports");
            cachePath.mkdirs();
            File file = new File(cachePath, "Embryo_Analysis_Report.pdf");
            FileOutputStream stream = new FileOutputStream(file);
            document.writeTo(stream);
            stream.close();
            document.close();

            Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);

            if (contentUri != null) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/pdf");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                
                DoctorAssessmentData data = DoctorAssessmentData.getInstance();
                String patientName = data.getPatientName() != null ? data.getPatientName() : "Patient";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Embryo Analysis Report - " + patientName);
                
                startActivity(Intent.createChooser(shareIntent, "Share Report PDF via"));
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error generating PDF for sharing: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private PdfDocument generatePdfDocument(View view) {
        int width = view.getWidth();
        int height = view.getHeight();

        if (width <= 0 || height <= 0) {
            return null;
        }

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        view.draw(canvas);
        document.finishPage(page);
        return document;
    }

    private void exportToPdf() {
        NestedScrollView nestedScrollView = findViewById(R.id.nsv_report);
        if (nestedScrollView != null && nestedScrollView.getChildCount() > 0) {
            saveViewAsPdf(nestedScrollView.getChildAt(0));
        } else {
            Toast.makeText(this, "Could not find report content", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveViewAsPdf(View view) {
        PdfDocument document = generatePdfDocument(view);
        if (document == null) {
            Toast.makeText(this, "Layout not ready for PDF export", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = "Embryo_Report_" + System.currentTimeMillis() + ".pdf";
        OutputStream outputStream = null;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
                if (uri != null) {
                    outputStream = getContentResolver().openOutputStream(uri);
                }
            } else {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                outputStream = new FileOutputStream(file);
            }

            if (outputStream != null) {
                document.writeTo(outputStream);
                Toast.makeText(this, "PDF saved to Downloads folder", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            document.close();
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignored) {}
            }
        }
    }

    private void populateData() {
        DoctorAssessmentData data = DoctorAssessmentData.getInstance();

        // Patient Info
        ((TextView) findViewById(R.id.tv_report_patient_name)).setText(data.getPatientName() != null ? data.getPatientName() : "N/A");
        ((TextView) findViewById(R.id.tv_report_patient_id)).setText(data.getPatientId() != null ? data.getPatientId() : "N/A");
        
        // Fix DOB Format from MM/DD/YYYY to DD/MM/YYYY
        String dob = data.getPatientDob();
        if (dob != null && dob.contains("/")) {
            try {
                // Check if it's already in DD/MM/YYYY (first part > 12)
                String[] parts = dob.split("/");
                if (parts.length == 3) {
                    int first = Integer.parseInt(parts[0]);
                    int second = Integer.parseInt(parts[1]);
                    
                    // If first part is > 12, it's likely already DD/MM/YYYY
                    if (first <= 12) {
                        // Attempt to parse as MM/DD/YYYY and format to DD/MM/YYYY
                        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date date = inputFormat.parse(dob);
                        if (date != null) {
                            dob = outputFormat.format(date);
                        }
                    }
                }
            } catch (Exception ignored) {}
        }
        ((TextView) findViewById(R.id.tv_report_dob)).setText(dob != null ? dob : "N/A");

        // Nutrient Markers
        float glucose = data.getGlucoseLevel();
        ((TextView) findViewById(R.id.tv_glucose_value)).setText(String.format(Locale.getDefault(), "%.1f mg/dL", glucose));
        ((ProgressBar) findViewById(R.id.pb_glucose)).setProgress((int) (glucose * 10)); 

        ((TextView) findViewById(R.id.tv_amino_acids)).setText(data.getAminoAcids() + " mg/dL"); 
        ((TextView) findViewById(R.id.tv_vitamins)).setText(data.getVitamins() + " mg/dL");

        // By-products
        ((TextView) findViewById(R.id.tv_lactate)).setText(String.format(Locale.getDefault(), "%.1f", data.getLactateLevel()));
        ((TextView) findViewById(R.id.tv_ammonia)).setText(String.valueOf(data.getAmmonia()));
        ((TextView) findViewById(R.id.tv_urea)).setText(String.format(Locale.getDefault(), "%.1f", data.getPyruvateLevel()));

        // Signals
        float oxygen = data.getOxygenUptake();
        ((TextView) findViewById(R.id.tv_oxygen_uptake)).setText(String.format(Locale.getDefault(), "%.1f mL/min", oxygen));
        
        float co2 = data.getCo2Release();
        ((TextView) findViewById(R.id.tv_co2_release)).setText(String.format(Locale.getDefault(), "%.1f mL/min", co2));

        ((TextView) findViewById(R.id.tv_ph_change)).setText(String.format(Locale.getDefault(), "%.2f", data.getPhChange()));

        // Results
        int confidence = (int) data.getConfidenceScore();
        ((TextView) findViewById(R.id.tv_total_confidence)).setText(confidence + "%");
        
        TextView tvTag = findViewById(R.id.tv_confidence_tag);
        if (confidence > 80) {
            tvTag.setText("HIGH");
            tvTag.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#70E050")));
        } else {
            tvTag.setText("MODERATE");
            tvTag.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#F59E0B")));
        }

        String prediction = data.getViabilityPrediction();
        ((TextView) findViewById(R.id.tv_report_activity_level)).setText(prediction != null ? prediction : "Optimal");

        String feedback = data.getAiFeedback();
        String notes = data.getQuestionAnswer("notes"); 
        if (notes == null || notes.isEmpty()) notes = data.getQuestionAnswer("doctor_notes");

        StringBuilder fullFeedback = new StringBuilder();
        if (feedback != null && !feedback.isEmpty()) {
            fullFeedback.append(feedback);
        }
        if (notes != null && !notes.isEmpty()) {
            if (fullFeedback.length() > 0) fullFeedback.append("\n\n---\nClinical Notes:\n");
            fullFeedback.append(notes);
        }

        if (fullFeedback.length() > 0) {
            ((TextView) findViewById(R.id.tv_ai_insights_text)).setText(fullFeedback.toString());
        }
    }
}
