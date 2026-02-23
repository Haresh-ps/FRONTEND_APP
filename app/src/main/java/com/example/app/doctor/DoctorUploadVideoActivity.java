package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorUploadVideoActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 2;
    private android.net.Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_upload_video);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_skip).setOnClickListener(v -> {
            Intent intent = new Intent(this, DoctorReadyAnalyzeActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_browse).setOnClickListener(v -> openVideoGallery());

        findViewById(R.id.btn_next).setOnClickListener(v -> uploadVideo());
    }

    private void openVideoGallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();
            android.widget.Toast.makeText(this, "Video Selected", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadVideo() {
        if (videoUri == null) {
            android.widget.Toast.makeText(this, "Please select a video first", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            java.io.File file = FileUtils.getFileFromUri(this, videoUri);
            
            // Limit video size check if needed, but for now allow.
            
            okhttp3.RequestBody requestFile = okhttp3.RequestBody.create(
                    okhttp3.MediaType.parse(getContentResolver().getType(videoUri)),
                    file
            );

            okhttp3.MultipartBody.Part body = okhttp3.MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            int assessmentId = DoctorAssessmentData.getInstance().getAssessmentId();
            if (assessmentId == -1) {
                android.widget.Toast.makeText(this, "Error: Assessment not created yet", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            
            okhttp3.RequestBody assessmentIdBody = okhttp3.RequestBody.create(
                    okhttp3.MediaType.parse("text/plain"),
                    String.valueOf(assessmentId)
            );

            ApiService apiService = RetrofitClient.getApiService();
            retrofit2.Call<MediaResponse> call = apiService.uploadMedia(body, assessmentIdBody);
            
            // Show upload progress activity or dialog
            Intent progressIntent = new Intent(this, DoctorUploadProgressActivity.class);
            // We might want to start uploading in background service or show blocking dialog.
            // For simplicity, let's show dialog and then go to progress activity or just assume progress activity IS the loader.
            // But the UI flow suggests "Next" -> "UploadProgressActivity".
            // So maybe we should just start the upload AND navigate, but passing the call?
            // Safer: Upload here with dialog, then go to ReadyAnalyze. 
            // OR: Start upload, and if successful, go. 
            
            android.app.ProgressDialog pd = new android.app.ProgressDialog(this);
            pd.setMessage("Uploading Video...");
            pd.setCancelable(false);
            pd.show();

            call.enqueue(new retrofit2.Callback<MediaResponse>() {
                @Override
                public void onResponse(retrofit2.Call<MediaResponse> call, retrofit2.Response<MediaResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful()) {
                        android.widget.Toast.makeText(DoctorUploadVideoActivity.this, "Video Uploaded!", android.widget.Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoctorUploadVideoActivity.this, DoctorReadyAnalyzeActivity.class); // Skip progress activity if fast? Or go there.
                        startActivity(intent);
                    } else {
                         android.widget.Toast.makeText(DoctorUploadVideoActivity.this, "Upload Failed: " + response.message(), android.widget.Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<MediaResponse> call, Throwable t) {
                    pd.dismiss();
                    android.widget.Toast.makeText(DoctorUploadVideoActivity.this, "Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            android.widget.Toast.makeText(this, "File Error: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}
