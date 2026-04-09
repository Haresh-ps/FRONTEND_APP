package com.example.app.doctor;

import com.google.gson.annotations.SerializedName;

public class AnalysisResponse {
    @SerializedName("confidence_score") public double confidenceScore;
    @SerializedName("viability_prediction") public String viabilityPrediction;
    @SerializedName("ai_feedback") public String aiFeedback;
    @SerializedName("glucose_level") public double glucoseLevel;
    @SerializedName("lactate_level") public double lactateLevel;
    @SerializedName("pyruvate_level") public double pyruvateLevel;
    @SerializedName("oxidative_stress") public double oxidativeStress;
    @SerializedName("amino_acids") public int aminoAcids;
    @SerializedName("vitamins") public int vitamins;
    @SerializedName("ammonia") public int ammonia;
    @SerializedName("ph_change") public double phChange;
    @SerializedName("oxygen_uptake") public double oxygenUptake;
    @SerializedName("co2_release") public double co2Release;
}
