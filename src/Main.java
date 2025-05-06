import config.DBConnection;
import ui.common.LandingPage;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("✅ Connected to database: " + conn.getCatalog());
        } catch (SQLException e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null,
                        "Cannot continue without a DB connection. Exiting.",
                        "Startup Error", JOptionPane.ERROR_MESSAGE);
            });
            return;
        }

        SwingUtilities.invokeLater(() -> {
            new LandingPage();
        });
    }
}
