package ui.Admin;

import dao.admin.AddTeacherDao;

import javax.swing.*;
import java.awt.*;

public class AddTeacherPanel extends JPanel {

    private JTextField nameField, designationField, emailField, joinDateField;
    private JPasswordField passwordField;
    private JComboBox<String> genderBox;

    private AddCoursePanel coursePanel;

    public AddTeacherPanel(AddCoursePanel coursePanel) {
        this.coursePanel = coursePanel;

        setLayout(new GridLayout(7, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Full Name:");
        nameField = new JTextField();

        JLabel designationLabel = new JLabel("Designation:");
        designationField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        JLabel joinLabel = new JLabel("Join Date (YYYY-MM-DD):");
        joinDateField = new JTextField();

        JLabel genderLabel = new JLabel("Gender:");
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton addButton = new JButton("Add Teacher");
        addButton.setBackground(new Color(39, 174, 96));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Tahoma", Font.BOLD, 14));

        add(nameLabel);
        add(nameField);
        add(designationLabel);
        add(designationField);
        add(emailLabel);
        add(emailField);
        add(joinLabel);
        add(joinDateField);
        add(genderLabel);
        add(genderBox);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());
        add(addButton);

        addButton.addActionListener(e -> handleAddTeacher());
    }

    private void handleAddTeacher() {
        String name = nameField.getText().trim();
        String designation = designationField.getText().trim();
        String email = emailField.getText().trim();
        String joinDate = joinDateField.getText().trim();
        String gender = (String) genderBox.getSelectedItem();
        String password = new String(passwordField.getPassword()).trim();

        if (name.isEmpty() || designation.isEmpty() || email.isEmpty() || joinDate.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }

        try {
            boolean success = AddTeacherDao.addTeacher(name, designation, email, joinDate, gender, password);
            if (success) {
                JOptionPane.showMessageDialog(this, "Teacher added successfully!");
                nameField.setText("");
                designationField.setText("");
                emailField.setText("");
                joinDateField.setText("");
                passwordField.setText("");

                if (coursePanel != null) {
                    coursePanel.loadTeachers();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding teacher: " + ex.getMessage());
        }

        try {
            boolean success = AddTeacherDao.addTeacher(name, designation, email, joinDate, gender, password);
            if (success) {
                JOptionPane.showMessageDialog(this, "Teacher added successfully!");
                nameField.setText("");
                designationField.setText("");
                emailField.setText("");
                joinDateField.setText("");
                passwordField.setText("");

                if (coursePanel != null) {
                    coursePanel.loadTeachers();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding teacher: " + ex.getMessage());
        }
    }
}
