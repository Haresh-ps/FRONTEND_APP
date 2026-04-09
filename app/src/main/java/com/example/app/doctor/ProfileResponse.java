package com.example.app.doctor;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("clinic_name") public String clinicName;
    @SerializedName("specialization") public String specialization;
    @SerializedName("full_name") public String fullName;
    @SerializedName("email") public String email;
    @SerializedName("phone_number") public String phoneNumber;
    @SerializedName("address") public String address;
    @SerializedName("experience_years") public String experienceYears;
}
