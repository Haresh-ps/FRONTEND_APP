package com.example.app.doctor;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface ApiService {

    @POST("api/auth/login/")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/auth/signup/")
    Call<Void> signup(@Body SignupRequest request);

    @POST("api/auth/profile/")
    Call<ProfileResponse> createProfile(@Body ProfileRequest request);

    @GET("api/auth/profile/")
    Call<ProfileResponse> getProfile();

    @PUT("api/auth/profile/")
    Call<ProfileResponse> updateProfile(@Body ProfileRequest request);

    @POST("api/auth/assessments/")
    Call<AssessmentResponse> createAssessment(@Body AssessmentRequest request);

    @Multipart
    @POST("api/auth/upload/")
    Call<MediaResponse> uploadMedia(
            @Part MultipartBody.Part file,
            @Part("assessment_id") RequestBody assessmentId
    );

    @POST("api/auth/assessments/{id}/analyze/")
    Call<AnalysisResponse> analyzeAssessment(
            @Path("id") int assessmentId,
            @Body AnalysisRequest request
    );

    @POST("api/auth/chat/")
    Call<ChatResponse> chat(@Body ChatRequest request);

    @GET("api/auth/reports/")
    Call<List<AssessmentResponse>> listReports();
}
