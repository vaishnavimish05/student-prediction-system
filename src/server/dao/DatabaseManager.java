package server.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseManager {

    private static final String DEFAULT_URL  = "jdbc:mariadb://localhost:3306/student_prediction_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "";

    static {
        try {
            // MariaDB driver also handles MySQL URLs
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] JDBC driver not found: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        String url  = System.getProperty("db.url",      DEFAULT_URL);
        String user = System.getProperty("db.user",     DEFAULT_USER);
        String pass = System.getProperty("db.password", DEFAULT_PASS);
        return DriverManager.getConnection(url, user, pass);
    }

    public static void initSchema() {
        String sql = """
            CREATE TABLE IF NOT EXISTS students (
                id                          BIGINT AUTO_INCREMENT PRIMARY KEY,
                name                        VARCHAR(150) NOT NULL,
                email                       VARCHAR(150) NOT NULL UNIQUE,
                math_marks                  DOUBLE NOT NULL DEFAULT 0,
                science_marks               DOUBLE NOT NULL DEFAULT 0,
                english_marks               DOUBLE NOT NULL DEFAULT 0,
                avg_marks                   DOUBLE NOT NULL DEFAULT 0,
                attendance_percent          DOUBLE NOT NULL DEFAULT 0,
                assignments_submitted       INT    NOT NULL DEFAULT 0,
                study_hours_per_day         DOUBLE NOT NULL DEFAULT 0,
                extracurricular_activities  INT    NOT NULL DEFAULT 0,
                previous_failures           INT    NOT NULL DEFAULT 0,
                created_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        try (Connection c = getConnection();
             var st = c.createStatement()) {
            st.execute(sql);
            System.out.println("[DB] Schema ready.");
        } catch (SQLException e) {
            System.err.println("[DB] Schema init failed: " + e.getMessage());
            System.err.println("[DB] App will run without DB (in-memory mode).");
        }
    }
}
