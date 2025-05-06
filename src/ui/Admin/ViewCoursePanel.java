package ui.Admin;

import dao.admin.ViewCourseDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewCoursePanel extends JPanel {

    private JTable courseTable;
    private DefaultTableModel courseTableModel;
    private final ViewCourseDao dao = new ViewCourseDao();

    public ViewCoursePanel() {
        setLayout(new BorderLayout());

        courseTableModel = new DefaultTableModel(
                new String[]{"Subject ID", "Subject Name", "Semester", "Timing", "Teacher Name", "Students Enrolled"}, 0
        );
        courseTable = new JTable(courseTableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refresh());
        add(refreshBtn, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        courseTableModel.setRowCount(0);
        List<Object[]> courseList = dao.fetchCourses();

        for (Object[] row : courseList) {
            courseTableModel.addRow(row);
        }
    }
}
