package ui.Admin;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    public HomePanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Admin Home Dashboard", SwingConstants.CENTER);
        label.setFont(new Font("Roboto", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);
    }
}
