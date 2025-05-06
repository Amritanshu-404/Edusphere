package dao.admin;

import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowDelStudentDao {

    public List<List<String>> fetchStudents() {
        List<List<String>> students = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String query = """
                SELECT s.student_id, s.name, s.roll_number, s.semester, s.email, u.gender
                FROM students s JOIN users u ON s.student_id = u.user_id
            """;
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(rs.getInt("student_id")));
                row.add(rs.getString("name"));                   
                row.add(rs.getString("roll_number"));             
                row.add(String.valueOf(rs.getInt("semester")));  
                row.add(rs.getString("email"));                   
                row.add(rs.getString("gender"));               
                students.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean deleteStudent(int studentId) {
        try (Connection conn = DBConnection.getConnection()) {
            String[] queries = {
                "DELETE FROM enrollments WHERE student_id = ?",
                "DELETE FROM submissions WHERE student_id = ?",
                "DELETE FROM attendance WHERE student_id = ?",
                "DELETE FROM students WHERE student_id = ?",
                "DELETE FROM users WHERE user_id = ?"
            };

            for (String query : queries) {
                try (PreparedStatement pst = conn.prepareStatement(query)) {
                    pst.setInt(1, studentId);
                    pst.executeUpdate();
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
