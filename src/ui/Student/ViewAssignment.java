package ui.student;

import dao.student.ViewAssignmentDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ViewAssignment extends JPanel {

    private JComboBox<String> subjectComboBox;
    private JTable assignmentTable;
    private DefaultTableModel model;
    private Map<String, Integer> subjectMap = new HashMap<>();
    private int studentId;
    private final Map<Integer, File> fileMap = new HashMap<>();

    public ViewAssignment(int studentId) {
        this.studentId = studentId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Assignment Submission", JLabel.CENTER);
        title.setFont(new Font("Roboto", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectComboBox = new JComboBox<>();
        topPanel.add(subjectLabel);
        topPanel.add(subjectComboBox);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Title", "Description", "Upload Date", "Due Date", "Total Marks", "Choose File", "Submit"};
        model = new DefaultTableModel(columns, 0);
        assignmentTable = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6;
            }
        };
        JScrollPane scrollPane = new JScrollPane(assignmentTable);
        add(scrollPane, BorderLayout.CENTER);

        assignmentTable.getColumn("Choose File").setCellRenderer(new ButtonRenderer());
        assignmentTable.getColumn("Choose File").setCellEditor(new FileButtonEditor(new JCheckBox()));
        assignmentTable.getColumn("Submit").setCellRenderer(new ButtonRenderer());
        assignmentTable.getColumn("Submit").setCellEditor(new SubmitButtonEditor(new JCheckBox(), this));

        loadSubjects();

        subjectComboBox.addActionListener(e -> {
            model.setRowCount(0);
            loadAssignments();
        });
    }

    private void loadSubjects() {
        subjectMap = ViewAssignmentDao.getSubjectsForStudent(studentId);
        subjectComboBox.removeAllItems();
        for (String name : subjectMap.keySet()) {
            subjectComboBox.addItem(name);
        }
        if (subjectComboBox.getItemCount() > 0) {
            subjectComboBox.setSelectedIndex(0);
            loadAssignments();
        }
    }

    private void loadAssignments() {
        model.setRowCount(0);
        String selectedSubject = (String) subjectComboBox.getSelectedItem();
        if (selectedSubject == null) return;

        int subjectId = subjectMap.get(selectedSubject);
        List<Object[]> rows = ViewAssignmentDao.getAssignmentsBySubject(subjectId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (Object[] row : rows) {
            row[2] = ((java.sql.Date) row[2]).toLocalDate().format(formatter);
            row[3] = ((java.sql.Date) row[3]).toLocalDate().format(formatter);
            model.addRow(row);
        }
    }

    class FileButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;

        public FileButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Choose File");
            button.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    fileMap.put(row, chooser.getSelectedFile());
                    button.setText("Selected");
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            button.setText("Choose File");
            return button;
        }
    }

    class SubmitButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;
        private JPanel parent;

        public SubmitButtonEditor(JCheckBox checkBox, JPanel parent) {
            super(checkBox);
            this.parent = parent;
            button = new JButton("Submit");
            button.addActionListener(e -> {
                File selectedFile = fileMap.get(row);
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(parent, "Please choose a file before submitting.");
                    return;
                }

                String selectedSubject = (String) subjectComboBox.getSelectedItem();
                int subjectId = subjectMap.get(selectedSubject);
                String assignmentTitle = (String) model.getValueAt(row, 0);

                boolean success = ViewAssignmentDao.submitAssignment(studentId, subjectId, assignmentTitle, selectedFile);
                if (success) {
                    JOptionPane.showMessageDialog(parent, "Assignment submitted successfully!");
                } else {
                    JOptionPane.showMessageDialog(parent, "Submission failed.");
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
}
