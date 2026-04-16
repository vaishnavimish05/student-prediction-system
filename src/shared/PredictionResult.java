package shared;


public class PredictionResult {
    private Long   studentId;
    private String studentName;

    // Core Predictions
    private String passOrFail;
    private String grade;
    private double placementProbability;
    private String dropoutRisk;

    // Computed Scores
    private double academicScore;
    private double attendanceScore;
    private double overallRiskScore;
    private double confidenceScore; // ML confidence in prediction (0-100%)

    private String personalizedFeedback;
    private String improvementPlan;
    private String studyStrategy;

    public PredictionResult() {}

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getPassOrFail() { return passOrFail; }
    public void setPassOrFail(String passOrFail) { this.passOrFail = passOrFail; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public double getPlacementProbability() { return placementProbability; }
    public void setPlacementProbability(double placementProbability) { this.placementProbability = placementProbability; }
    public String getDropoutRisk() { return dropoutRisk; }
    public void setDropoutRisk(String dropoutRisk) { this.dropoutRisk = dropoutRisk; }
    public double getAcademicScore() { return academicScore; }
    public void setAcademicScore(double academicScore) { this.academicScore = academicScore; }
    public double getAttendanceScore() { return attendanceScore; }
    public void setAttendanceScore(double attendanceScore) { this.attendanceScore = attendanceScore; }
    public double getOverallRiskScore() { return overallRiskScore; }
    public void setOverallRiskScore(double overallRiskScore) { this.overallRiskScore = overallRiskScore; }
    public double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }
    public String getPersonalizedFeedback() { return personalizedFeedback; }
    public void setPersonalizedFeedback(String personalizedFeedback) { this.personalizedFeedback = personalizedFeedback; }
    public String getImprovementPlan() { return improvementPlan; }
    public void setImprovementPlan(String improvementPlan) { this.improvementPlan = improvementPlan; }
    public String getStudyStrategy() { return studyStrategy; }
    public void setStudyStrategy(String studyStrategy) { this.studyStrategy = studyStrategy; }
}
