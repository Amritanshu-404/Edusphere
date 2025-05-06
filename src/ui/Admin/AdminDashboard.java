package ui.Admin;

import ui.common.LandingPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminDashboard extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    private StudentListPanel studentListPanel;
    private TeacherListPanel teacherListPanel;
    private ViewCoursePanel viewCoursePanel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
       setSize(1545, 870);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(0, 0);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(39, 174, 96));
        topPanel.setPreferredSize(new Dimension(1000, 50));

        JLabel title = new JLabel("Welcome Admin!");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        topPanel.add(title, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(245, 245, 245));
        sidebar.setPreferredSize(new Dimension(300, getHeight()));

        Font menuFont = new Font("SansSerif", Font.BOLD, 14);

        JButton manageTeacherBtn = new JButton("Manage Teachers ⯆");
        manageTeacherBtn.setFont(menuFont);
        manageTeacherBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addTeacherBtn = new JButton("➕ Add Teacher");
        JButton showTeacherBtn = new JButton("📄 Show Teachers");

        JButton manageStudentBtn = new JButton("Manage Students ⯆");
        manageStudentBtn.setFont(menuFont);
        manageStudentBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addStudentBtn = new JButton("➕ Add Student");
        JButton showStudentBtn = new JButton("📄 Show Students");

        JButton manageClassBtn = new JButton("Manage Classes ⯆");
        manageClassBtn.setFont(menuFont);
        manageClassBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addClassBtn = new JButton("➕ Create Class");
        JButton showClassBtn = new JButton("📄 Show Classes");

        for (JButton btn : new JButton[]{addTeacherBtn, showTeacherBtn, addStudentBtn, showStudentBtn, addClassBtn, showClassBtn}) {
            btn.setBackground(new Color(230, 230, 230));
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
            btn.setMaximumSize(new Dimension(250, 35));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setVisible(false);
        }

        for (JButton btn : new JButton[]{manageTeacherBtn, manageStudentBtn, manageClassBtn}) {
            btn.setBackground(new Color(24, 24, 24));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setMaximumSize(new Dimension(250, 40));
            btn.setBorderPainted(false);
        }

        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(manageTeacherBtn);
        sidebar.add(addTeacherBtn);
        sidebar.add(showTeacherBtn);

        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(manageStudentBtn);
        sidebar.add(addStudentBtn);
        sidebar.add(showStudentBtn);

        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(manageClassBtn);
        sidebar.add(addClassBtn);
        sidebar.add(showClassBtn);

        sidebar.add(Box.createVerticalGlue());
        JButton logoutBtn = new JButton("⭘ Logout");
        logoutBtn.setBackground(new Color(231, 76, 70));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(menuFont);
        logoutBtn.setMaximumSize(new Dimension(250, 40));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setBorderPainted(false);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(logoutBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel, BorderLayout.CENTER);

        AddCoursePanel addCoursePanel = new AddCoursePanel();
        AddStudentPanel addStudentPanel = new AddStudentPanel(addCoursePanel);

        studentListPanel = new StudentListPanel();
        teacherListPanel = new TeacherListPanel();
        AddTeacherPanel addTeacherPanel = new AddTeacherPanel(addCoursePanel);
        viewCoursePanel = new ViewCoursePanel();

        mainPanel.add(new JPanel(), "home"); // Empty home
        mainPanel.add(addStudentPanel, "addStudent");
        mainPanel.add(studentListPanel, "showStudent");
        mainPanel.add(addTeacherPanel, "addTeacher");
        mainPanel.add(teacherListPanel, "showTeacher");
        mainPanel.add(addCoursePanel, "addClass");
        mainPanel.add(viewCoursePanel, "showClass");

        cardLayout.show(mainPanel, "home");

        manageTeacherBtn.addActionListener(e -> {
            boolean visible = addTeacherBtn.isVisible();
            addTeacherBtn.setVisible(!visible);
            showTeacherBtn.setVisible(!visible);
        });

        manageStudentBtn.addActionListener(e -> {
            boolean visible = addStudentBtn.isVisible();
            addStudentBtn.setVisible(!visible);
            showStudentBtn.setVisible(!visible);
        });

        manageClassBtn.addActionListener(e -> {
            boolean visible = addClassBtn.isVisible();
            addClassBtn.setVisible(!visible);
            showClassBtn.setVisible(!visible);
        });

        addTeacherBtn.addActionListener(e -> cardLayout.show(mainPanel, "addTeacher"));

        showTeacherBtn.addActionListener(e -> {
            teacherListPanel.refresh();
            cardLayout.show(mainPanel, "showTeacher");
        });

        addStudentBtn.addActionListener(e -> cardLayout.show(mainPanel, "addStudent"));

        showStudentBtn.addActionListener(e -> {
            studentListPanel.refresh();
            cardLayout.show(mainPanel, "showStudent");
        });

        addClassBtn.addActionListener(e -> cardLayout.show(mainPanel, "addClass"));

        showClassBtn.addActionListener(e -> {
            viewCoursePanel.refresh();
            cardLayout.show(mainPanel, "showClass");
        });

        logoutBtn.addActionListener((ActionEvent e) -> {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboard::new);
    }
}
