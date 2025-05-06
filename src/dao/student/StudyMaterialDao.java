package dao.student;

import config.DBConnection;

import java.sql.*;
import java.util.*;

public class StudyMaterialDao {

    public static Map<String, Integer> getSubjectsForStudent(int studentId) {
        Map<String, Integer> subjectMap = new LinkedHashMap<>();
        String query = "SELECT DISTINCT sub.subject_id, sub.name " +
                       "FROM subjects sub " +
                       "JOIN enrollments e ON sub.subject_id = e.subject_id " +
                       "WHERE e.student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                subjectMap.put(rs.getString("name"), rs.getInt("subject_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjectMap;
    }

    public static List<Object[]> getStudyMaterialsBySubject(int subjectId) {
        List<Object[]> materials = new ArrayList<>();
        String query = "SELECT title, description, upload_date, chapter_number, file_path " +
                       "FROM study_materials WHERE subject_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                materials.add(new Object[]{
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDate("upload_date").toString(),
                    rs.getInt("chapter_number"),
                    rs.getString("file_path")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return materials;
    }
}
