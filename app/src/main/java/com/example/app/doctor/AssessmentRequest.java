package com.example.app.doctor;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class AssessmentRequest {
    @SerializedName("patient_id") public String patientId;
    @SerializedName("patient_name") public String patientName;
    @SerializedName("patient_dob") public String patientDob;
    @SerializedName("patient_age") public int patientAge;
    @SerializedName("embryo_count") public int embryoCount;
    @SerializedName("embryo_day") public String embryoDay;
    @SerializedName("culture_duration") public String cultureDuration;
    @SerializedName("questions_data") public Map<String, String> questionsData;

    public AssessmentRequest(String patientId, String patientName, String patientDob, int patientAge, int embryoCount, String embryoDay, String cultureDuration, Map<String, String> questionsData) {
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
