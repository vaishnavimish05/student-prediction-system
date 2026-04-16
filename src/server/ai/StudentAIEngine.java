package server.ai;

import server.model.Student;
import shared.PredictionResult;

/**
 * AI ENGINE — Rule-based intelligence. No external API needed.
 *
 * Generates personalized, context-aware feedback using:
 *   1. Pattern matching on student profile clusters
 *   2. Weighted scoring and threshold analysis
 *   3. Template-driven natural language generation
 *   4. Multi-factor risk assessment
 *
 * The output reads like intelligent advice because it's built from
 * the actual student data — not generic text.
 */
public class StudentAIEngine {

    // ── Profile clusters ─────────────────────────────────────────────────
    enum ProfileType {
        HIGH_ACHIEVER,      // avg >= 80, attendance >= 80
        CONSISTENT,         // avg >= 65, attendance >= 75
        UNDERPERFORMER,     // avg >= 40, attendance < 75
        STRUGGLING,         // avg < 60, attendance < 70
        AT_RISK,            // avg < 40 OR attendance < 60
        POTENTIAL_DROPOUT   // multiple failure indicators
    }

    public void generateFeedback(Student s, PredictionResult r) {
        ProfileType profile = classifyProfile(s, r);
        r.setPersonalizedFeedback(EnhancedFeedbackGenerator.generateContextualFeedback(s, r));
        r.setImprovementPlan(buildImprovementPlan(s, r, profile));
        r.setStudyStrategy(buildStudyStrategy(s, r, profile));
    }

    // ── Profile classification ────────────────────────────────────────────
    private ProfileType classifyProfile(Student s, PredictionResult r) {
        double avg  = s.getAvgMarks();
        double att  = s.getAttendancePercent();
        int    fail = s.getPreviousFailures();
        int    asn  = s.getAssignmentsSubmitted();

        if (fail >= 2 && avg < 50 && att < 65)         return ProfileType.POTENTIAL_DROPOUT;
        if (avg < 40  || att < 60)                      return ProfileType.AT_RISK;
        if (avg >= 80 && att >= 80 && asn >= 8)         return ProfileType.HIGH_ACHIEVER;
        if (avg >= 65 && att >= 75)                     return ProfileType.CONSISTENT;
        if (avg >= 40 && att < 75)                      return ProfileType.UNDERPERFORMER;
        return ProfileType.STRUGGLING;
    }

    // ── Personalised Feedback ─────────────────────────────────────────────
    private String buildFeedback(Student s, PredictionResult r, ProfileType p) {
        String name     = s.getName().split(" ")[0]; // first name only
        double avg      = s.getAvgMarks();
        double att      = s.getAttendancePercent();
        String grade    = r.getGrade();
        String risk     = r.getDropoutRisk();
        int    placement= (int)(r.getPlacementProbability() * 100);

        return switch (p) {
            case HIGH_ACHIEVER -> String.format(
                "%s, you're performing at an excellent level! Your average of %.1f%% has earned you " +
                "a predicted grade of %s, and your %.1f%% attendance shows real dedication. " +
                "Your placement probability stands at %d%% — keep pushing for top scores and consider " +
                "leadership roles in projects to further strengthen your profile. You're on track for outstanding results.",
                name, avg, grade, att, placement);

            case CONSISTENT -> String.format(
                "Good work, %s! With an average of %.1f%% and %.1f%% attendance, you're maintaining " +
                "a solid academic record (Grade: %s). Your placement prospects look positive at %d%%. " +
                "The next step is to push your scores above 75%% — focused revision on your weakest subject " +
                "could make a significant difference this semester.",
                name, avg, grade, att, placement);

            case UNDERPERFORMER -> String.format(
                "%s, your academic scores are decent at %.1f%% (Grade: %s), but your attendance of %.1f%% " +
                "is pulling down your overall performance. Attendance directly impacts exam eligibility " +
                "and your placement probability is currently %d%%. Attending consistently — even just " +
                "improving to 75%% — would noticeably improve your predicted outcomes.",
                name, avg, grade, att, placement);

            case STRUGGLING -> String.format(
                "%s, your current average of %.1f%% and attendance of %.1f%% indicate you're facing " +
                "some challenges — that's okay, and it's fixable with the right support. " +
                "Your dropout risk is flagged as %s, which means now is the best time to act. " +
                "Connect with your faculty advisor this week, and focus on submitting pending assignments " +
                "to quickly improve your standing.",
                name, avg, att, risk);

            case AT_RISK -> String.format(
                "%s, I want to be direct with you: your academic indicators need immediate attention. " +
                "An average of %.1f%% and %.1f%% attendance put you at HIGH risk of not meeting " +
                "semester requirements. This is not the end — many students have recovered from " +
                "similar situations. Speak to your professor today, request a remedial plan, " +
                "and focus on attending every remaining class.",
                name, avg, att);

            case POTENTIAL_DROPOUT -> String.format(
                "%s, your academic profile is showing serious warning signs — %d previous failure(s), " +
                "%.1f%% average marks, and %.1f%% attendance. The system has flagged a HIGH dropout risk. " +
                "Please seek help immediately — your institution's student support services exist for " +
                "exactly this situation. With the right intervention plan, you can turn this around.",
                name, s.getPreviousFailures(), avg, att);
        };
    }

    // ── Improvement Plan ─────────────────────────────────────────────────
    private String buildImprovementPlan(Student s, PredictionResult r, ProfileType p) {
        StringBuilder plan = new StringBuilder();
        double avg  = s.getAvgMarks();
        double att  = s.getAttendancePercent();
        int    asn  = s.getAssignmentsSubmitted();
        double hrs  = s.getStudyHoursPerDay();

        // Step 1 — attendance
        if (att < 75) {
            plan.append("1. ATTENDANCE: Your attendance is ").append(String.format("%.0f%%", att))
                .append(" — you need 75%% minimum. Attend every class this week without exception. Set phone alarms for each lecture.\n");
        } else {
            plan.append("1. ATTENDANCE: Maintain your ").append(String.format("%.0f%%", att))
                .append("%% attendance — it's a key eligibility requirement. Aim for 90%%.\n");
        }

        // Step 2 — assignments
        if (asn < 7) {
            int missing = 10 - asn;
            plan.append("2. ASSIGNMENTS: You've submitted only ").append(asn).append("/10 assignments. ")
                .append("Submit ").append(missing).append(" pending assignment(s) within 3 days — late submissions still count.\n");
        } else {
            plan.append("2. ASSIGNMENTS: Good job on submitting ").append(asn)
                .append("/10 assignments. Keep this consistency for the rest of the semester.\n");
        }

        // Step 3 — study hours
        if (hrs < 2.0) {
            plan.append("3. STUDY TIME: You're studying only ").append(String.format("%.1f", hrs))
                .append(" hrs/day. Increase to at least 3 hours with a fixed daily schedule — consistency beats marathon sessions.\n");
        } else if (hrs < 4.0) {
            plan.append("3. STUDY TIME: Your ").append(String.format("%.1f", hrs))
                .append(" hrs/day is a reasonable base. Add 30 minutes of focused past-paper practice each evening.\n");
        } else {
            plan.append("3. STUDY TIME: You're investing ").append(String.format("%.1f", hrs))
                .append(" hrs/day — make sure quality matches quantity. Use structured notes and self-testing, not passive reading.\n");
        }

        // Step 4 — weakest subject
        double weakest = Math.min(s.getMathMarks(), Math.min(s.getScienceMarks(), s.getEnglishMarks()));
        String weakSubj = (weakest == s.getMathMarks()) ? "Mathematics"
                        : (weakest == s.getScienceMarks()) ? "Science" : "English";
        plan.append("4. FOCUS SUBJECT: Your weakest subject is ").append(weakSubj)
            .append(String.format(" (%.0f%%)", weakest))
            .append(". Dedicate an extra 30 minutes daily to this subject and seek help from your teacher.\n");

        // Step 5 — overall goal
        if (avg < 60) {
            plan.append("5. TARGET: Aim to reach 60%% average by end of month — this gets you a passing grade. ")
                .append("Take 1 practice test per week and review wrong answers carefully.");
        } else if (avg < 75) {
            plan.append("5. TARGET: Push your average from ").append(String.format("%.0f%%", avg))
                .append(" to 75%% — this upgrades your grade and placement probability significantly. ")
                .append("Join or form a study group for peer learning.");
        } else {
            plan.append("5. TARGET: Maintain excellence — aim for ").append(String.format("%.0f%%", Math.min(avg + 5, 98)))
                .append("%% by the final exam. Consider teaching peers: explaining concepts deepens your own understanding.");
        }

        return plan.toString();
    }

    // ── Study Strategy ───────────────────────────────────────────────────
    private String buildStudyStrategy(Student s, PredictionResult r, ProfileType p) {
        double hrs    = s.getStudyHoursPerDay();
        double avg    = s.getAvgMarks();
        double att    = s.getAttendancePercent();
        int    extra  = s.getExtracurricularActivities();
        String grade  = r.getGrade();

        double targetHours = Math.max(hrs + 1.0, 3.0);
        targetHours = Math.min(targetHours, 8.0);

        // Choose technique set based on performance level
        String techniques;
        if (avg >= 75) {
            techniques = """
                Techniques for High Performers:
                  • Feynman Technique — explain topics as if teaching a 10-year-old; exposes gaps instantly
                  • Interleaved Practice — mix subjects in a single session to improve retention
                  • Mock Exams under timed conditions — builds exam stamina and identifies weak spots""";
        } else if (avg >= 50) {
            techniques = """
                Techniques for Developing Students:
                  • Active Recall — close your notes and write down everything you remember after each topic
                  • Spaced Repetition — revisit topics on Day 1, Day 3, Day 7, then weekly
                  • Cornell Notes — divide page into notes/cues/summary for structured review""";
        } else {
            techniques = """
                Techniques for Foundation Building:
                  • Chunking — break complex topics into 10-15 minute micro-sessions
                  • Pomodoro Method — 25 min focused study, 5 min break (reduces overwhelm)
                  • Concept Maps — draw visual connections between topics before reading the textbook""";
        }

        // Build weekly timetable
        String timetable;
        if (att < 70) {
            timetable = """
                Weekly Structure (Attendance Recovery Mode):
                  Mon-Fri : Attend ALL classes → 1.5 hr revision same evening
                  Saturday: 3 hrs — weakest subject deep dive + assignment catch-up
                  Sunday  : 2 hrs — full week review + plan next week""";
        } else {
            timetable = String.format("""
                Weekly Structure (%.0f hrs/day target):
                  Morning (before class): 30 min — skim today's topic (prime your brain)
                  After class           : %.0f hr(s) — revise today's lecture notes while fresh
                  Evening               : %.0f hr(s) — practice problems / assignments
                  Saturday              : 4 hrs — full subject revision + past papers
                  Sunday                : 2 hrs — light review + weekly plan""",
                targetHours,
                Math.floor(targetHours * 0.4),
                Math.ceil(targetHours * 0.6));
        }

        String extraNote = (extra > 0)
            ? "\nExtracurricular Tip: You're involved in activities — that's great for placement! Ensure they don't eat into core study time. Schedule them on weekends only during exam prep."
            : "\nExtracurricular Note: Consider joining 1 club or activity — it boosts soft skills and improves your placement profile significantly.";

        // Subject-specific analysis
        String subjectAnalysis = buildSubjectAnalysis(s);
        
        // Wellness tips
        String wellnessTips = buildWellnessTips();
        
        // Recommended resources
        String resources = buildRecommendedResources(s, grade);

        return String.format(
            "Personalised Study Strategy — Current Grade: %s\n\n%s\n\n%s\n\n%s\n\n%s\n\n%s%s",
            grade, timetable, techniques, subjectAnalysis, resources, wellnessTips, extraNote);
    }

    // ── Subject-specific Analysis ────────────────────────────────────────
    private String buildSubjectAnalysis(Student s) {
        double math = s.getMathMarks();
        double sci = s.getScienceMarks();
        double eng = s.getEnglishMarks();
        
        StringBuilder sb = new StringBuilder("Subject Performance Analysis:\n");
        
        // Analyze each subject
        if (math >= 80) {
            sb.append("  📐 Mathematics (").append(String.format("%.0f%%", math)).append("): Excellent! You have strong logical thinking. Use this strength in advanced topics.\n");
        } else if (math >= 60) {
            sb.append("  📐 Mathematics (").append(String.format("%.0f%%", math)).append("): Solid foundation. Practice more word problems and conceptual questions regularly.\n");
        } else {
            sb.append("  📐 Mathematics (").append(String.format("%.0f%%", math)).append("): Needs attention. Start with fundamentals, use flowcharts for complex topics, and practice daily.\n");
        }
        
        if (sci >= 80) {
            sb.append("  🔬 Science (").append(String.format("%.0f%%", sci)).append("): Outstanding! Your analytical skills are strong. Help peers understand concepts.\n");
        } else if (sci >= 60) {
            sb.append("  🔬 Science (").append(String.format("%.0f%%", sci)).append("): Good progress. Create diagrams and visualizations to deepen understanding of complex topics.\n");
        } else {
            sb.append("  🔬 Science (").append(String.format("%.0f%%", sci)).append("): Development needed. Use videos and animations before reading textbooks, then practice experiments.\n");
        }
        
        if (eng >= 80) {
            sb.append("  📖 English (").append(String.format("%.0f%%", eng)).append("): Excellent command! You have strong communication skills — valuable for all careers.\n");
        } else if (eng >= 60) {
            sb.append("  📖 English (").append(String.format("%.0f%%", eng)).append("): Competent level. Read more literature, practice essay writing, and get feedback from teachers.\n");
        } else {
            sb.append("  📖 English (").append(String.format("%.0f%%", eng)).append("): Focus on basics. Read daily (news, blogs), write summaries, and practice grammar drills.\n");
        }
        
        return sb.toString();
    }

    // ── Recommended Resources ────────────────────────────────────────────
    private String buildRecommendedResources(Student s, String grade) {
        StringBuilder sb = new StringBuilder("Recommended Learning Resources:\n");
        
        double avg = s.getAvgMarks();
        if (avg >= 75) {
            sb.append("  🎓 For Advanced Learning:\n");
            sb.append("     • Khan Academy Premium — deep-dive into advanced topics\n");
            sb.append("     • Coursera/edX — enroll in specialized courses in your weak areas\n");
            sb.append("     • Research Papers — develop critical thinking by reading academic papers\n");
        } else if (avg >= 50) {
            sb.append("  📚 For Balanced Learning:\n");
            sb.append("     • Khan Academy (Free) — structured video lessons for all subjects\n");
            sb.append("     • YouTube Channels — subject-specific educational channels matching your pace\n");
            sb.append("     • Practice Websites — Brilliant.org, Chegg (problem-solving focused)\n");
        } else {
            sb.append("  🆘 For Foundation Building:\n");
            sb.append("     • Khan Academy Basics — start from fundamentals with practice exercises\n");
            sb.append("     • Tuition/Coaching — 1-on-1 guidance recommended for weak areas\n");
            sb.append("     • Study Groups — learn from peers at similar level, build confidence\n");
        }
        
        sb.append("  🤝 Peer Learning:\n");
        sb.append("     • Form a study group with 2-3 motivated classmates (meets 2x weekly)\n");
        sb.append("     • Teach a topic to someone — you'll learn it faster and deeper\n");
        sb.append("     • Schedule doubt-clearing sessions with classmates before exams\n");
        
        return sb.toString();
    }

    // ── Wellness & Motivation Tips ───────────────────────────────────────
    private String buildWellnessTips() {
        return """
            💪 Health & Wellness for Academic Success:
              • Sleep: Get 7-8 hours per night — improves memory retention and exam performance by 25%
              • Nutrition: Eat protein-rich breakfasts, avoid heavy meals before study sessions
              • Exercise: 20-30 min daily exercise boosts focus, mood, and stress management
              • Breaks: Every 45 min of study, take a 10-min break (walk, stretch, hydrate)
              • Environment: Study in a quiet, well-lit space free from phone distractions
              • Motivation: Celebrate small wins, track progress weekly on a visible chart""";
    }
}
