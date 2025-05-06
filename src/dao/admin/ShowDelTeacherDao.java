package dao.admin;

import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowDelTeacherDao {

    public List<List<String>> fetchTeachers() {
        List<List<String>> teacherList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT teacher_id, name, designation, email, join_date FROM teachers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(rs.getInt("teacher_id"))); 
                row.add(rs.getString("name"));                   
                row.add(rs.getString("designation"));            
                row.add(rs.getString("email"));                  
                row.add(rs.getString("join_date"));              
                teacherList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teacherList;
    }

    public boolean deleteTeacher(int teacherId) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM teachers WHERE teacher_id = ?");
            stmt1.setInt(1, teacherId);
            stmt1.executeUpdate();

            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM users WHERE user_id = ?");
            stmt2.setInt(1, teacherId);
            stmt2.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
