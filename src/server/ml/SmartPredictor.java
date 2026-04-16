package server.ml;

import server.model.Student;
import server.ml.StudentHistoryManager.PredictionRecord;

import java.util.*;

public class SmartPredictor {
    
    private final StudentHistoryManager history = StudentHistoryManager.getInstance();
    
    /**
     * Get similar students from history (within 5% deviation on key metrics)
     */
    public List<PredictionRecord> findSimilarStudents(Student s, int limit) {
        double avgMarks = (s.getMathMarks() + s.getScienceMarks() + s.getEnglishMarks()) / 3.0;
        double targetAtt = s.getAttendancePercent();
        
        return history.getAllRecords().stream()
            .filter(r -> {
                double rAvg = (r.mathMarks + r.scienceMarks + r.englishMarks) / 3.0;
                return Math.abs(rAvg - avgMarks) <= 5 &&
                       Math.abs(r.attendancePercent - targetAtt) <= 10;
            })
            .sorted((a, b) -> {
                double aAvg = (a.mathMarks + a.scienceMarks + a.englishMarks) / 3.0;
                double bAvg = (b.mathMarks + b.scienceMarks + b.englishMarks) / 3.0;
                double avgMarkes = (s.getMathMarks() + s.getScienceMarks() + s.getEnglishMarks()) / 3.0;
                return Double.compare(Math.abs(aAvg - avgMarkes), Math.abs(bAvg - avgMarkes));
            })
            .limit(limit)
            .toList();
    }
    
    public String predictGradeFromHistory(Student s) {
        List<PredictionRecord> similar = findSimilarStudents(s, 5);
        if (similar.isEmpty()) return null; // Use rule-based fallback
        
        Map<String, Integer> gradeCount = new HashMap<>();
        for (PredictionRecord r : similar) {
            gradeCount.merge(r.predictedGrade, 1, Integer::sum);
        }
        
        // Return most common grade
        return gradeCount.entrySet().stream()
            .max(Comparator.comparingInt(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    public double refinePlacementProbability(Student s, double baseProb) {
        List<PredictionRecord> similar = findSimilarStudents(s, 10);
        if (similar.isEmpty()) return baseProb;
        
        double avgPlacement = similar.stream()
            .mapToDouble(r -> r.placementProbability)
            .average()
            .orElse(baseProb);
        
        return baseProb * 0.3 + avgPlacement * 0.7;
    }
    
    public String detectRiskFromHistory(Student s) {
        List<PredictionRecord> similar = findSimilarStudents(s, 5);
        if (similar.isEmpty()) return null; // Use rule-based fallback
        
        long highRiskCount = similar.stream()
            .filter(r -> "HIGH".equals(r.dropoutRisk))
            .count();
        
        double riskPercentage = (highRiskCount * 100.0) / similar.size();
        
        if (riskPercentage >= 60) return "HIGH";
        if (riskPercentage >= 30) return "MEDIUM";
        return "LOW";
    }
    
    public double getConfidenceScore(Student s) {
        List<PredictionRecord> similar = findSimilarStudents(s, 5);
        
        double confidence = Math.min(similar.size() * 20, 100); 
        
        if (!similar.isEmpty()) {
            List<String> grades = similar.stream().map(r -> r.predictedGrade).distinct().toList();
            if (grades.size() == 1) confidence = Math.min(confidence + 15, 100); // High agreement
        }
        
        return confidence;
    }
    
    /**
     * Get learning insights based on history
     */
    public Map<String, String> getHistoricalInsights() {
        Map<String, String> insights = new LinkedHashMap<>();
        
        List<PredictionRecord> recent = history.getRecentRecords(100);
        if (recent.isEmpty()) {
            insights.put("Total Students", "0");
            return insights;
        }
        
        insights.put("Total Students Analyzed", String.valueOf(history.getTotalPredictions()));
        insights.put("Average Attendance", String.format("%.1f%%", history.getAverageAttendance()));
        insights.put("Average Marks", String.format("%.1f%%", history.getAverageMarks()));
        insights.put("High-Risk Percentage", String.format("%.1f%%", history.getHighRiskPercentage()));
        
        Map<String, Integer> gradeDistribution = history.getGradeDistribution();
        String mostCommon = gradeDistribution.entrySet().stream()
            .max(Comparator.comparingInt(Map.Entry::getValue))
            .map(e -> e.getKey() + " (" + e.getValue() + ")")
            .orElse("N/A");
        insights.put("Most Common Grade", mostCommon);
        
        long passCount = recent.stream()
            .filter(r -> "PASS".equals(r.passOrFail))
            .count();
        insights.put("Pass Rate (Recent)", String.format("%.1f%%", (passCount * 100.0) / recent.size()));
        
        return insights;
    }
}
