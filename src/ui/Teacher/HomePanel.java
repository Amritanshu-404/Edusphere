package ui.Teacher;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    public HomePanel(int teacherId) {
        setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Teacher Dashboard", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.CENTER);
    }
}
