package ui.Teacher;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.sql.*;
import java.text.SimpleDateFormat;
import dao.teacher.CheckSubmissionsDao;

public class CheckSubmissions extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private int teacherId;

    public CheckSubmissions(int teacherId) {
        this.teacherId = teacherId;
        initializeUI();
        loadSubmissions();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("STUDENT SUBMISSIONS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton saveButton = new JButton("Save All Changes");
        saveButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        saveButton.addActionListener(e -> saveAllChanges());
        headerPanel.add(saveButton, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
            new String[]{"Student", "Assignment", "Submission Date", "Marks", "Feedback", "Download", 
                        "Submission ID", "FilePath"}, 
            0
        ) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 2: return Date.class;
                    case 3: return Integer.class;
                    case 5: return JButton.class;
                    default: return String.class;
                }
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4 || column == 5;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(220, 220, 220));

        table.removeColumn(table.getColumnModel().getColumn(6));
        table.removeColumn(table.getColumnModel().getColumn(6));

        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Date) {
                    value = dateFormat.format(value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, 
                        hasFocus, row, column);
            }
        });


        TableColumn downloadColumn = table.getColumnModel().getColumn(5);
        downloadColumn.setCellRenderer(new ButtonRenderer());
        downloadColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
        downloadColumn.setPreferredWidth(100);

 
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean stopCellEditing() {
                try {
                    String value = (String) super.getCellEditorValue();
                    if (!value.isEmpty()) {
                        Integer.parseInt(value); 
                    }
                    return super.stopCellEditing();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(table, "Please enter a valid number for marks");
                    return false;
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

   public void loadSubmissions() {
    CheckSubmissionsDao dao = new CheckSubmissionsDao();
    ResultSet rs = dao.getSubmissions(teacherId);
    
    try {
        if (rs != null) {
            while (rs.next()) {
                String filePath = rs.getString("file_path");
                tableModel.addRow(new Object[]{
                    rs.getString("student_name"),
                    rs.getString("assignment_title"),
                    rs.getDate("submission_date"),
                    rs.getObject("marks_obtained"),
                    rs.getString("feedback") != null ? rs.getString("feedback") : "",
                    filePath != null ? "Download" : "",
                    rs.getInt("submission_id"),
                    filePath
                });
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error loading submissions: " + ex.getMessage(),
            "Database Error", 
            JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
    private void saveAllChanges() {
        CheckSubmissionsDao dao = new CheckSubmissionsDao();
        int updatedCount = dao.updateSubmissions(tableModel);

        if (updatedCount > 0) {
            JOptionPane.showMessageDialog(this, 
                "Successfully updated " + updatedCount + " submissions",
                "Update Complete",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "No changes made or error in updating",
                "Update Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null && !value.toString().isEmpty()) {
                setText("Download");
                setEnabled(true);
            } else {
                setText("");
                setEnabled(false);
            }
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String currentFilePath;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                if (currentFilePath != null && !currentFilePath.isEmpty()) {
                    downloadFile(currentFilePath);
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentFilePath = (String) tableModel.getValueAt(row, 7);
            button.setText("Download");
            return button;
        }

        public Object getCellEditorValue() {
            return "Download";
        }

        private void downloadFile(String filePath) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Submission File");
            fileChooser.setSelectedFile(new File(new File(filePath).getName()));

            if (fileChooser.showSaveDialog(CheckSubmissions.this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File source = new File(filePath);
                    File destination = fileChooser.getSelectedFile();
                    Files.copy(source.toPath(), destination.toPath());
                    JOptionPane.showMessageDialog(CheckSubmissions.this,
                        "File downloaded successfully to:\n" + destination.getAbsolutePath(),
                        "Download Complete",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CheckSubmissions.this,
                        "Failed to download file: " + ex.getMessage(),
                        "Download Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
