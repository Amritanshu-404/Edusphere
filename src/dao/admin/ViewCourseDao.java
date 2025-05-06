package dao.admin;

import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewCourseDao {

    public List<Object[]> fetchCourses() {
        List<Object[]> courseList = new ArrayList<>();

        String sql = """
            SELECT s.subject_id, s.name AS subject_name, s.semester, s.timing,
                   t.name AS teacher_name,
                   COUNT(e.student_id) AS students_enrolled
            FROM subjects s
            LEFT JOIN teachers t ON s.teacher_id = t.teacher_id
            LEFT JOIN enrollments e ON s.subject_id = e.subject_id
            GROUP BY s.subject_id, s.name, s.semester, s.timing, t.name
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int subjectId = rs.getInt("subject_id");
                String subjectName = rs.getString("subject_name");
                int semester = rs.getInt("semester");
                String timing = rs.getString("timing");
                String teacherName = rs.getString("teacher_name");
                int studentCount = rs.getInt("students_enrolled");

                courseList.add(new Object[]{
                        subjectId,
                        subjectName,
                        semester,
                        timing,
                        (teacherName != null ? teacherName : "Not Assigned"),
                        studentCount
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courseList;
    }
}
