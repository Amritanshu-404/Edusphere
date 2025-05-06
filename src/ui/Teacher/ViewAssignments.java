package ui.Teacher;

import dao.ViewAssignmentsDao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewAssignments extends JPanel {

    private JTable assignmentTable;
    private DefaultTableModel tableModel;
    private int teacherId;

    public ViewAssignments(int teacherId) {
        this.teacherId = teacherId;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Your Created Assignments");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(10, 0, 20, 0));
        add(title, BorderLayout.NORTH);

     
        String[] columns = {"Title", "Subject", "Semester", "Due Date", "Full Marks"};
        tableModel = new DefaultTableModel(columns, 0);
        assignmentTable = new JTable(tableModel);
        assignmentTable.setRowHeight(30);
        assignmentTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        assignmentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        assignmentTable.setFillsViewportHeight(true);
        assignmentTable.setShowGrid(true);
        assignmentTable.setGridColor(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(assignmentTable);
        add(scrollPane, BorderLayout.CENTER);

        loadAssignments();
    }

    private void loadAssignments() {
        ViewAssignmentsDao dao = new ViewAssignmentsDao(teacherId);
        List<Object[]> assignments = dao.getAssignments();

        for (Object[] assignment : assignments) {
            tableModel.addRow(assignment);
        }
    }
}
