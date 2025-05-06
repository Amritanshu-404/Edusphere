package dao.student;

import config.DBConnection;

import java.io.File;
import java.sql.*;
import java.util.*;

public class ViewAssignmentDao {

    public static Map<String, Integer> getSubjectsForStudent(int studentId) {
        Map<String, Integer> subjectMap = new HashMap<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT DISTINCT s.subject_id, s.name FROM subjects s "
                    + "JOIN enrollments e ON s.subject_id = e.subject_id WHERE e.student_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subjectMap.put(rs.getString("name"), rs.getInt("subject_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjectMap;
    }

    public static List<Object[]> getAssignmentsBySubject(int subjectId) {
        List<Object[]> assignments = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT title, description, upload_date, due_date, total_marks FROM assignments WHERE subject_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                assignments.add(new Object[]{
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("upload_date"),
                        rs.getDate("due_date"),
                        rs.getInt("total_marks"),
                        "Choose File",
                        "Submit"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assignments;
    }

    public static boolean submitAssignment(int studentId, int subjectId, String assignmentTitle, File file) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT assignment_id FROM assignments WHERE subject_id = ? AND title = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, subjectId);
            stmt.setString(2, assignmentTitle);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int assignmentId = rs.getInt("assignment_id");
                String insert = "INSERT INTO submissions (assignment_id, student_id, subject_id, file_path, submission_date) VALUES (?, ?, ?, ?, NOW())";
                PreparedStatement insertStmt = conn.prepareStatement(insert);
                insertStmt.setInt(1, assignmentId);
                insertStmt.setInt(2, studentId);
                insertStmt.setInt(3, subjectId);
                insertStmt.setString(4, file.getAbsolutePath());
                insertStmt.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
