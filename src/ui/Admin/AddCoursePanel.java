package ui.Admin;

import dao.admin.AddCourseDao;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddCoursePanel extends JPanel {

    private JComboBox<String> teacherComboBox;
    private JList<String> studentList;
    private DefaultListModel<String> studentListModel;
    private JTextField courseNameField, timingField;
    private JComboBox<String> semesterComboBox;

    public AddCoursePanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        formPanel.add(new JLabel("Course Name:"));
        courseNameField = new JTextField();
        formPanel.add(courseNameField);

        formPanel.add(new JLabel("Semester:"));
        semesterComboBox = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        
        formPanel.add(semesterComboBox);

        formPanel.add(new JLabel("Timing:"));
        timingField = new JTextField();
        formPanel.add(timingField);

        formPanel.add(new JLabel("Assign Teacher:"));
        teacherComboBox = new JComboBox<>();
        formPanel.add(teacherComboBox);

        formPanel.add(new JLabel("Enroll Students:"));
        studentListModel = new DefaultListModel<>();
        studentList = new JList<>(studentListModel);
        studentList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(studentList);
        formPanel.add(scrollPane);

        add(formPanel, BorderLayout.CENTER);

        JButton submitBtn = new JButton("Create Course");
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(submitBtn, BorderLayout.CENTER);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(buttonPanel, BorderLayout.SOUTH);

        loadTeachers();
        loadStudents();

        submitBtn.addActionListener(e -> handleCreateCourse());
    }

    public void loadTeachers() {
        try {
            teacherComboBox.removeAllItems();
            for (String teacher : AddCourseDao.getAllTeachers()) {
                teacherComboBox.addItem(teacher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadStudents() {
        try {
            studentListModel.clear();
            for (String student : AddCourseDao.getAllStudents()) {
                studentListModel.addElement(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCreateCourse() {
        String name = courseNameField.getText().trim();
        String semText = (String) semesterComboBox.getSelectedItem();
        String timing = timingField.getText().trim();

        if (name.isEmpty() || semText == null || teacherComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }

        int semester;
        try {
            semester = Integer.parseInt(semText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Semester must be a number.");
            return;
        }

        int teacherId = Integer.parseInt(teacherComboBox.getSelectedItem().toString().split(" - ")[0]);
        List<Integer> studentIds = new ArrayList<>();
        for (String selected : studentList.getSelectedValuesList()) {
            studentIds.add(Integer.parseInt(selected.split(" - ")[0]));
        }

        try {
            boolean success = AddCourseDao.createCourse(name, semester, timing, teacherId, studentIds);
            if (success) {
                JOptionPane.showMessageDialog(this, "Course created and students enrolled!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create course.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while creating course.");
        }
    }
}
