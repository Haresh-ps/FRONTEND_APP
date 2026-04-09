package com.example.app.doctor;

import com.google.gson.annotations.SerializedName;

public class AssessmentResponse {
    @SerializedName("id") public int id;
    @SerializedName("patient_id") public String patientId;
    @SerializedName("patient_name") public String patientName;
    @SerializedName("patient_dob") public String patientDob;
    @SerializedName("patient_age") public int patientAge;
    @SerializedName("embryo_count") public int embryoCount;
    @SerializedName("embryo_day") public String embryoDay;
    @SerializedName("culture_duration") public String cultureDuration;
    @SerializedName("culture_medium") public String cultureMedium;
    @SerializedName("media_color_change") public String mediaColorChange;
    @SerializedName("ph_deviation") public String phDeviation;
    @SerializedName("visual_clarity") public String visualClarity;
    @SerializedName("doctor_notes") public String doctorNotes;
    @SerializedName("created_at") public String createdAt;
    @SerializedName("analysis") public java.util.List<AnalysisData> analysis;

    public static class AnalysisData {
        @SerializedName("confidence_score") public float confidenceScore;
        @SerializedName("viability_prediction") public String viabilityPrediction;
        @SerializedName("ai_feedback") public String aiFeedback;
        @SerializedName("glucose_level") public float glucoseLevel;
        @SerializedName("lactate_level") public float lactateLevel;
        @SerializedName("pyruvate_level") public float pyruvateLevel;
        @SerializedName("oxidative_stress") public float oxidativeStress;
        @SerializedName("amino_acids") public int aminoAcids;
        @SerializedName("vitamins") public int vitamins;
        @SerializedName("ammonia") public int ammonia;
        @SerializedName("ph_change") public float phChange;
        @SerializedName("oxygen_uptake") public float oxygenUptake;
        @SerializedName("co2_release") public float co2Release;
    }
}
