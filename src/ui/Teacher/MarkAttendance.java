package ui.Teacher;

import dao.teacher.MarkAttendanceDao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Map;

public class MarkAttendance extends JPanel {
    private Choice subjectChoice;
    private JTable table;
    private DefaultTableModel model;
    private JButton submit;
    private int teacherId;
    private Map<String, Integer> subjectMap;

    public MarkAttendance(int teacherId) {
        this.teacherId = teacherId;
        setLayout(null);
        setPreferredSize(new Dimension(1000, 600));

        JLabel text = new JLabel("Mark Attendance");
        text.setBounds(370, 10, 360, 20);
        text.setFont(new Font("serif", Font.BOLD, 25));
        add(text);

        JLabel l1 = new JLabel("Subject :");
        l1.setBounds(35, 75, 80, 20);
        l1.setFont(new Font("arial", Font.BOLD, 15));
        add(l1);

        subjectChoice = new Choice();
        subjectChoice.setBounds(125, 75, 150, 30);
        subjectChoice.setBackground(Color.WHITE);
        add(subjectChoice);

        subjectChoice.addItemListener(e -> {
            model.setRowCount(0);
            loadEnrolledStudents(subjectChoice.getSelectedItem());
        });

        String[] columnNames = {"Present", "Student ID", "Student Name", "Roll Number", "Semester"};
        model = new DefaultTableModel(columnNames, 0) {
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        table = new JTable(model);
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(10, 150, 970, 350);
        add(jsp);

        submit = new JButton("Submit Attendance");
        submit.setBounds(400, 520, 200, 40);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("serif", Font.BOLD, 20));
        add(submit);

        submit.addActionListener(e -> submitAttendance());

        loadSubjects();

        setVisible(true);
    }

    private void loadSubjects() {
        try {
            MarkAttendanceDao dao = new MarkAttendanceDao();
            subjectMap = dao.loadSubjects(teacherId);
            subjectChoice.removeAll();

            for (String subjectName : subjectMap.keySet()) {
                subjectChoice.add(subjectName);
            }

            if (subjectChoice.getItemCount() > 0) {
                loadEnrolledStudents(subjectChoice.getItem(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load subjects.");
        }
    }

    private void loadEnrolledStudents(String subjectName) {
        try {
            MarkAttendanceDao dao = new MarkAttendanceDao();
            ResultSet rs = dao.loadEnrolledStudents(subjectName);

            while (rs.next()) {
                Object[] row = {
                        false,
                        rs.getString("student_id"),
                        rs.getString("name"),
                        rs.getString("roll_number"),
                        rs.getString("semester")
                };
                model.addRow(row);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load enrolled students.");
        }
    }

    private void submitAttendance() {
        try {
            MarkAttendanceDao dao = new MarkAttendanceDao();
            String subjectName = subjectChoice.getSelectedItem();
            int subjectId = dao.getSubjectId(subjectName);

            dao.submitAttendance(subjectId, model);

            JOptionPane.showMessageDialog(this, "Attendance submitted successfully!");

            refresh();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to submit attendance.");
        }
    }

    public void refresh() {
        subjectChoice.removeAll();
        model.setRowCount(0);
        loadSubjects();
    }
}
