package server.ml;
import server.model.Student;
import shared.PredictionResult;
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class StudentHistoryManager {
    
    private static final StudentHistoryManager INSTANCE = new StudentHistoryManager();
    private final List<PredictionRecord> history = new CopyOnWriteArrayList<>();
    private static final String HISTORY_FILE = "student_predictions_history.csv";
    
    public static class PredictionRecord {
        public long timestamp;
        public String name, email;
        public double mathMarks, scienceMarks, englishMarks;
        public double attendancePercent, studyHoursPerDay;
        public int assignmentsSubmitted, extracurricularActivities, previousFailures;
        public String predictedGrade, dropoutRisk, passOrFail;
        public double placementProbability, overallRiskScore;
        
        public PredictionRecord() {}
        
        public PredictionRecord(Student s, PredictionResult r) {
            this.timestamp = System.currentTimeMillis();
            this.name = s.getName();
            this.email = s.getEmail();
            this.mathMarks = s.getMathMarks();
            this.scienceMarks = s.getScienceMarks();
            this.englishMarks = s.getEnglishMarks();
            this.attendancePercent = s.getAttendancePercent();
            this.studyHoursPerDay = s.getStudyHoursPerDay();
            this.assignmentsSubmitted = s.getAssignmentsSubmitted();
            this.extracurricularActivities = s.getExtracurricularActivities();
            this.previousFailures = s.getPreviousFailures();
            this.predictedGrade = r.getGrade();
            this.dropoutRisk = r.getDropoutRisk();
            this.passOrFail = r.getPassOrFail();
            this.placementProbability = r.getPlacementProbability();
            this.overallRiskScore = r.getOverallRiskScore();
        }
    }
    
    private StudentHistoryManager() {
        loadFromFile();
    }
    
    public static StudentHistoryManager getInstance() {
        return INSTANCE;
    }
    
    public void addRecord(Student s, PredictionResult r) {
        PredictionRecord record = new PredictionRecord(s, r);
        history.add(record);
        saveToFile(); // Persist immediately
        System.out.printf("[HISTORY] Saved prediction for %s (Total: %d)%n", s.getName(), history.size());
    }
    
    public List<PredictionRecord> getAllRecords() {
        return new ArrayList<>(history);
    }
    
    public List<PredictionRecord> getRecordsByRisk(String riskLevel) {
        return history.stream()
            .filter(r -> r.dropoutRisk.equals(riskLevel))
            .toList();
    }
    
    public List<PredictionRecord> getRecentRecords(int limit) {
        return history.stream()
            .sorted((a, b) -> Long.compare(b.timestamp, a.timestamp))
            .limit(limit)
            .toList();
    }
    
    public Map<String, Integer> getGradeDistribution() {
        Map<String, Integer> dist = new LinkedHashMap<>();
        history.stream()
            .map(r -> r.predictedGrade)
            .forEach(grade -> dist.merge(grade, 1, Integer::sum));
        return dist;
    }
    
    public double getAverageAttendance() {
        return history.isEmpty() ? 0 : 
            history.stream().mapToDouble(r -> r.attendancePercent).average().orElse(0);
    }
    
    public double getAverageMarks() {
        return history.isEmpty() ? 0 :
            history.stream()
            .mapToDouble(r -> (r.mathMarks + r.scienceMarks + r.englishMarks) / 3.0)
            .average().orElse(0);
    }
    
    public long getTotalPredictions() {
        return history.size();
    }
    
    public double getHighRiskPercentage() {
        if (history.isEmpty()) return 0;
        long highRisk = history.stream()
            .filter(r -> "HIGH".equals(r.dropoutRisk))
            .count();
        return (highRisk * 100.0) / history.size();
    }
    
    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HISTORY_FILE))) {
            // Header
            writer.println("timestamp,name,email,mathMarks,scienceMarks,englishMarks,attendance," +
                "studyHours,assignments,extracurricular,failures,grade,dropoutRisk,passOrFail," +
                "placementProbability,riskScore");
            
            // Records
            for (PredictionRecord r : history) {
                writer.printf("%d,%s,%s,%.1f,%.1f,%.1f,%.1f,%.1f,%d,%d,%d,%s,%s,%s,%.2f,%.1f%n",
                    r.timestamp, r.name, r.email, r.mathMarks, r.scienceMarks, r.englishMarks,
                    r.attendancePercent, r.studyHoursPerDay, r.assignmentsSubmitted,
                    r.extracurricularActivities, r.previousFailures, r.predictedGrade,
                    r.dropoutRisk, r.passOrFail, r.placementProbability, r.overallRiskScore);
            }
            System.out.println("[HISTORY] Saved " + history.size() + " records to " + HISTORY_FILE);
        } catch (IOException e) {
            System.err.println("[HISTORY] Failed to save: " + e.getMessage());
        }
    }
    
    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length < 16) continue;
                
                PredictionRecord r = new PredictionRecord();
                r.timestamp = Long.parseLong(parts[0]);
                r.name = parts[1];
                r.email = parts[2];
                r.mathMarks = Double.parseDouble(parts[3]);
                r.scienceMarks = Double.parseDouble(parts[4]);
                r.englishMarks = Double.parseDouble(parts[5]);
                r.attendancePercent = Double.parseDouble(parts[6]);
                r.studyHoursPerDay = Double.parseDouble(parts[7]);
                r.assignmentsSubmitted = Integer.parseInt(parts[8]);
                r.extracurricularActivities = Integer.parseInt(parts[9]);
                r.previousFailures = Integer.parseInt(parts[10]);
                r.predictedGrade = parts[11];
                r.dropoutRisk = parts[12];
                r.passOrFail = parts[13];
                r.placementProbability = Double.parseDouble(parts[14]);
                r.overallRiskScore = Double.parseDouble(parts[15]);
                
                history.add(r);
            }
            System.out.println("[HISTORY] Loaded " + history.size() + " historical records");
        } catch (FileNotFoundException e) {
            System.out.println("[HISTORY] No history file found - starting fresh");
        } catch (IOException e) {
            System.err.println("[HISTORY] Error loading: " + e.getMessage());
        }
    }
    
    public void clear() {
        history.clear();
        new File(HISTORY_FILE).delete();
        System.out.println("[HISTORY] Cleared all records");
    }
}
