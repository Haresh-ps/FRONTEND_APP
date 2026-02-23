package com.example.app.doctor;

public class DoctorAssessmentData {
    private static DoctorAssessmentData instance;
    
    private String embryoDay = "Blastocyst";
    private String cultureDuration = "5 days";
    private int embryoCount = 1;
    private int assessmentId = -1; // -1 means not created yet
    
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

    public void clear() {
        embryoDay = "Blastocyst";
        cultureDuration = "5 days";
        embryoCount = 1;
        questionsData.clear();
        assessmentId = -1;
        patientId = "";
        patientName = "";
        patientDob = "";
        patientAge = 0;
        glucoseLevel = 0f;
        lactateLevel = 0f;
        pyruvateLevel = 0f;
        oxidativeStress = 0f;
    }
}
