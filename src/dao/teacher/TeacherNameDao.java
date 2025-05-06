package dao.teacher;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherNameDao {

    public static String getTeacherName(int teacherId) {
        String teacherName = "Teacher";
        String query = "SELECT name FROM teachers WHERE teacher_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                teacherName = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return teacherName;
    }
}
