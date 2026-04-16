package server.service;

import server.ai.StudentAIEngine;
import server.dao.StudentDAO;
import server.model.Student;
import server.ml.SmartPredictor;
import server.ml.StudentHistoryManager;
import shared.PredictionResult;
import shared.StudentData;

import java.util.List;
import java.util.Map;

/**
 * SERVICE layer — core business logic (MVC Model).
 * Orchestrates: DAO → Algorithm → AI Engine → Result
 */
public class PredictionService {

    private final StudentDAO         dao       = new StudentDAO();
    private final StudentAIEngine    aiEngine  = new StudentAIEngine();
    private final SmartPredictor     predictor = new SmartPredictor();
    private final StudentHistoryManager history = StudentHistoryManager.getInstance();

    // ── Main prediction entry point ───────────────────────────────────────
    public PredictionResult predict(StudentData req) {
        // 1. Build and persist Student entity
        Student student = buildStudent(req);
        student = dao.save(student);

        // 2. Run weighted prediction algorithm
        PredictionResult result = runAlgorithm(student);

        // 3. Enhance with smart predictions from historical data
        result = improveWithHistory(student, result);

        // 4. Generate AI feedback (rule-based, no API)
        aiEngine.generateFeedback(student, result);

        // 5. Save to history (in-memory with file persistence)
        history.addRecord(student, result);

        System.out.printf("[SERVICE] Prediction: %s → Grade:%s Risk:%s Placement:%.0f%% (Confidence:%.0f%%)%n",
            student.getName(), result.getGrade(),
            result.getDropoutRisk(), result.getPlacementProbability() * 100,
            result.getConfidenceScore());

        return result;
    }

    // ── Enhance predictions using historical data ─────────────────────────
    private PredictionResult improveWithHistory(Student s, PredictionResult r) {
        // Try to improve grade prediction
        String historicalGrade = predictor.predictGradeFromHistory(s);
        if (historicalGrade != null) {
            r.setGrade(historicalGrade);
        }

        // Refine placement probability
        double refinedPlacement = predictor.refinePlacementProbability(s, r.getPlacementProbability());
        r.setPlacementProbability(refinedPlacement);

        // Validate dropout risk prediction
        String historicalRisk = predictor.detectRiskFromHistory(s);
        if (historicalRisk != null) {
            r.setDropoutRisk(historicalRisk);
        }

        // Set confidence score
        r.setConfidenceScore(predictor.getConfidenceScore(s));

        System.out.println("[ML] Enhanced prediction with historical patterns");
        return r;
    }

    // ── History: return all stored students ───────────────────────────────
    public List<Student> getAllStudents() {
        return dao.findAll();
    }

    // ── Get prediction history ─────────────────────────────────────────────
    public List<StudentHistoryManager.PredictionRecord> getPredictionHistory() {
        return history.getAllRecords();
    }

    public Map<String, String> getHistoricalInsights() {
        return predictor.getHistoricalInsights();
    }

    // ── Weighted scoring algorithm ────────────────────────────────────────
    private PredictionResult runAlgorithm(Student s) {
        PredictionResult r = new PredictionResult();
        r.setStudentId(s.getId());
        r.setStudentName(s.getName());

        double avg = s.getAvgMarks();
        r.setAcademicScore(avg);
        r.setAttendanceScore(s.getAttendancePercent());

        // Overall score (0-100) — weighted composite
        double score =
              avg                                    * 0.40   // 40% academic
            + s.getAttendancePercent()               * 0.25   // 25% attendance
            + (s.getAssignmentsSubmitted() / 10.0) * 100 * 0.15  // 15% assignments
            + Math.min(s.getStudyHoursPerDay() / 6.0, 1.0) * 100 * 0.10 // 10% study (cap 6h)
            + s.getExtracurricularActivities() * 10 * 0.05   // 5% extra
            - s.getPreviousFailures() * 5;                   // penalty

        score = Math.max(0, Math.min(100, score));
        r.setOverallRiskScore(score);

        // Pass/Fail
        boolean passes = avg >= 40 && s.getAttendancePercent() >= 60;
        r.setPassOrFail(passes ? "PASS" : "FAIL");

        // Grade
        r.setGrade(calcGrade(avg, s.getAttendancePercent()));

        // Placement probability
        r.setPlacementProbability(calcPlacement(s, score));

        // Dropout risk
        r.setDropoutRisk(calcDropout(s, score));

        return r;
    }

    private String calcGrade(double avg, double att) {
        boolean lowAtt = att < 75;
        if      (avg >= 90) return lowAtt ? "A"  : "A+";
        else if (avg >= 80) return lowAtt ? "B"  : "A";
        else if (avg >= 70) return lowAtt ? "C"  : "B";
        else if (avg >= 60) return lowAtt ? "D"  : "C";
        else if (avg >= 40) return "D";
        else                return "F";
    }

    private double calcPlacement(Student s, double score) {
        double p = score / 100.0 * 0.60;
        if      (s.getAvgMarks() >= 75) p += 0.20;
        else if (s.getAvgMarks() >= 60) p += 0.10;
        if (s.getAttendancePercent() >= 85) p += 0.10;
        if (s.getExtracurricularActivities() > 0) p += 0.05;
        p -= s.getPreviousFailures() * 0.05;
        if (s.getStudyHoursPerDay() >= 4) p += 0.05;
        return Math.max(0.05, Math.min(0.98, p));
    }

    private String calcDropout(Student s, double score) {
        int pts = 0;
        if      (s.getAvgMarks() < 40)              pts += 3;
        else if (s.getAvgMarks() < 60)              pts += 1;
        if      (s.getAttendancePercent() < 60)     pts += 3;
        else if (s.getAttendancePercent() < 75)     pts += 1;
        if (s.getAssignmentsSubmitted() <= 3)       pts += 2;
        if (s.getPreviousFailures() >= 2)            pts += 2;
        if (s.getStudyHoursPerDay() < 1.0)           pts += 1;
        if      (pts >= 6) return "HIGH";
        else if (pts >= 3) return "MEDIUM";
        else               return "LOW";
    }

    private Student buildStudent(StudentData req) {
        Student s = new Student();
        s.setName(req.getName());
        s.setEmail(req.getEmail());
        s.setMathMarks(req.getMathMarks());
        s.setScienceMarks(req.getScienceMarks());
        s.setEnglishMarks(req.getEnglishMarks());
        double avg = (req.getMathMarks() + req.getScienceMarks() + req.getEnglishMarks()) / 3.0;
        s.setAvgMarks(Math.round(avg * 10.0) / 10.0);
        s.setAttendancePercent(req.getAttendancePercent());
        s.setAssignmentsSubmitted(req.getAssignmentsSubmitted());
        s.setStudyHoursPerDay(req.getStudyHoursPerDay());
        s.setExtracurricularActivities(req.getExtracurricularActivities());
        s.setPreviousFailures(req.getPreviousFailures());
        return s;
    }
}
