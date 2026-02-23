package com.example.app.doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;

public class DoctorUploadPicActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private android.net.Uri imageUri;
    private android.widget.ImageView ivPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_upload_pic);

        ivPreview = findViewById(R.id.iv_preview); // Assuming there's an ImageView in layout? If not, ignore or add dynamic.
        // Actually the layout XML wasn't shown fully, usually upload activities have a preview. 
        // I will assume for now we just hold the URI.

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_upload).setOnClickListener(v -> openGallery());
        
        findViewById(R.id.btn_next).setOnClickListener(v -> uploadImage());
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            android.widget.Toast.makeText(this, "Image Selected", android.widget.Toast.LENGTH_SHORT).show();
            // Optional: ivPreview.setImageURI(imageUri);
        }
    }

    private void uploadImage() {
        if (imageUri == null) {
            android.widget.Toast.makeText(this, "Please select an image first", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            java.io.File file = FileUtils.getFileFromUri(this, imageUri);
            
            // Create RequestBody instance from file
            okhttp3.RequestBody requestFile = okhttp3.RequestBody.create(
                    okhttp3.MediaType.parse(getContentResolver().getType(imageUri)),
                    file
            );

            // MultipartBody.Part is used to send also the actual file name
            okhttp3.MultipartBody.Part body = okhttp3.MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            // Assessment ID
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
            
            // Show loading...
            android.app.ProgressDialog pd = new android.app.ProgressDialog(this);
            pd.setMessage("Uploading...");
            pd.show();

            call.enqueue(new retrofit2.Callback<MediaResponse>() {
                @Override
                public void onResponse(retrofit2.Call<MediaResponse> call, retrofit2.Response<MediaResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful()) {
                        android.widget.Toast.makeText(DoctorUploadPicActivity.this, "Upload Successful!", android.widget.Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoctorUploadPicActivity.this, DoctorPicPreviewActivity.class); // Or next activity
                        startActivity(intent);
                    } else {
                         android.widget.Toast.makeText(DoctorUploadPicActivity.this, "Upload Failed: " + response.message(), android.widget.Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<MediaResponse> call, Throwable t) {
                    pd.dismiss();
                    android.widget.Toast.makeText(DoctorUploadPicActivity.this, "Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            android.widget.Toast.makeText(this, "File Error: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}
