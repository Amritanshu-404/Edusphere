package dao.teacher;

import config.DBConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class CheckSubmissionsDao {

    public ResultSet getSubmissions(int teacherId) {
        String query = "SELECT sub.submission_id, st.name AS student_name, a.title AS assignment_title, " +
                       "sub.submission_date, sub.marks_obtained, sub.feedback, sub.file_path " +
                       "FROM submissions sub " +
                       "JOIN students st ON sub.student_id = st.student_id " +
                       "JOIN assignments a ON sub.assignment_id = a.assignment_id " +
                       "JOIN subjects s ON a.subject_id = s.subject_id " +
                       "WHERE s.teacher_id = ? " +
                       "ORDER BY st.name, a.title";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, teacherId);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        }
        return null;
    }

    public int updateSubmissions(DefaultTableModel tableModel) {
        int updatedCount = 0;
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            String updateQuery = "UPDATE submissions SET marks_obtained = ?, feedback = ? WHERE submission_id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);

            for (int row = 0; row < tableModel.getRowCount(); row++) {
                int submissionId = (int) tableModel.getValueAt(row, 6);
                Object marks = tableModel.getValueAt(row, 3);
                String feedback = (String) tableModel.getValueAt(row, 4);

                stmt.setObject(1, marks);
                stmt.setString(2, feedback);
                stmt.setInt(3, submissionId);
                stmt.addBatch();
                updatedCount++;
            }

            int[] results = stmt.executeBatch();
            conn.commit();

            int successCount = 0;
            for (int result : results) {
                if (result >= 0) successCount++;
            }
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return updatedCount;
    }
}
