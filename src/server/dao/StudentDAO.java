package server.dao;
import server.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class StudentDAO {

    public Student save(Student s) {
        String sql = """
            INSERT INTO students
              (name, email, math_marks, science_marks, english_marks, avg_marks,
               attendance_percent, assignments_submitted, study_hours_per_day,
               extracurricular_activities, previous_failures)
            VALUES (?,?,?,?,?,?,?,?,?,?,?)
            ON DUPLICATE KEY UPDATE
              math_marks=VALUES(math_marks), science_marks=VALUES(science_marks),
              english_marks=VALUES(english_marks), avg_marks=VALUES(avg_marks),
              attendance_percent=VALUES(attendance_percent),
              assignments_submitted=VALUES(assignments_submitted),
              study_hours_per_day=VALUES(study_hours_per_day),
              extracurricular_activities=VALUES(extracurricular_activities),
              previous_failures=VALUES(previous_failures)
            """;
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1,  s.getName());
            ps.setString(2,  s.getEmail());
            ps.setDouble(3,  s.getMathMarks());
            ps.setDouble(4,  s.getScienceMarks());
            ps.setDouble(5,  s.getEnglishMarks());
            ps.setDouble(6,  s.getAvgMarks());
            ps.setDouble(7,  s.getAttendancePercent());
            ps.setInt   (8,  s.getAssignmentsSubmitted());
            ps.setDouble(9,  s.getStudyHoursPerDay());
            ps.setInt   (10, s.getExtracurricularActivities());
            ps.setInt   (11, s.getPreviousFailures());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) s.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Save failed (DB may be offline): " + e.getMessage());
            s.setId((long)(Math.random() * 100000)); // temp ID for no-DB mode
        }
        return s;
    }
    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY created_at DESC";
        try (Connection c = DatabaseManager.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getLong("id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setMathMarks(rs.getDouble("math_marks"));
                s.setScienceMarks(rs.getDouble("science_marks"));
                s.setEnglishMarks(rs.getDouble("english_marks"));
                s.setAvgMarks(rs.getDouble("avg_marks"));
                s.setAttendancePercent(rs.getDouble("attendance_percent"));
                s.setAssignmentsSubmitted(rs.getInt("assignments_submitted"));
                s.setStudyHoursPerDay(rs.getDouble("study_hours_per_day"));
                s.setExtracurricularActivities(rs.getInt("extracurricular_activities"));
                s.setPreviousFailures(rs.getInt("previous_failures"));
                list.add(s);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] FindAll failed: " + e.getMessage());
        }
        return list;
    }
}
