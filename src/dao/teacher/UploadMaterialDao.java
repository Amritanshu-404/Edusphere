package dao.teacher;

import config.DBConnection;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;

public class UploadMaterialDao {
    public static int getSubjectId(String subjectName, int teacherId) throws SQLException {
        String query = "SELECT subject_id FROM subjects WHERE name = ? AND teacher_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, subjectName);
            stmt.setInt(2, teacherId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("subject_id");
            }
            return -1;
        }
    }

    public static String saveFile(File selectedFile) throws Exception {
        String destDir = "uploads/";
        File destFolder = new File(destDir);
        if (!destFolder.exists()) destFolder.mkdirs();

        String destPath = destDir + selectedFile.getName();
        File destFile = new File(destPath);
        Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return destPath;
    }

    public static boolean insertStudyMaterial(int subjectId, String title, String description,
                                              LocalDate uploadDate, int chapterNumber, String filePath) {
        String insertQuery = "INSERT INTO study_materials (subject_id, title, description, upload_date, chapter_number, file_path) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setInt(1, subjectId);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setDate(4, Date.valueOf(uploadDate));
            stmt.setInt(5, chapterNumber);
            stmt.setString(6, filePath);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
