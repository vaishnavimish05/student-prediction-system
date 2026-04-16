package client.model;

import shared.PredictionResult;
import shared.StudentData;

/**
 * CLIENT MODEL (MVC) — holds form state and last prediction result.
 * JavaFX controllers bind to this model.
 */
public class StudentModel {

    // Form inputs
    private String name     = "";
    private String email    = "";
    private double math     = 0;
    private double science  = 0;
    private double english  = 0;
    private double attendance = 75;
    private int    assignments = 8;
    private double studyHours  = 3.0;
    private int    extracurricular = 0;
    private int    previousFailures = 0;

    // Last prediction result
    private PredictionResult lastResult;

    public StudentData toStudentData() {
        StudentData d = new StudentData();
        d.setName(name);
        d.setEmail(email.isEmpty() ? name.toLowerCase().replaceAll("\\s+", ".") + "@student.edu" : email);
        d.setMathMarks(math);
        d.setScienceMarks(science);
        d.setEnglishMarks(english);
        d.setAttendancePercent(attendance);
        d.setAssignmentsSubmitted(assignments);
        d.setStudyHoursPerDay(studyHours);
        d.setExtracurricularActivities(extracurricular);
        d.setPreviousFailures(previousFailures);
        return d;
    }

    // Getters and setters
    public String getName()                          { return name; }
    public void   setName(String name)               { this.name = name; }
    public String getEmail()                         { return email; }
    public void   setEmail(String email)             { this.email = email; }
    public double getMath()                          { return math; }
    public void   setMath(double math)               { this.math = math; }
    public double getScience()                       { return science; }
    public void   setScience(double science)         { this.science = science; }
    public double getEnglish()                       { return english; }
    public void   setEnglish(double english)         { this.english = english; }
    public double getAttendance()                    { return attendance; }
    public void   setAttendance(double attendance)   { this.attendance = attendance; }
    public int    getAssignments()                   { return assignments; }
    public void   setAssignments(int assignments)    { this.assignments = assignments; }
    public double getStudyHours()                    { return studyHours; }
    public void   setStudyHours(double studyHours)   { this.studyHours = studyHours; }
    public int    getExtracurricular()               { return extracurricular; }
    public void   setExtracurricular(int e)          { this.extracurricular = e; }
    public int    getPreviousFailures()              { return previousFailures; }
    public void   setPreviousFailures(int f)         { this.previousFailures = f; }
    public PredictionResult getLastResult()          { return lastResult; }
    public void   setLastResult(PredictionResult r)  { this.lastResult = r; }
}
