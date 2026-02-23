package com.example.app.doctor;

import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/auth/login/")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/auth/signup/")
    Call<Void> signup(@Body SignupRequest request);

    @POST("api/auth/profile/")
    Call<ProfileResponse> createProfile(@Body ProfileRequest request);

    @retrofit2.http.GET("api/auth/profile/")
    Call<ProfileResponse> getProfile();

    @retrofit2.http.PUT("api/auth/profile/")
    Call<ProfileResponse> updateProfile(@Body ProfileRequest request);

    @POST("api/auth/assessments/")
    Call<AssessmentResponse> createAssessment(@Body AssessmentRequest request);

    @retrofit2.http.Multipart
    @POST("api/auth/upload/")
    Call<MediaResponse> uploadMedia(
            @retrofit2.http.Part okhttp3.MultipartBody.Part file,
            @retrofit2.http.Part("assessment_id") okhttp3.RequestBody assessmentId
    );

    @POST("api/auth/assessments/{id}/analyze/")
    Call<AnalysisResponse> analyzeAssessment(
            @retrofit2.http.Path("id") int assessmentId,
            @Body AnalysisRequest request
    );
}

class AnalysisRequest {
    @SerializedName("glucose_level") float glucoseLevel;
    @SerializedName("lactate_level") float lactateLevel;
    @SerializedName("pyruvate_level") float pyruvateLevel;
    @SerializedName("oxidative_stress") float oxidativeStress;

    public AnalysisRequest(float glucoseLevel, float lactateLevel, float pyruvateLevel, float oxidativeStress) {
        this.glucoseLevel = glucoseLevel;
        this.lactateLevel = lactateLevel;
        this.pyruvateLevel = pyruvateLevel;
        this.oxidativeStress = oxidativeStress;
    }
}

// Data Classes

class LoginRequest {
    String username;
    String password;
    public LoginRequest(String username, String password) { this.username = username; this.password = password; }
}

class LoginResponse {
    @SerializedName("access") String access;
    @SerializedName("refresh") String refresh;
}

class SignupRequest {
    String username;
    String email;
    String password;
    public SignupRequest(String username, String email, String password) { this.username = username; this.email = email; this.password = password; }
}

class ProfileRequest {
    @SerializedName("clinic_name") String clinicName;
    @SerializedName("specialization") String specialization;
    @SerializedName("phone_number") String phoneNumber;
    @SerializedName("full_name") String fullName;
    @SerializedName("address") String address;
    @SerializedName("experience_years") String experienceYears;

    public ProfileRequest(String clinicName, String specialization, String phoneNumber, String fullName, String address, String experienceYears) {
        this.clinicName = clinicName;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.address = address;
        this.experienceYears = experienceYears;
    }
}

class ProfileResponse {
    @SerializedName("clinic_name") String clinicName;
    @SerializedName("specialization") String specialization;
    @SerializedName("full_name") String fullName;
}

class AssessmentRequest {
    @SerializedName("patient_id") String patientId;
    @SerializedName("patient_name") String patientName;
    @SerializedName("patient_dob") String patientDob;
    @SerializedName("patient_age") int patientAge;
    @SerializedName("embryo_count") int embryoCount;
    @SerializedName("embryo_day") String embryoDay;
    @SerializedName("culture_duration") String cultureDuration;
    // Map of questions
    @SerializedName("questions_data") java.util.Map<String, String> questionsData; 

    public AssessmentRequest(String patientId, String patientName, String patientDob, int patientAge, int embryoCount, String embryoDay, String cultureDuration, java.util.Map<String, String> questionsData) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientDob = patientDob;
        this.patientAge = patientAge;
        this.embryoCount = embryoCount;
        this.embryoDay = embryoDay;
        this.cultureDuration = cultureDuration;
        this.questionsData = questionsData;
    }
}

class AssessmentResponse {
    @SerializedName("id") int id;
    @SerializedName("created_at") String createdAt;
}

class MediaResponse {
    @SerializedName("id") int id;
    @SerializedName("file_url") String fileUrl;
}

class AnalysisResponse {
    @SerializedName("confidence_score") double confidenceScore;
    @SerializedName("viability_prediction") String viabilityPrediction;
    @SerializedName("ai_feedback") String aiFeedback;
}
