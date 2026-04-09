package com.example.app.doctor;

import com.google.gson.annotations.SerializedName;

public class AnalysisRequest {
    @SerializedName("glucose_level") public float glucoseLevel;
    @SerializedName("lactate_level") public float lactateLevel;
    @SerializedName("pyruvate_level") public float pyruvateLevel;
    @SerializedName("oxidative_stress") public float oxidativeStress;

    public AnalysisRequest(float glucoseLevel, float lactateLevel, float pyruvateLevel, float oxidativeStress) {
        this.glucoseLevel = glucoseLevel;
        this.lactateLevel = lactateLevel;
        this.pyruvateLevel = pyruvateLevel;
        this.oxidativeStress = oxidativeStress;
    }
}
