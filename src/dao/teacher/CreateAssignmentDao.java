package dao.teacher;

import config.DBConnection;
import java.sql.*;

public class CreateAssignmentDao {
    public int getSubjectId(String subjectName, int teacherId) throws SQLException {
        String query = "SELECT subject_id FROM subjects WHERE name = ? AND teacher_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, subjectName);
            stmt.setInt(2, teacherId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("subject_id");
            }
        }
        return -1;
    }

    public boolean createAssignment(int subjectId, String title, String description, Date uploadDate, Date dueDate, int totalMarks) {
        String insertQuery = "INSERT INTO assignments (subject_id, title, description, upload_date, due_date, total_marks) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setInt(1, subjectId);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setDate(4, uploadDate);
            stmt.setDate(5, dueDate);
            stmt.setInt(6, totalMarks);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
