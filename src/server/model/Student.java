package server.model;
public class Student {
    private Long   id;
    private String name;
    private String email;
    private double mathMarks;
    private double scienceMarks;
    private double englishMarks;
    private double avgMarks;
    private double attendancePercent;
    private int    assignmentsSubmitted;
    private double studyHoursPerDay;
    private int    extracurricularActivities;
    private int    previousFailures;

    public Student() {}

    public Long   getId()                          { return id; }
    public void   setId(Long id)                   { this.id = id; }
    public String getName()                        { return name; }
    public void   setName(String name)             { this.name = name; }
    public String getEmail()                       { return email; }
    public void   setEmail(String email)           { this.email = email; }
    public double getMathMarks()                   { return mathMarks; }
    public void   setMathMarks(double v)           { this.mathMarks = v; }
    public double getScienceMarks()                { return scienceMarks; }
    public void   setScienceMarks(double v)        { this.scienceMarks = v; }
    public double getEnglishMarks()                { return englishMarks; }
    public void   setEnglishMarks(double v)        { this.englishMarks = v; }
    public double getAvgMarks()                    { return avgMarks; }
    public void   setAvgMarks(double avgMarks)     { this.avgMarks = avgMarks; }
    public double getAttendancePercent()           { return attendancePercent; }
    public void   setAttendancePercent(double v)   { this.attendancePercent = v; }
    public int    getAssignmentsSubmitted()        { return assignmentsSubmitted; }
    public void   setAssignmentsSubmitted(int v)   { this.assignmentsSubmitted = v; }
    public double getStudyHoursPerDay()            { return studyHoursPerDay; }
    public void   setStudyHoursPerDay(double v)    { this.studyHoursPerDay = v; }
    public int    getExtracurricularActivities()   { return extracurricularActivities; }
    public void   setExtracurricularActivities(int v){ this.extracurricularActivities = v; }
    public int    getPreviousFailures()            { return previousFailures; }
    public void   setPreviousFailures(int v)       { this.previousFailures = v; }
}
