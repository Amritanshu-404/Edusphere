package ui.Teacher;

import dao.teacher.CreateAssignmentDao;
import config.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.LocalDate;

public class CreateAssignment extends JPanel {
    private Choice subjectChoice;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dueDateField;
    private JTextField totalMarksField;
    private JButton submitButton;

    private int teacherId;

    public CreateAssignment(int teacherId) {
        this.teacherId = teacherId;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 600));
        setBackground(new Color(245, 245, 245)); 

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel heading = new JLabel("Create New Assignment");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(new Color(0, 51, 102));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(heading, gbc);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(new Font("Arial", Font.PLAIN, 16)); 
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(subjectLabel, gbc);

        subjectChoice = new Choice();
        subjectChoice.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(subjectChoice, gbc);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16)); 
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(titleLabel, gbc);

        titleField = new JTextField();
        titleField.setFont(new Font("Arial", Font.PLAIN, 14));
        titleField.setPreferredSize(new Dimension(300, 30));  
        titleField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(titleField, gbc);

        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 16)); 
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(descLabel, gbc);

        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(descScroll, gbc);

        JLabel dueDateLabel = new JLabel("Due Date (YYYY-MM-DD):");
        dueDateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(dueDateLabel, gbc);

        dueDateField = new JTextField();
        dueDateField.setFont(new Font("Arial", Font.PLAIN, 14));
        dueDateField.setPreferredSize(new Dimension(300, 30)); 
        dueDateField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(dueDateField, gbc);
        
        JLabel marksLabel = new JLabel("Total Marks:");
        marksLabel.setFont(new Font("Arial", Font.PLAIN, 16));  
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(marksLabel, gbc);

        totalMarksField = new JTextField();
        totalMarksField.setFont(new Font("Arial", Font.PLAIN, 14));
        totalMarksField.setPreferredSize(new Dimension(300, 30));
        totalMarksField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(totalMarksField, gbc);

        submitButton = new JButton("Create Assignment");
        submitButton.setBackground(new Color(0, 51, 102));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(300, 40));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(submitButton, gbc);

        submitButton.addActionListener(this::handleSubmit);
        
        add(formPanel, BorderLayout.CENTER);

        loadSubjects();
    }

    private void loadSubjects() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT name FROM subjects WHERE teacher_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            subjectChoice.removeAll();
            while (rs.next()) {
                subjectChoice.add(rs.getString("name"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load subjects.");
        }
    }

    private void handleSubmit(ActionEvent e) {
        String subjectName = subjectChoice.getSelectedItem();
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String dueDateStr = dueDateField.getText().trim();
        String totalMarksStr = totalMarksField.getText().trim();

        if (title.isEmpty() || dueDateStr.isEmpty() || totalMarksStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }

        try {
            int totalMarks = Integer.parseInt(totalMarksStr);
            LocalDate dueDate = LocalDate.parse(dueDateStr);
            LocalDate uploadDate = LocalDate.now();

            CreateAssignmentDao dao = new CreateAssignmentDao();
            int subjectId = dao.getSubjectId(subjectName, teacherId);

            if (subjectId == -1) {
                JOptionPane.showMessageDialog(this, "Subject not found.");
                return;
            }

            boolean success = dao.createAssignment(subjectId, title, description, Date.valueOf(uploadDate), Date.valueOf(dueDate), totalMarks);

            if (success) {
                JOptionPane.showMessageDialog(this, "Assignment created successfully!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create assignment.");
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Total marks must be a number.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to create assignment.");
        }
    }

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        dueDateField.setText("");
        totalMarksField.setText("");
    }

    public void refresh() {
        loadSubjects();
        clearForm();
    }
}
