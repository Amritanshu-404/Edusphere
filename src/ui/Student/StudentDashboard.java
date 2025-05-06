package ui.student;

import dao.student.StudentNameDao;
import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private int studentId;

    public StudentDashboard(int studentId) {
        this.studentId = studentId;

        setTitle("Student Dashboard");
        setSize(1545, 870);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(0, 0);
        setLayout(new BorderLayout());
        setResizable(false);

        String studentName = StudentNameDao.getStudentName(studentId);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(39, 174, 96));
        topPanel.setPreferredSize(new Dimension(1000, 50));

        JLabel title = new JLabel("Welcome, " + studentName + "!");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Roboto", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        topPanel.add(title, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(245, 245, 245));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));

        JButton dashboardBtn = createNavButton("Dashboard");
        JButton classesBtn = createNavButton("My Performance");
        JButton assignmentsBtn = createNavButton("View Assignments");
        JButton materialsBtn = createNavButton("Study Materials");

        for (JButton btn : new JButton[]{dashboardBtn, classesBtn, assignmentsBtn, materialsBtn}) {
            sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
            sidebar.add(btn);
        }

        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = new JButton("⭘ Logout");
        logoutBtn.setBackground(new Color(231, 76, 70));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        logoutBtn.setMaximumSize(new Dimension(200, 40));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(logoutBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new HomePanel(studentId), "Home");
        mainPanel.add(new Performance(studentId), "Performance");
        mainPanel.add(new ViewAssignment(studentId), "View Assignments");
        mainPanel.add(new StudyMaterials(studentId), "Study Materials");

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "Home");

        dashboardBtn.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        classesBtn.addActionListener(e -> cardLayout.show(mainPanel, "Performance"));
        assignmentsBtn.addActionListener(e -> cardLayout.show(mainPanel, "View Assignments"));
        materialsBtn.addActionListener(e -> cardLayout.show(mainPanel, "Study Materials"));

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.OK_OPTION) {
                dispose();
            }
        });

        setVisible(true);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(24, 24, 24));
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(200, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
}
