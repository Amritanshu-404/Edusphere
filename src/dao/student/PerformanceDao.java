package dao.student;

import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerformanceDao {

    public static List<Object[]> getPerformanceData(int studentId) {
        List<Object[]> performanceList = new ArrayList<>();

        String query = """
            SELECT a.title, s.name AS subject_name, a.total_marks,
                   COALESCE(sub.marks_obtained, 0) AS marks_obtained,
                   CASE
                       WHEN sub.submission_id IS NOT NULL THEN 'Submitted'
                       ELSE 'Not Submitted'
                   END AS status
            FROM assignments a
            JOIN subjects s ON a.subject_id = s.subject_id
            LEFT JOIN submissions sub ON a.assignment_id = sub.assignment_id AND sub.student_id = ?
            JOIN enrollments e ON e.subject_id = s.subject_id
            WHERE e.student_id = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, studentId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                String subject = rs.getString("subject_name");
                int fullMarks = rs.getInt("total_marks");
                int obtainedMarks = rs.getInt("marks_obtained");
                String status = rs.getString("status");

                performanceList.add(new Object[]{title, subject, obtainedMarks, fullMarks, status});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return performanceList;
    }
}
