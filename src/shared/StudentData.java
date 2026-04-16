package shared;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Shared DTO used by both server (servlet) and client (JavaFX).
 * Serialized to/from JSON via Jackson.
 */
public class StudentData {
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("mathMarks")
    private double mathMarks;
    @JsonProperty("scienceMarks")
    private double scienceMarks;
    @JsonProperty("englishMarks")
    private double englishMarks;
    @JsonProperty("attendancePercent")
    private double attendancePercent;
    @JsonProperty("assignmentsSubmitted")
    private int    assignmentsSubmitted;
    @JsonProperty("studyHoursPerDay")
    private double studyHoursPerDay;
    @JsonProperty("extracurricularActivities")
    private int    extracurricularActivities;
    @JsonProperty("previousFailures")
    private int    previousFailures;

    public StudentData() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public double getMathMarks() { return mathMarks; }
    public void setMathMarks(double mathMarks) { this.mathMarks = mathMarks; }
    public double getScienceMarks() { return scienceMarks; }
    public void setScienceMarks(double scienceMarks) { this.scienceMarks = scienceMarks; }
    public double getEnglishMarks() { return englishMarks; }
    public void setEnglishMarks(double englishMarks) { this.englishMarks = englishMarks; }
    public double getAttendancePercent() { return attendancePercent; }
    public void setAttendancePercent(double attendancePercent) { this.attendancePercent = attendancePercent; }
    public int getAssignmentsSubmitted() { return assignmentsSubmitted; }
    public void setAssignmentsSubmitted(int assignmentsSubmitted) { this.assignmentsSubmitted = assignmentsSubmitted; }
    public double getStudyHoursPerDay() { return studyHoursPerDay; }
    public void setStudyHoursPerDay(double studyHoursPerDay) { this.studyHoursPerDay = studyHoursPerDay; }
    public int getExtracurricularActivities() { return extracurricularActivities; }
    public void setExtracurricularActivities(int extracurricularActivities) { this.extracurricularActivities = extracurricularActivities; }
    public int getPreviousFailures() { return previousFailures; }
    public void setPreviousFailures(int previousFailures) { this.previousFailures = previousFailures; }
}
