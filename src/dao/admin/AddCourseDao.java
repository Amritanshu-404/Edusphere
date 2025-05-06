package dao.admin;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddCourseDao {

    public static List<String> getAllTeachers() throws SQLException {
        List<String> teachers = new ArrayList<>();
        String query = "SELECT teacher_id, name FROM teachers";

        try (Connection conn = DBConnection.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                teachers.add(rs.getInt("teacher_id") + " - " + rs.getString("name"));
            }
        }
        return teachers;
    }

    public static List<String> getAllStudents() throws SQLException {
        List<String> students = new ArrayList<>();
        String query = "SELECT student_id, name FROM students";

        try (Connection conn = DBConnection.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                students.add(rs.getInt("student_id") + " - " + rs.getString("name"));
            }
        }
        return students;
    }

    public static boolean createCourse(String name, int semester, String timing, int teacherId, List<Integer> studentIds) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            String insertCourse = "INSERT INTO subjects (name, semester, timing, teacher_id) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertCourse, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setInt(2, semester);
            ps.setString(3, timing);
            ps.setInt(4, teacherId);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int subjectId = keys.getInt(1);

                String enrollSQL = "INSERT INTO enrollments (subject_id, student_id) VALUES (?, ?)";
                PreparedStatement enrollStmt = conn.prepareStatement(enrollSQL);
                for (int studentId : studentIds) {
                    enrollStmt.setInt(1, subjectId);
                    enrollStmt.setInt(2, studentId);
                    enrollStmt.addBatch();
                }
                enrollStmt.executeBatch();
                return true;
            }
        }
        return false;
    }
}
