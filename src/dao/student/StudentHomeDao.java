package dao.student;

import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentHomeDao {

    public static List<Object[]> getTimetable(int studentId) {
        List<Object[]> timetableList = new ArrayList<>();

        String query = """
            SELECT s.name AS subject_name, s.timing, t.name AS teacher_name
            FROM subjects s
            JOIN enrollments e ON s.subject_id = e.subject_id
            JOIN teachers t ON s.teacher_id = t.teacher_id
            WHERE e.student_id = ?
            ORDER BY s.timing;
        """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                timetableList.add(new Object[]{
                    rs.getString("subject_name"),
                    rs.getString("timing"),
                    rs.getString("teacher_name")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return timetableList;
    }

    public static List<Object[]> getAttendance(int studentId) {
        List<Object[]> attendanceList = new ArrayList<>();

        String query = """
            SELECT s.name AS subject_name,
                   COUNT(a.attendance_id) AS total_classes,
                   SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) AS present_count
            FROM subjects s
            JOIN enrollments e ON s.subject_id = e.subject_id
            LEFT JOIN attendance a ON s.subject_id = a.subject_id AND a.student_id = ?
            WHERE e.student_id = ?
            GROUP BY s.name;
        """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, studentId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String subject = rs.getString("subject_name");
                int total = rs.getInt("total_classes");
                int present = rs.getInt("present_count");
                double percent = total == 0 ? 0.0 : (present * 100.0 / total);

                attendanceList.add(new Object[]{
                    subject,
                    total,
                    present,
                    String.format("%.2f%%", percent)
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendanceList;
    }

    public static List<Object[]> getAssignments(int studentId) {
        List<Object[]> assignmentList = new ArrayList<>();

        String query = """
            SELECT a.title AS assignment_name, s.name AS subject_name, a.due_date
            FROM assignments a
            JOIN subjects s ON a.subject_id = s.subject_id
            JOIN enrollments e ON e.subject_id = s.subject_id
            WHERE e.student_id = ?
            ORDER BY a.due_date;
        """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String assignmentName = rs.getString("assignment_name");
                String subjectName = rs.getString("subject_name");
                Date dueDate = rs.getDate("due_date");

                assignmentList.add(new Object[]{
                    assignmentName,
                    subjectName,
                    dueDate != null ? dueDate.toString() : "N/A"
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignmentList;
    }
}
