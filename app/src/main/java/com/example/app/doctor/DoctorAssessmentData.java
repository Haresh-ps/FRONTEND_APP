package com.example.app.doctor;

public class DoctorAssessmentData {
    private static DoctorAssessmentData instance;
    
    private String embryoDay = "";
    private String cultureDuration = "";
    private int embryoCount = 1;
    private int assessmentId = -1; // -1 means not created yet
    private String assessmentDate = "";
    
    // Patient Details
    private String patientId = "";
    private String patientName = "";
    private String patientDob = "";
    private int patientAge = 0;
    
    // Metabolic Data
    private float glucoseLevel = 0f;
    private float lactateLevel = 0f;
    private float pyruvateLevel = 0f;
    private float oxidativeStress = 0f;
    
    // New metabolic markers
    private float oxygenUptake = 0f;
    private float co2Release = 0f;
    private int aminoAcids = 0;
    private int vitamins = 0;
    private int ammonia = 0;
    private float phChange = 0f;
    
    // Analysis Result
    private float confidenceScore = 0f;
    private String viabilityPrediction = "";
    private String aiFeedback = "";
    private String uploadedImageUri = "";
    
    // Store generic answers for Q1-Q6
    private java.util.Map<String, String> questionsData = new java.util.HashMap<>();

    private DoctorAssessmentData() {}

    public static synchronized DoctorAssessmentData getInstance() {
        if (instance == null) {
            instance = new DoctorAssessmentData();
        }
        return instance;
    }

    public String getEmbryoDay() { return embryoDay; }
    public void setEmbryoDay(String embryoDay) { this.embryoDay = embryoDay; }

    public String getCultureDuration() { return cultureDuration; }
    public void setCultureDuration(String cultureDuration) { this.cultureDuration = cultureDuration; }

    public int getEmbryoCount() { return embryoCount; }
    public void setEmbryoCount(int embryoCount) { this.embryoCount = embryoCount; }

    public int getAssessmentId() { return assessmentId; }
    public void setAssessmentId(int assessmentId) { this.assessmentId = assessmentId; }

    public String getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(String date) { this.assessmentDate = date; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientDob() { return patientDob; }
    public void setPatientDob(String patientDob) { this.patientDob = patientDob; }

    public int getPatientAge() { return patientAge; }
    public void setPatientAge(int patientAge) { this.patientAge = patientAge; }

    public void setQuestionAnswer(String key, String value) {
        questionsData.put(key, value);
    }

    public String getQuestionAnswer(String key) {
        return questionsData.getOrDefault(key, "");
    }

    public java.util.Map<String, String> getQuestionsData() {
        return questionsData;
    }

    public void setQuestionsData(java.util.Map<String, String> data) { this.questionsData = data; }

    public float getGlucoseLevel() { return glucoseLevel; }
    public void setGlucoseLevel(float glucoseLevel) { this.glucoseLevel = glucoseLevel; }

    public float getLactateLevel() { return lactateLevel; }
    public void setLactateLevel(float lactateLevel) { this.lactateLevel = lactateLevel; }

    public float getPyruvateLevel() { return pyruvateLevel; }
    public void setPyruvateLevel(float pyruvateLevel) { this.pyruvateLevel = pyruvateLevel; }

    public float getOxidativeStress() { return oxidativeStress; }
    public void setOxidativeStress(float oxidativeStress) { this.oxidativeStress = oxidativeStress; }

    public float getOxygenUptake() { return oxygenUptake; }
    public void setOxygenUptake(float oxygenUptake) { this.oxygenUptake = oxygenUptake; }

    public float getCo2Release() { return co2Release; }
    public void setCo2Release(float co2Release) { this.co2Release = co2Release; }

    public int getAminoAcids() { return aminoAcids; }
    public void setAminoAcids(int aminoAcids) { this.aminoAcids = aminoAcids; }

    public int getVitamins() { return vitamins; }
    public void setVitamins(int vitamins) { this.vitamins = vitamins; }

    public int getAmmonia() { return ammonia; }
    public void setAmmonia(int ammonia) { this.ammonia = ammonia; }

    public float getPhChange() { return phChange; }
    public void setPhChange(float phChange) { this.phChange = phChange; }

    public float getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(float confidenceScore) { this.confidenceScore = confidenceScore; }

    public String getViabilityPrediction() { return viabilityPrediction; }
    public void setViabilityPrediction(String viabilityPrediction) { this.viabilityPrediction = viabilityPrediction; }

    public String getAiFeedback() { return aiFeedback; }
    public void setAiFeedback(String aiFeedback) { this.aiFeedback = aiFeedback; }

    public String getUploadedImageUri() { return uploadedImageUri; }
    public void setUploadedImageUri(String uploadedImageUri) { this.uploadedImageUri = uploadedImageUri; }

    public void clear() {
        embryoDay = "";
        cultureDuration = "";
        embryoCount = 1;
        questionsData.clear();
        assessmentId = -1;
        assessmentDate = "";
        patientId = "";
        patientName = "";
        patientDob = "";
        patientAge = 0;
        glucoseLevel = 0f;
        lactateLevel = 0f;
        pyruvateLevel = 0f;
        oxidativeStress = 0f;
        oxygenUptake = 0f;
        co2Release = 0f;
        confidenceScore = 0f;
        viabilityPrediction = "";
        aiFeedback = "";
        uploadedImageUri = "";
        aminoAcids = 0;
        vitamins = 0;
        ammonia = 0;
        phChange = 0f;
    }
}
