package server.ai;

import server.model.Student;
import shared.PredictionResult;

/**
 * ENHANCED FEEDBACK GENERATOR — More nuanced, context-aware feedback
 * Uses sophisticated templates and context analysis for natural language generation
 */
public class EnhancedFeedbackGenerator {
    
    /**
     * Generate deeply personalized feedback with context awareness
     */
    public static String generateContextualFeedback(Student s, PredictionResult r) {
        double avg = s.getAvgMarks();
        double att = s.getAttendancePercent();
        int fail = s.getPreviousFailures();
        int asn = s.getAssignmentsSubmitted();
        
        StringBuilder feedback = new StringBuilder();
        
        // Opening with encouragement or concern
        String opening = generateOpening(s, r);
        feedback.append(opening).append("\n\n");
        
        // Core analysis
        feedback.append(generateCoreAnalysis(s, r)).append("\n\n");
        
        // Action items
        feedback.append(generateActionItems(s, r)).append("\n\n");
        
        // Closing motivation
        feedback.append(generateClosing(s, r));
        
        return feedback.toString();
    }
    
    private static String generateOpening(Student s, PredictionResult r) {
        double avg = s.getAvgMarks();
        double att = s.getAttendancePercent();
        int fail = s.getPreviousFailures();
        
        if (fail >= 2 && avg < 50) {
            return "🚨 URGENT ATTENTION REQUIRED\n\nYour academic profile shows concerning indicators that need immediate intervention. " +
                   "However, this is recoverable with focused effort and the right support system.";
        } else if (avg >= 80 && att >= 80) {
            return "🌟 OUTSTANDING PERFORMANCE\n\nYou're demonstrating exceptional commitment to your studies. " +
                   "Your consistent performance and strong attendance are building a solid foundation for success.";
        } else if (avg >= 65 && att >= 75) {
            return "✅ GOOD PROGRESS\n\nYou're on a positive trajectory with solid academic performance. " +
                   "Small improvements can yield significant results in your overall standing.";
        } else if (avg < 50 || att < 60) {
            return "⚠️ TIME FOR ACTION\n\nYour current metrics indicate a need for immediate changes. " +
                   "The good news: targeted interventions can reverse this trend quickly.";
        } else {
            return "📊 PERFORMANCE REVIEW\n\nLet's analyze your academic standing and identify opportunities for growth.";
        }
    }
    
    private static String generateCoreAnalysis(Student s, PredictionResult r) {
        double avg = (s.getMathMarks() + s.getScienceMarks() + s.getEnglishMarks()) / 3.0;
        double math = s.getMathMarks();
        double sci = s.getScienceMarks();
        double eng = s.getEnglishMarks();
        double att = s.getAttendancePercent();
        
        StringBuilder analysis = new StringBuilder("📈 DETAILED ANALYSIS:\n\n");
        
        // Marks analysis
        analysis.append("Marks Breakdown:\n");
        if (math >= 80) {
            analysis.append("  📐 Mathematics (").append(String.format("%.0f%%", math))
                .append(") — Your logical reasoning is strong. Leverage this in complex topics.\n");
        } else {
            analysis.append("  📐 Mathematics (").append(String.format("%.0f%%", math))
                .append(") — Focus on practice problems and concept clarity.\n");
        }
        
        if (sci >= 80) {
            analysis.append("  🔬 Science (").append(String.format("%.0f%%", sci))
                .append(") — Excellent analytical skills. Help peers to deepen your own understanding.\n");
        } else {
            analysis.append("  🔬 Science (").append(String.format("%.0f%%", sci))
                .append(") — Use diagrams and real-world applications to improve comprehension.\n");
        }
        
        if (eng >= 80) {
            analysis.append("  📖 English (").append(String.format("%.0f%%", eng))
                .append(") — Strong communication skills. Critical for placement interviews.\n");
        } else {
            analysis.append("  📖 English (").append(String.format("%.0f%%", eng))
                .append(") — Regular reading and writing practice essential for improvement.\n");
        }
        
        // Attendance analysis
        analysis.append("\nAttendance Impact:\n");
        if (att >= 85) {
            analysis.append("  ✅ Your ").append(String.format("%.0f%%", att))
                .append(" attendance is excellent and directly boosts your eligibility.\n");
        } else if (att >= 75) {
            analysis.append("  ⚠️ Your ").append(String.format("%.0f%%", att))
                .append(" attendance is acceptable but can be improved. Aim for 85%+.\n");
        } else {
            analysis.append("  🔴 Your ").append(String.format("%.0f%%", att))
                .append(" attendance is a critical concern. Attend EVERY remaining class.\n");
        }
        
        return analysis.toString();
    }
    
    private static String generateActionItems(Student s, PredictionResult r) {
        StringBuilder actions = new StringBuilder("🎯 IMMEDIATE ACTION ITEMS:\n\n");
        
        double avg = s.getAvgMarks();
        double att = s.getAttendancePercent();
        int asn = s.getAssignmentsSubmitted();
        
        // Priority 1: Attendance
        if (att < 75) {
            actions.append("1️⃣ PRIORITY: Attendance Recovery\n")
                .append("   • Set phone alarms for every class\n")
                .append("   • Inform professors TODAY of your commitment to improve\n")
                .append("   • Target: Reach 80% in next 2 weeks\n\n");
        }
        
        // Priority 2: Assignments
        if (asn < 7) {
            actions.append("1️⃣ PRIORITY: Assignment Completion\n")
                .append("   • Submit ALL pending assignments within 3 days\n")
                .append("   • Quality matters: spend time on each one\n")
                .append("   • Ask for extensions if needed — professors appreciate honesty\n\n");
        }
        
        // Priority 3: Weak subject
        double weakest = Math.min(s.getMathMarks(), Math.min(s.getScienceMarks(), s.getEnglishMarks()));
        if (weakest < 60) {
            actions.append("2️⃣ Academic Focus:\n")
                .append("   • Dedicate 1 hour daily to your weakest subject\n")
                .append("   • Work through EVERY past paper question\n")
                .append("   • Form a 2-person study group for peer learning\n\n");
        }
        
        // General improvement
        actions.append("3️⃣ Overall Strategy:\n");
        if (avg < 50) {
            actions.append("   • Seek immediate tutoring support\n");
        }
        actions.append("   • Create a weekly study schedule and stick to it\n")
            .append("   • Review notes within 24 hours of each class\n")
            .append("   • Take practice tests every Sunday\n");
        
        return actions.toString();
    }
    
    private static String generateClosing(Student s, PredictionResult r) {
        int fail = s.getPreviousFailures();
        double avg = s.getAvgMarks();
        
        if (fail >= 2 && avg < 50) {
            return "🤝 SUPPORT IS AVAILABLE\n\nDon't feel alone — your institution has counseling, tutoring, and academic support services. " +
                   "Use them. Most successful students have been in your position.\n\n" +
                   "Your next 4 weeks will determine your semester outcome. Act now. 💪";
        } else if (avg >= 80) {
            return "🚀 YOU'RE ON TRACK\n\nMaintain this momentum. Your consistent performance opens doors to " +
                   "internships, leadership roles, and strong placement opportunities.\n\n" +
                   "Keep pushing — excellence is a habit, not an accident. 🏆";
        } else {
            return "🎓 GROWTH OPPORTUNITY\n\nEvery student faces challenges. What matters is how you respond. " +
                   "The strategies outlined above are proven to work when executed consistently.\n\n" +
                   "You have the power to change your trajectory starting today. Let's do this! 💯";
        }
    }
}
