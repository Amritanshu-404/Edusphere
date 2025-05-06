package ui.Teacher;

import dao.teacher.UploadMaterialDao;
import config.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.time.LocalDate;

public class UploadMaterial extends JPanel {
    private Choice subjectChoice;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField chapterField;
    private JTextField filePathField;
    private JButton browseButton;
    private JButton uploadButton;

    private File selectedFile;
    private int teacherId;

    public UploadMaterial(int teacherId) {
        this.teacherId = teacherId;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 600));
        setBackground(new Color(245, 245, 245));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel heading = new JLabel("Upload Study Material");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(new Color(0, 51, 102));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(heading, gbc);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(subjectLabel, gbc);

        subjectChoice = new Choice();
        subjectChoice.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        formPanel.add(subjectChoice, gbc);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(titleLabel, gbc);

        titleField = new JTextField();
        titleField.setFont(new Font("Arial", Font.PLAIN, 14));
        titleField.setPreferredSize(new Dimension(300, 30));
        titleField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);

       
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(descLabel, gbc);

        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        formPanel.add(descScroll, gbc);

        JLabel chapterLabel = new JLabel("Chapter Number:");
        chapterLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(chapterLabel, gbc);

        chapterField = new JTextField();
        chapterField.setFont(new Font("Arial", Font.PLAIN, 14));
        chapterField.setPreferredSize(new Dimension(300, 30));
        chapterField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        gbc.gridx = 1;
        formPanel.add(chapterField, gbc);

        JLabel fileLabel = new JLabel("Select File:");
        fileLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(fileLabel, gbc);

        JPanel filePanel = new JPanel(new BorderLayout(10, 0));
        filePathField = new JTextField();
        filePathField.setEditable(false);
        filePathField.setFont(new Font("Arial", Font.PLAIN, 14));

        browseButton = new JButton("Browse");
        browseButton.setFont(new Font("Arial", Font.PLAIN, 14));
        browseButton.addActionListener(e -> browseFile());

        filePanel.add(filePathField, BorderLayout.CENTER);
        filePanel.add(browseButton, BorderLayout.EAST);
        gbc.gridx = 1;
        formPanel.add(filePanel, gbc);

        uploadButton = new JButton("Upload Material");
        uploadButton.setBackground(new Color(0, 51, 102));
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFont(new Font("Arial", Font.BOLD, 16));
        uploadButton.setPreferredSize(new Dimension(300, 40));
        uploadButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(uploadButton, gbc);

        uploadButton.addActionListener(this::handleUpload);

   
        loadSubjects();

        add(formPanel, BorderLayout.CENTER);
    }

    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void handleUpload(ActionEvent e) {
        String subjectName = subjectChoice.getSelectedItem();
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String chapterNumStr = chapterField.getText().trim();

        if (title.isEmpty() || selectedFile == null || chapterNumStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields and choose a file.");
            return;
        }

        try {
            int chapterNumber = Integer.parseInt(chapterNumStr);
            LocalDate uploadDate = LocalDate.now();

            int subjectId = UploadMaterialDao.getSubjectId(subjectName, teacherId);
            if (subjectId == -1) {
                JOptionPane.showMessageDialog(this, "Invalid subject.");
                return;
            }

            String filePath = UploadMaterialDao.saveFile(selectedFile);
            boolean success = UploadMaterialDao.insertStudyMaterial(subjectId, title, description, uploadDate, chapterNumber, filePath);

            if (success) {
                JOptionPane.showMessageDialog(this, "Material uploaded successfully!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Upload failed.");
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Chapter number must be an integer.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Upload failed.");
        }
    }

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        chapterField.setText("");
        filePathField.setText("");
        selectedFile = null;
    }

    private void loadSubjects() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT name FROM subjects WHERE teacher_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            subjectChoice.removeAll();
            while (rs.next()) {
                subjectChoice.add(rs.getString("name"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load subjects.");
        }
    }

    public void refresh() {
        loadSubjects();
        clearForm();
    }
}
