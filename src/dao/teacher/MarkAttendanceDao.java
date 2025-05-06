package dao.teacher;

import config.DBConnection;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MarkAttendanceDao {

    public Map<String, Integer> loadSubjects(int teacherId) throws SQLException {
        Map<String, Integer> subjectMap = new HashMap<>();
        String query = "SELECT subject_id, name FROM subjects WHERE teacher_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                subjectMap.put(rs.getString("name"), rs.getInt("subject_id"));
            }
        }
        return subjectMap;
    }

    public ResultSet loadEnrolledStudents(String subjectName) throws SQLException {
        String query = "SELECT s.student_id, s.name, s.roll_number, s.semester " +
                "FROM students s " +
                "JOIN enrollments e ON s.student_id = e.student_id " +
                "JOIN subjects sub ON e.subject_id = sub.subject_id " +
                "WHERE sub.name = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, subjectName);
        return stmt.executeQuery();
    }

    public void submitAttendance(int subjectId, DefaultTableModel model) throws SQLException {
        String insertQuery = "INSERT INTO attendance (subject_id, student_id, attendance_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            conn.setAutoCommit(false);

            LocalDate currentDate = LocalDate.now();

            for (int i = 0; i < model.getRowCount(); i++) {
                boolean isPresent = (Boolean) model.getValueAt(i, 0);
                String studentId = model.getValueAt(i, 1).toString();

                insertStmt.setInt(1, subjectId);
                insertStmt.setString(2, studentId);
                insertStmt.setDate(3, Date.valueOf(currentDate));
                insertStmt.setString(4, isPresent ? "Present" : "Absent");

                insertStmt.addBatch();
            }

            insertStmt.executeBatch();
            conn.commit();
        }
    }

    public int getSubjectId(String subjectName) throws SQLException {
        String getSubjectIdQuery = "SELECT subject_id FROM subjects WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement subjectStmt = conn.prepareStatement(getSubjectIdQuery)) {
            subjectStmt.setString(1, subjectName);
            ResultSet subjectRs = subjectStmt.executeQuery();

            if (subjectRs.next()) {
                return subjectRs.getInt("subject_id");
            } else {
                throw new SQLException("Subject ID not found.");
            }
        }
    }
}
