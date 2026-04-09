package com.example.app.doctor;

import com.google.gson.annotations.SerializedName;

public class ProfileRequest {
    @SerializedName("clinic_name") public String clinicName;
    @SerializedName("specialization") public String specialization;
    @SerializedName("phone_number") public String phoneNumber;
    @SerializedName("full_name") public String fullName;
    @SerializedName("address") public String address;
    @SerializedName("experience_years") public String experienceYears;
    @SerializedName("email") public String email;

    public ProfileRequest(String clinicName, String specialization, String phoneNumber, String fullName, String address, String experienceYears, String email) {
        this.clinicName = clinicName;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.address = address;
        this.experienceYears = experienceYears;
        this.email = email;
    }
}
