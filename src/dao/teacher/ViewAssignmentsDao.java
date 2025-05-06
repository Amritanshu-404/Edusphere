package dao;

import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewAssignmentsDao {
    private int teacherId;

    public ViewAssignmentsDao(int teacherId) {
        this.teacherId = teacherId;
    }

    public List<Object[]> getAssignments() {
        List<Object[]> assignments = new ArrayList<>();
        String query = """
                SELECT a.title, s.name AS subject_name, s.semester, a.due_date, a.total_marks
                FROM assignments a
                JOIN subjects s ON a.subject_id = s.subject_id
                WHERE s.teacher_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String subject = rs.getString("subject_name");
                int semester = rs.getInt("semester");
                Date dueDate = rs.getDate("due_date");
                int fullMarks = rs.getInt("total_marks");

                assignments.add(new Object[]{title, subject, semester, dueDate, fullMarks});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignments;
    }
}
