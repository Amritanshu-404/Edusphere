package ui.Admin;

import dao.admin.ShowDelTeacherDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeacherListPanel extends JPanel {

    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private final ShowDelTeacherDao dao = new ShowDelTeacherDao();

    public TeacherListPanel() {
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Name", "Designation", "Email", "Join Date", "Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        teacherTable = new JTable(tableModel);
        teacherTable.setRowHeight(30);
        teacherTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        teacherTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(teacherTable);
        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<List<String>> teachers = dao.fetchTeachers();

        for (List<String> row : teachers) {
            tableModel.addRow(new Object[]{
                    Integer.parseInt(row.get(0)),
                    row.get(1),
                    row.get(2),
                    row.get(3),
                    row.get(4),
                    "Delete"
            });
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(231, 76, 60));
            setForeground(Color.WHITE);
            setFont(new Font("Tahoma", Font.BOLD, 12));
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText("Delete");
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Delete");
            button.setOpaque(true);
            button.setBackground(new Color(231, 76, 60));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Tahoma", Font.BOLD, 12));
            button.addActionListener(e -> deleteTeacher(selectedRow));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Delete";
        }
    }

    private void deleteTeacher(int row) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this teacher?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int teacherId = (int) tableModel.getValueAt(row, 0);
            if (dao.deleteTeacher(teacherId)) {
                tableModel.removeRow(row);
                JOptionPane.showMessageDialog(this, "Teacher deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting teacher.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
