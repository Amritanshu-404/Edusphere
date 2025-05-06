package dao.admin;

import config.DBConnection;
import java.sql.*;

public class AddTeacherDao {

    public static boolean addTeacher(String name, String designation, String email, String joinDate, String gender, String password) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            // Step 1: Insert into users table
            String userSql = "INSERT INTO users (password, role, gender) VALUES (?, 'Teacher', ?)";
            PreparedStatement userStmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, password);
            userStmt.setString(2, gender);
            userStmt.executeUpdate();

            ResultSet rs = userStmt.getGeneratedKeys();
            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt(1);
            }

            if (userId == -1) throw new SQLException("Failed to retrieve user ID.");

            // Step 2: Insert into teachers table
            String teacherSql = "INSERT INTO teachers (teacher_id, name, designation, email, join_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement teacherStmt = conn.prepareStatement(teacherSql);
            teacherStmt.setInt(1, userId);
            teacherStmt.setString(2, name);
            teacherStmt.setString(3, designation);
            teacherStmt.setString(4, email);
            teacherStmt.setDate(5, Date.valueOf(joinDate));
            teacherStmt.executeUpdate();

            return true;
        }
    }
}
