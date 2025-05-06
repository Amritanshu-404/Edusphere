package ui.student;

import dao.student.PerformanceDao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Performance extends JPanel {

    private JTable performanceTable;
    private DefaultTableModel tableModel;
    private int studentId;

    public Performance(int studentId) {
        this.studentId = studentId;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Your Performance");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(10, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Assignment Title", "Subject", "Marks Obtained", "Full Marks", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        performanceTable = new JTable(tableModel);
        performanceTable.setRowHeight(30);
        performanceTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        performanceTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        performanceTable.setFillsViewportHeight(true);
        performanceTable.setShowGrid(true);
        performanceTable.setGridColor(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(performanceTable);
        add(scrollPane, BorderLayout.CENTER);

        loadPerformanceData();
    }

    private void loadPerformanceData() {
        List<Object[]> rows = PerformanceDao.getPerformanceData(studentId);
        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
    }
}
