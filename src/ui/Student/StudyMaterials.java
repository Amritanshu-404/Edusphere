package ui.student;

import dao.student.StudyMaterialDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class StudyMaterials extends JPanel {

    private JComboBox<String> subjectDropdown;
    private JTable materialsTable;
    private DefaultTableModel tableModel;
    private Map<String, Integer> subjectMap = new HashMap<>();
    private int studentId;

    public StudyMaterials(int studentId) {
        this.studentId = studentId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Download Your Study Materials", JLabel.CENTER);
        title.setFont(new Font("Roboto", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel selectLabel = new JLabel("Select Subject:");
        subjectDropdown = new JComboBox<>();
        subjectDropdown.setPreferredSize(new Dimension(200, 30));
        topPanel.add(selectLabel);
        topPanel.add(subjectDropdown);
        add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        tableModel = new DefaultTableModel(new String[]{"Title", "Description", "Upload Date", "Chapter No", "Download"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        materialsTable = new JTable(tableModel);
        materialsTable.setRowHeight(30);
        materialsTable.getColumn("Download").setCellRenderer(new ButtonRenderer());
        materialsTable.getColumn("Download").setCellEditor(new ButtonEditor(new JCheckBox()));
        add(new JScrollPane(materialsTable), BorderLayout.CENTER);

        loadSubjects();

        subjectDropdown.addActionListener(e -> {
            String selected = (String) subjectDropdown.getSelectedItem();
            if (selected != null) {
                loadStudyMaterials(subjectMap.get(selected));
            }
        });
    }

    private void loadSubjects() {
        subjectMap = StudyMaterialDao.getSubjectsForStudent(studentId);
        subjectDropdown.removeAllItems();
        for (String subjectName : subjectMap.keySet()) {
            subjectDropdown.addItem(subjectName);
        }

        if (subjectDropdown.getItemCount() > 0) {
            subjectDropdown.setSelectedIndex(0);
            loadStudyMaterials(subjectMap.get(subjectDropdown.getItemAt(0)));
        }
    }

    private void loadStudyMaterials(int subjectId) {
        List<Object[]> materials = StudyMaterialDao.getStudyMaterialsBySubject(subjectId);
        tableModel.setRowCount(0);
        for (Object[] row : materials) {
            tableModel.addRow(row);
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {

        public ButtonRenderer() {
            setText("Download");
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        private JButton button;
        private String filePath;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Download");
            button.addActionListener(e -> {
                if (filePath != null) {
                    try {
                        Desktop.getDesktop().open(new File(filePath));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Failed to open file.");
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            filePath = (String) table.getValueAt(row, 4);
            return button;
        }
    }
}
