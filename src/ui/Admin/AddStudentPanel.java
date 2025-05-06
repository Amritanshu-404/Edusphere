package ui.Admin;

import dao.admin.AddStudentDao;

import javax.swing.*;
import java.awt.*;

public class AddStudentPanel extends JPanel {

    private JTextField nameField, rollField, emailField, admissionDateField;
    private JPasswordField passwordField;
    private JComboBox<String> genderBox, semesterBox;

    private AddCoursePanel coursePanel;

    public AddStudentPanel(AddCoursePanel coursePanel) {
        this.coursePanel = coursePanel;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.WHITE);
        add(centerWrapper, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        nameField = new JTextField(30);
        rollField = new JTextField(30);
        emailField = new JTextField(30);
        admissionDateField = new JTextField(30);
        passwordField = new JPasswordField(30);

        semesterBox = new JComboBox<>();
        for (int i = 1; i <= 4; i++) semesterBox.addItem(String.valueOf(i));

        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        JButton addButton = new JButton("Add Student");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addButton.setFocusPainted(false);
        addButton.setPreferredSize(new Dimension(150, 35));

        addLabeledField(formPanel, gbc, "Full Name:", nameField, 0);
        addLabeledField(formPanel, gbc, "Roll Number:", rollField, 1);
        addLabeledField(formPanel, gbc, "Email:", emailField, 2);
        addLabeledField(formPanel, gbc, "Semester:", semesterBox, 3);
        addLabeledField(formPanel, gbc, "Gender:", genderBox, 4);
        addLabeledField(formPanel, gbc, "Admission Date (YYYY-MM-DD):", admissionDateField, 5);
        addLabeledField(formPanel, gbc, "Password:", passwordField, 6);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(addButton, gbc);

        centerWrapper.add(formPanel);

        addButton.addActionListener(e -> handleAddStudent());
    }

    private void addLabeledField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    private void handleAddStudent() {
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String email = emailField.getText().trim();
        String semesterStr = (String) semesterBox.getSelectedItem();
        String gender = (String) genderBox.getSelectedItem();
        String admissionDate = admissionDateField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (name.isEmpty() || roll.isEmpty() || email.isEmpty() || admissionDate.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try {
            int semester = Integer.parseInt(semesterStr);
            boolean success = AddStudentDao.addStudent(name, roll, email, semester, gender, admissionDate, password);

            if (success) {
                nameField.setText(""); rollField.setText(""); emailField.setText("");
                admissionDateField.setText(""); passwordField.setText("");
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                if (coursePanel != null) {
                    coursePanel.loadStudents();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
