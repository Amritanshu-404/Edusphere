package ui.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dao.LoginDao;
import ui.Admin.AdminDashboard;
import ui.student.StudentDashboard;
import ui.Teacher.TeacherDashboard;

public class LoginPage extends JFrame implements ActionListener {

    private LandingPage landingPageRef;

    private Container c;
    private JLabel label1, label2, label3;
    private JComboBox<String> usertype;
    private JTextField username;
    private JPasswordField password;
    private JButton btn1, btn2;

    public LoginPage(LandingPage landingPage) {
        this.landingPageRef = landingPage;

        setTitle("Login Page");
        setBounds(300, 150, 900, 500);
        c = getContentPane();
        c.setLayout(new BorderLayout());
        setResizable(false);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        ImageIcon originalIcon = new ImageIcon(ClassLoader.getSystemResource("resources/images/login.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(450, 500, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        leftPanel.add(imageLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(450, 500));

        Font labelFont = new Font("Roboto", Font.BOLD, 14);

        label1 = new JLabel("User Type");
        label1.setFont(labelFont);
        label1.setBounds(50, 80, 100, 30);

        label2 = new JLabel("Username");
        label2.setFont(labelFont);
        label2.setBounds(50, 150, 100, 30);

        label3 = new JLabel("Password");
        label3.setFont(labelFont);
        label3.setBounds(50, 220, 100, 30);

        rightPanel.add(label1);
        rightPanel.add(label2);
        rightPanel.add(label3);

        String[] str = {"Admin", "Teacher", "Student"};
        usertype = new JComboBox<>(str);
        usertype.setBounds(50, 110, 300, 30);
        usertype.setBackground(Color.WHITE);
        rightPanel.add(usertype);

        username = new JTextField();
        username.setBounds(50, 180, 300, 30);
        rightPanel.add(username);

        password = new JPasswordField();
        password.setBounds(50, 250, 300, 30);
        rightPanel.add(password);

        btn1 = createStyledButton("Sign In", new Color(0, 123, 255), new Color(0, 100, 210));
        btn1.setBounds(230, 350, 120, 40);
        rightPanel.add(btn1);

        btn2 = createStyledButton("Back", new Color(220, 53, 69), new Color(200, 40, 50));
        btn2.setBounds(50, 350, 120, 40);
        rightPanel.add(btn2);

        btn1.addActionListener(this);
        btn2.addActionListener(this);

        c.add(leftPanel, BorderLayout.WEST);
        c.add(rightPanel, BorderLayout.EAST);

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text.toUpperCase());
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = button.getModel().isRollover() ? hoverColor : bgColor;
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);
                g2.dispose();
                super.paint(g, c);
            }
        });

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn2) {
            setVisible(false);
            if (landingPageRef != null) {
                landingPageRef.setVisible(true);
            }
        }

        if (e.getSource() == btn1) {
            String utype = (String) usertype.getSelectedItem();
            String uname = username.getText().trim();
            String pass = new String(password.getPassword()).trim();

            LoginDao logindao = new LoginDao();
            boolean isAuthenticated = false;

            int studentId = -1;
            int teacherId = -1;

            if (utype.equalsIgnoreCase("Admin")) {
                try {
                    int userId = Integer.parseInt(uname);
                    isAuthenticated = logindao.authenticateAdmin(userId, pass);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Admin ID must be a number");
                    return;
                }
            } else {
                isAuthenticated = logindao.authenticateUser(utype, uname, pass);

                if (isAuthenticated) {
                    if (utype.equalsIgnoreCase("Student")) {
                        studentId = logindao.getStudentIdByUsername(uname);
                    } else if (utype.equalsIgnoreCase("Teacher")) {
                        teacherId = logindao.getTeacherIdByUsername(uname);
                    }
                }
            }

            if (isAuthenticated) {
                setVisible(false);
                if (landingPageRef != null) landingPageRef.dispose();

                switch (utype.toLowerCase()) {
                    case "admin":
                        new AdminDashboard();
                        break;
                    case "teacher":
                        if (teacherId != -1) {
                            new TeacherDashboard(teacherId);
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to retrieve Teacher ID.");
                        }
                        break;
                    case "student":
                        if (studentId != -1) {
                            new StudentDashboard(studentId);
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to retrieve Student ID.");
                        }
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
            }
        }
    }

    public static void main(String[] args) {
    }
}
