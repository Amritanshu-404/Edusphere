package dao;

import java.sql.*;
import config.DBConnection;

public class LoginDao {

    // Authenticate Admin by user_id (int) and password
    public boolean authenticateAdmin(int userId, String password) {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM users WHERE user_id = ? AND role = 'Admin' AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Authenticate Student or Teacher by name and password (linked by user_id)
    public boolean authenticateUser(String userType, String name, String password) {
        try {
            Connection conn = DBConnection.getConnection();

            String query = "";
            if (userType.equalsIgnoreCase("Student")) {
                query = "SELECT u.* FROM users u " +
                        "JOIN students s ON u.user_id = s.student_id " +
                        "WHERE s.name = ? AND u.role = 'Student' AND u.password = ?";
            } else if (userType.equalsIgnoreCase("Teacher")) {
                query = "SELECT u.* FROM users u " +
                        "JOIN teachers t ON u.user_id = t.teacher_id " +
                        "WHERE t.name = ? AND u.role = 'Teacher' AND u.password = ?";
            } else {
                return false;
            }

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fetch student_id based on the student's name (username)
    // In LoginDao.java
// In dao/LoginDao.java
public int getStudentIdByUsername(String username) {
    int studentId = -1;
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT student_id FROM students WHERE name = ?")) {

        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            studentId = rs.getInt("student_id");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return studentId;
}



    // Fetch teacher_id based on the teacher's name (username)
    public int getTeacherIdByUsername(String username) {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT teacher_id FROM teachers WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("teacher_id"); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  
    }
}
