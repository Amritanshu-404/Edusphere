package ui.Admin;

import dao.admin.ShowDelStudentDao;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentListPanel extends JPanel {
    private final JPanel studentPanel;
    private final ShowDelStudentDao dao = new ShowDelStudentDao();

    public StudentListPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        studentPanel = new JPanel();
        studentPanel.setLayout(new GridLayout(0, 2, 15, 15));
        studentPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(studentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        studentPanel.removeAll();
        List<List<String>> students = dao.fetchStudents();

        if (students.isEmpty()) {
            JLabel noDataLabel = new JLabel("No students found.");
            noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);

            studentPanel.setLayout(new BorderLayout());
            studentPanel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            studentPanel.setLayout(new GridLayout(0, 2, 15, 15));
            for (List<String> s : students) {
                int id = Integer.parseInt(s.get(0));
                String name = s.get(1);
                String roll = s.get(2);
                String sem = s.get(3);
                String email = s.get(4);
                String gender = s.get(5);

                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                card.setPreferredSize(new Dimension(280, 130));
                card.setBackground(new Color(245, 250, 250));

                JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
                infoPanel.setOpaque(false);
                infoPanel.add(new JLabel("ID: " + id));
                infoPanel.add(new JLabel("Name: " + name));
                infoPanel.add(new JLabel("Roll No: " + roll));
                infoPanel.add(new JLabel("Semester: " + sem));
                infoPanel.add(new JLabel("Email: " + email));
                infoPanel.add(new JLabel("Gender: " + gender));

                for (Component comp : infoPanel.getComponents()) {
                    comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                }

                JButton deleteBtn = new JButton("Delete");
                deleteBtn.setForeground(Color.WHITE);
                deleteBtn.setBackground(Color.RED.darker());
                deleteBtn.setFocusPainted(false);
                deleteBtn.setPreferredSize(new Dimension(90, 30));
                deleteBtn.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to delete this student?",
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = dao.deleteStudent(id);
                        if (success) {
                            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                            refresh();
                        } else {
                            JOptionPane.showMessageDialog(this, "Deletion failed!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                rightPanel.setOpaque(false);
                rightPanel.add(deleteBtn);

                card.add(infoPanel, BorderLayout.CENTER);
                card.add(rightPanel, BorderLayout.SOUTH);

                studentPanel.add(card);
            }
        }

        revalidate();
        repaint();
    }
}
