package ui.student;

import dao.student.StudentHomeDao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HomePanel extends JPanel {

    private int studentId;
    private JTable timetableTable;
    private JTable attendanceTable;
    private DefaultTableModel timetableModel;
    private DefaultTableModel attendanceModel;
    private JTextArea announcementArea;
    private JTable assignmentTable;
    private DefaultTableModel assignmentModel;

    public HomePanel(int studentId) {
        this.studentId = studentId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(20, 10, 40, 60));

        JLabel timetableLabel = new JLabel("📘 Timetable");
        timetableLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        timetableLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        timetableLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        timetableModel = new DefaultTableModel(new String[]{"Subject", "Timing", "Teacher"}, 0);
        timetableTable = new JTable(timetableModel);
        JScrollPane timetableScroll = new JScrollPane(timetableTable);
        timetableScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        timetableScroll.setPreferredSize(new Dimension(900, 200));

        timetableScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        JLabel attendanceLabel = new JLabel("📊 Attendance Summary");
        attendanceLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        attendanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        attendanceLabel.setBorder(new EmptyBorder(30, 0, 10, 0));

        attendanceModel = new DefaultTableModel(new String[]{"Subject", "Total Classes", "Present", "Attendance %"}, 0);
        attendanceTable = new JTable(attendanceModel);
        JScrollPane attendanceScroll = new JScrollPane(attendanceTable);
        attendanceScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        attendanceScroll.setPreferredSize(new Dimension(900, 200));
        attendanceScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        JLabel announcementLabel = new JLabel("📢 Announcements");
        announcementLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        announcementLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        announcementLabel.setBorder(new EmptyBorder(30, 0, 10, 0));

        announcementArea = new JTextArea(5, 20);
        announcementArea.setLineWrap(true);
        announcementArea.setWrapStyleWord(true);
        announcementArea.setEditable(false);
        announcementArea.setBackground(new Color(245, 245, 245));
        JScrollPane announcementScroll = new JScrollPane(announcementArea);
        announcementScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        announcementScroll.setPreferredSize(new Dimension(480, 150));
        announcementScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        JLabel assignmentLabel = new JLabel("📝 Assignments");
        assignmentLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        assignmentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        assignmentLabel.setBorder(new EmptyBorder(30, 0, 10, 0));

        assignmentModel = new DefaultTableModel(new String[]{"Assignment Name", "Subject", "Due Date"}, 0);
        assignmentTable = new JTable(assignmentModel);
        JScrollPane assignmentScroll = new JScrollPane(assignmentTable);
        assignmentScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        assignmentScroll.setPreferredSize(new Dimension(480, 150));
        assignmentScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        JPanel announcementsAssignmentsPanel = new JPanel();
        announcementsAssignmentsPanel.setLayout(new BoxLayout(announcementsAssignmentsPanel, BoxLayout.X_AXIS));
        announcementsAssignmentsPanel.setBackground(Color.WHITE);
        announcementsAssignmentsPanel.add(announcementScroll);
        announcementsAssignmentsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        announcementsAssignmentsPanel.add(assignmentScroll);

        centerPanel.add(timetableLabel);
        centerPanel.add(timetableScroll);
        centerPanel.add(attendanceLabel);
        centerPanel.add(attendanceScroll);
        centerPanel.add(announcementLabel);
        centerPanel.add(announcementsAssignmentsPanel);

        add(centerPanel, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        loadTimetableData();
        loadAttendanceData();
        loadAssignments();
    }

    private void loadTimetableData() {
        List<Object[]> timetableData = StudentHomeDao.getTimetable(studentId);
        for (Object[] row : timetableData) {
            timetableModel.addRow(row);
        }
    }

    private void loadAttendanceData() {
        List<Object[]> attendanceData = StudentHomeDao.getAttendance(studentId);
        for (Object[] row : attendanceData) {
            attendanceModel.addRow(row);
        }
    }

    private void loadAssignments() {
        List<Object[]> assignmentData = StudentHomeDao.getAssignments(studentId);
        for (Object[] row : assignmentData) {
            assignmentModel.addRow(row);
        }
    }
}
