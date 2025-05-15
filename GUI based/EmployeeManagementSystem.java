import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EmployeeManagementSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}

class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JComboBox<String> roleCombo;

    public LoginFrame() {
        setTitle("Employee Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 228, 225)); // Light pink background

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(255, 228, 225)); // Same background for the panel

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(userLabel);
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(passLabel);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(roleLabel);
        roleCombo = new JComboBox<>(new String[]{"Admin", "Employee"});
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(roleCombo);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(100, 149, 237)); // Cornflower blue
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(e -> handleLogin());

        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        if (role.equals("Admin")) {
            if (username.equals("admin") && password.equals("admin123")) {
                new AdminDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Employee login using ID and password
            try (BufferedReader reader = new BufferedReader(new FileReader("employees.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" - ");
                    if (parts.length == 4) {
                        String id = parts[0];
                        String pass = parts[2];
                        if (id.equals(username) && pass.equals(password)) {
                            new EmployeeDashboard(id);
                            dispose();
                            return;
                        }
                    }
                }
                JOptionPane.showMessageDialog(this, "Invalid employee ID or password", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading employee data", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(173, 216, 230)); // Light blue background

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        panel.setBackground(new Color(173, 216, 230)); // Same background for the panel

        JButton manageEmployees = createButton("Manage Employees", new Color(100, 149, 237));
        JButton viewAttendance = createButton("View Attendance Records", new Color(100, 149, 237));
        JButton viewLeaveRequests = createButton("View Leave Requests", new Color(100, 149, 237));
        JButton assignTask = createButton("Assign Task", new Color(100, 149, 237));
        JButton logout = createButton("Logout", new Color(255, 69, 0)); // Red for logout

        manageEmployees.addActionListener(e -> new ManageEmployeesFrame());
        viewAttendance.addActionListener(e -> showTable("attendance.txt", "Attendance Records", new String[]{"Employee", "Status", "Date"}));
        viewLeaveRequests.addActionListener(e -> manageLeaveRequests());
        assignTask.addActionListener(e -> assignTask());
        logout.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        panel.add(manageEmployees);
        panel.add(viewAttendance);
        panel.add(viewLeaveRequests);
        panel.add(assignTask);
        panel.add(logout);

        add(panel);
        setVisible(true);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return btn;
    }

    private void showTable(String filePath, String title, String[] columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                Object[] row = Arrays.copyOf(parts, columns.length);
                model.addRow(row);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not read file: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(550, 300));
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void assignTask() {
        String username = JOptionPane.showInputDialog(this, "Enter employee username:");
        String task = JOptionPane.showInputDialog(this, "Enter task description:");

        if (username != null && task != null && !username.trim().isEmpty() && !task.trim().isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt", true))) {
                writer.write(username + " - " + task + " - " + new Date() + "\n");
                JOptionPane.showMessageDialog(this, "Task assigned successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error assigning task", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Employee username and task cannot be empty", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void manageLeaveRequests() {
        String[] columns = {"Employee", "Type", "Reason", "From Date", "To Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        List<String> rawData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("leaves.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rawData.add(line);
                String[] parts = line.split(" - ");
                if (parts.length == 6) {
                    model.addRow(parts);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not read leave file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(550, 300));

        int result = JOptionPane.showConfirmDialog(this, scrollPane, "Manage Leave Requests", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String currentStatus = (String) model.getValueAt(row, 5);
                if (!"Pending".equalsIgnoreCase(currentStatus)) {
                    JOptionPane.showMessageDialog(this, "This leave request has already been processed.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                String[] options = {"Approve", "Reject"};
                int choice = JOptionPane.showOptionDialog(this, "Update leave request status:",
                        "Leave Approval", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]);

                if (choice == 0 || choice == 1) {
                    model.setValueAt(options[choice], row, 5);
                    rawData.set(row, model.getValueAt(row, 0) + " - " +
                                     model.getValueAt(row, 1) + " - " +
                                     model.getValueAt(row, 2) + " - " +
                                     model.getValueAt(row, 3) + " - " +
                                     model.getValueAt(row, 4) + " - " +
                                     model.getValueAt(row, 5));

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("leaves.txt"))) {
                        for (String line : rawData) {
                            writer.write(line + "\n");
                        }
                        JOptionPane.showMessageDialog(this, "Leave status updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error updating leave file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a leave request to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}

class ManageEmployeesFrame extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private final String filePath = "employees.txt";

    public ManageEmployeesFrame() {
        setTitle("Manage Employees");
        setSize(600, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 248, 255)); // Alice Blue

        model = new DefaultTableModel(new String[]{"ID", "Name", "Password", "Designation"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24);
        loadEmployees();

        JScrollPane scrollPane = new JScrollPane(table);

        JButton addBtn = createButton("Add", new Color(60, 179, 113)); // Medium Sea Green
        JButton editBtn = createButton("Edit", new Color(65, 105, 225)); // Royal Blue
        JButton deleteBtn = createButton("Delete", new Color(255, 69, 0)); // Red

        addBtn.addActionListener(e -> addEmployee());
        editBtn.addActionListener(e -> editEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(240, 248, 255));
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return btn;
    }

    private void loadEmployees() {
        model.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 4) {
                    model.addRow(parts);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load employees", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEmployee() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField passField = new JTextField();
        JTextField designationField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(new JLabel("Designation:"));
        panel.add(designationField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String password = passField.getText().trim();
            String designation = designationField.getText().trim();

            if (!id.isEmpty() && !name.isEmpty() && !password.isEmpty() && !designation.isEmpty()) {
                String line = String.join(" - ", id, name, password, designation);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                    writer.write(line + "\n");
                    model.addRow(new String[]{id, name, password, designation});
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Failed to add employee", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void editEmployee() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            String id = (String) model.getValueAt(selected, 0);
            String name = (String) model.getValueAt(selected, 1);
            String password = (String) model.getValueAt(selected, 2);
            String designation = (String) model.getValueAt(selected, 3);

            JTextField idField = new JTextField(id);
            JTextField nameField = new JTextField(name);
            JTextField passField = new JTextField(password);
            JTextField designationField = new JTextField(designation);

            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            panel.add(new JLabel("ID:"));
            panel.add(idField);
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Password:"));
            panel.add(passField);
            panel.add(new JLabel("Designation:"));
            panel.add(designationField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Edit Employee", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                model.setValueAt(idField.getText().trim(), selected, 0);
                model.setValueAt(nameField.getText().trim(), selected, 1);
                model.setValueAt(passField.getText().trim(), selected, 2);
                model.setValueAt(designationField.getText().trim(), selected, 3);
                saveAll();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select an employee to edit", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteEmployee() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            model.removeRow(selected);
            saveAll();
        } else {
            JOptionPane.showMessageDialog(this, "Select an employee to delete", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String line = model.getValueAt(i, 0) + " - " +
                              model.getValueAt(i, 1) + " - " +
                              model.getValueAt(i, 2) + " - " +
                              model.getValueAt(i, 3);
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save employees", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class EmployeeDashboard extends JFrame {
    private final String username;

    public EmployeeDashboard(String username) {
        this.username = username;
        setTitle("Employee Dashboard - " + username);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 240, 245)); // Lavender blush

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));
        panel.setBackground(new Color(255, 240, 245));

        JButton markAttendance = createButton("Mark Attendance", new Color(60, 179, 113));
        JButton requestLeave = createButton("Request Leave", new Color(255, 165, 0));
        JButton viewTask = createButton("View Assigned Task", new Color(100, 149, 237));
        JButton logout = createButton("Logout", new Color(255, 69, 0));
        JButton viewLeaveStatus = createButton("View Leave Status", new Color(123, 104, 238)); // Medium Slate Blue

        markAttendance.addActionListener(e -> markAttendance());
        requestLeave.addActionListener(e -> requestLeave());
        viewTask.addActionListener(e -> viewTask());
        logout.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });
        viewLeaveStatus.addActionListener(e -> viewLeaveStatus());

        panel.add(markAttendance);
        panel.add(requestLeave);
        panel.add(viewTask);
        panel.add(viewLeaveStatus);
        panel.add(logout);

        add(panel);
        setVisible(true);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return btn;
    }

    private void markAttendance() {
        try (FileWriter fw = new FileWriter("attendance.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(username + " - Present - " + new Date() + "\n");
            JOptionPane.showMessageDialog(this, "Attendance marked successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error marking attendance", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void requestLeave() {
        JTextField reasonField = new JTextField();
        JTextField fromDateField = new JTextField();
        JTextField toDateField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Reason for Leave:"));
        panel.add(reasonField);
        panel.add(new JLabel("From Date (YYYY-MM-DD):"));
        panel.add(fromDateField);
        panel.add(new JLabel("To Date (YYYY-MM-DD):"));
        panel.add(toDateField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Request Leave", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String reason = reasonField.getText().trim();
            String fromDate = fromDateField.getText().trim();
            String toDate = toDateField.getText().trim();

            if (!reason.isEmpty() && !fromDate.isEmpty() && !toDate.isEmpty()) {
                try (FileWriter fw = new FileWriter("leaves.txt", true);
                     BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write(username + " - Leave Request - " + reason + " - " + fromDate + " - " + toDate + " - Pending\n");
                    JOptionPane.showMessageDialog(this, "Leave request submitted", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error submitting leave request", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void viewTask() {
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
            String line;
            StringBuilder taskDetails = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username)) {
                    taskDetails.append(line).append("\n");
                }
            }

            if (taskDetails.length() > 0) {
                JTextArea textArea = new JTextArea(taskDetails.toString());
                textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(350, 200));
                JOptionPane.showMessageDialog(this, scrollPane, "Assigned Tasks", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No tasks assigned yet.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading tasks file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewLeaveStatus() {
        String[] columns = {"Type", "Reason", "From Date", "To Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader("leaves.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 6 && parts[0].equals(username)) {
                    model.addRow(new Object[]{parts[1], parts[2], parts[3], parts[4], parts[5]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not read leave file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(550, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Your Leave Status", JOptionPane.INFORMATION_MESSAGE);
    }
}

