package ui.Teacher;

import dao.teacher.TeacherNameDao; 
import ui.common.LandingPage;

import javax.swing.*;
import java.awt.*;

public class TeacherDashboard extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private int teacherId;

    public TeacherDashboard(int teacherId) {
        this.teacherId = teacherId;

        setTitle("Teacher Dashboard");
        setSize(1545, 870);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(0, 0);
        setLayout(new BorderLayout());
        setResizable(false);

        String teacherName = TeacherNameDao.getTeacherName(teacherId);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(39, 174, 96));
        topPanel.setPreferredSize(new Dimension(1000, 50));

        JLabel title = new JLabel("Welcome, " + teacherName + "!");
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
        JButton attendanceBtn = createNavButton("Mark Attendance");
        JButton assignmentBtn = createNavButton("Create Assignment");
        JButton submissionBtn = createNavButton("Check Submissions");
        JButton materialBtn = createNavButton("Upload Material");
        JButton classesBtn = createNavButton("Created Assingments");

        for (JButton btn : new JButton[]{dashboardBtn, attendanceBtn, assignmentBtn, submissionBtn, materialBtn, classesBtn}) {
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

        mainPanel.add(new HomePanel(teacherId), "Dashboard");
        mainPanel.add(new MarkAttendance(teacherId), "Mark Attendance");
        mainPanel.add(new CreateAssignment(teacherId), "Create Assignment");
        mainPanel.add(new CheckSubmissions(teacherId), "Check Submissions");
        mainPanel.add(new UploadMaterial(teacherId), "Upload Material");
        mainPanel.add(new ViewAssignments(teacherId), "Created Assigments");

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "Dashboard");

        dashboardBtn.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        attendanceBtn.addActionListener(e -> cardLayout.show(mainPanel, "Mark Attendance"));
        assignmentBtn.addActionListener(e -> cardLayout.show(mainPanel, "Create Assignment"));
        submissionBtn.addActionListener(e -> cardLayout.show(mainPanel, "Check Submissions"));
        materialBtn.addActionListener(e -> cardLayout.show(mainPanel, "Upload Material"));
        classesBtn.addActionListener(e -> cardLayout.show(mainPanel, "Created Assigments"));

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.OK_OPTION) {
                new LandingPage();
                dispose();
            }
        });

        setVisible(true);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(24, 24, 24));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Roboto", Font.BOLD, 14));
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
