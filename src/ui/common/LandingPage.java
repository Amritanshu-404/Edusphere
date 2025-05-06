package ui.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LandingPage extends JFrame implements ActionListener {
    JButton getStarted;

    public LandingPage() {
        setSize(1545, 870);
        setLocation(0, 0);
        setTitle("EduSphere LMS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(null);
        leftPanel.setPreferredSize(new Dimension(800, 1080));

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        JPanel imageContainer = new JPanel(new GridBagLayout());
        imageContainer.setBackground(Color.WHITE);
        imageContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ImageIcon image = new ImageIcon(ClassLoader.getSystemResource("resources/images/landing_image.png"));
        Image scaledImage = image.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageContainer.add(imageLabel);
        rightPanel.add(imageContainer);

        JLabel decor1 = new JLabel("• • • • •");
        decor1.setBounds(150, 180, 500, 30);
        decor1.setForeground(new Color(0, 120, 215));
        decor1.setFont(new Font("Roboto", Font.PLAIN, 24));
        leftPanel.add(decor1);

        JLabel learningWith = new JLabel("Learning with");
        learningWith.setBounds(170, 220, 700, 100);
        learningWith.setForeground(Color.decode("#181818"));
        learningWith.setFont(new Font("Roboto", Font.PLAIN, 60));
        leftPanel.add(learningWith);

        JLabel eduSphere = new JLabel("EDUSPHERE");
        eduSphere.setBounds(170, 295, 700, 100);
        eduSphere.setForeground(Color.decode("#181818"));
        eduSphere.setFont(new Font("Roboto", Font.BOLD, 70));
        leftPanel.add(eduSphere);

        JLabel subHeading1 = new JLabel("Transform your learning experience with our comprehensive");
        subHeading1.setBounds(170, 400, 600, 30);
        subHeading1.setForeground(Color.decode("#181818"));
        subHeading1.setFont(new Font("Roboto", Font.PLAIN, 18));
        leftPanel.add(subHeading1);

        JLabel subHeading2 = new JLabel("LMS platform");
        subHeading2.setBounds(170, 430, 600, 30);
        subHeading2.setForeground(Color.decode("#181818"));
        subHeading2.setFont(new Font("Roboto", Font.PLAIN, 18));
        leftPanel.add(subHeading2);

   
        getStarted = new JButton("GET STARTED") {
            Color normal = new Color(0, 120, 215);
            Color hover = new Color(0, 100, 180);

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getModel().isRollover() ? hover : normal);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        getStarted.setBounds(175, 500, 220, 60);
        getStarted.setForeground(Color.WHITE);
        getStarted.setFont(new Font("Roboto", Font.BOLD, 20));
        getStarted.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 25));
        getStarted.setContentAreaFilled(false);
        getStarted.setFocusPainted(false);
        getStarted.setCursor(new Cursor(Cursor.HAND_CURSOR));
        getStarted.addActionListener(this);
        leftPanel.add(getStarted);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getStarted) {
            new LoginPage(this);
        }
    }

    public static void main(String[] args) {
        new LandingPage();
    }
}
