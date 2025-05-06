package dao.admin;

import config.DBConnection;

import java.sql.*;

public class AddStudentDao {

    public static boolean addStudent(String name, String roll, String email, int semester, String gender, String admissionDate, String password) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            // Insert into users table
            String userSql = "INSERT INTO users (password, role, gender) VALUES (?, 'Student', ?)";
            PreparedStatement userStmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, password);
            userStmt.setString(2, gender);
            userStmt.executeUpdate();

            ResultSet rs = userStmt.getGeneratedKeys();
            int userId = -1;
            if (rs.next()) userId = rs.getInt(1);
            if (userId == -1) throw new SQLException("Failed to retrieve generated user ID.");

            // Insert into students table
            String studentSql = "INSERT INTO students (student_id, name, roll_number, semester, email, admission_date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement studentStmt = conn.prepareStatement(studentSql);
            studentStmt.setInt(1, userId);
            studentStmt.setString(2, name);
            studentStmt.setString(3, roll);
            studentStmt.setInt(4, semester);
            studentStmt.setString(5, email);
            studentStmt.setDate(6, Date.valueOf(admissionDate));
            studentStmt.executeUpdate();

            return true;
        }
    }
}
