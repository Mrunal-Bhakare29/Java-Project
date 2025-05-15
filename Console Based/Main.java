import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static final String ADMIN_USER = "admin";   
    private static final String ADMIN_PASS = "admin123";

    public static void main(String[] args) {
        while (true) {
            System.out.println("=== Employee Management System ===");
            System.out.println("1. Admin Login");
            System.out.println("2. Employee Login");
            System.out.println("3. Exit");
            System.out.print("Select option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": adminLogin(); break;
                case "2": employeeLogin(); break;
                case "3":
                    System.out.println("Exiting... Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void adminLogin() {
        System.out.print("Enter admin username: ");
        String user = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String pass = scanner.nextLine();
        if (ADMIN_USER.equals(user) && ADMIN_PASS.equals(pass)) {
            adminMenu();
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private static void employeeLogin() {
        System.out.print("Enter employee ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter employee password: ");
        String pass = scanner.nextLine();
        try {
            Employee emp = EmployeeManager.login(id, pass);
            if (emp != null) {
                employeeMenu(emp);
            } else {
                System.out.println("Invalid ID or password.");
            }
        } catch (IOException e) {
            System.out.println("Error reading employee data: " + e.getMessage());
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Employee");
            System.out.println("2. Edit Employee");
            System.out.println("3. Delete Employee");
            System.out.println("4. View All Employees");
            System.out.println("5. View Leave Requests");
            System.out.println("6. Approve/Reject Leave");
            System.out.println("7. View Attendance Report");
            System.out.println("8. Assign Task");
            System.out.println("9. View All Tasks");
            System.out.println("10. Logout");
            System.out.print("Select option: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1": addEmployee(); break;
                    case "2": editEmployee(); break;
                    case "3": deleteEmployee(); break;
                    case "4": viewAllEmployees(); break;
                    case "5": viewLeaveRequests(); break;
                    case "6": approveRejectLeave(); break;
                    case "7": viewAttendanceReport(); break;
                    case "8": assignTask(); break;
                    case "9": viewAllTasks(); break;
                    case "10": return;
                    default: System.out.println("Invalid choice."); break;
                }
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }
    }

    private static void employeeMenu(Employee emp) {
        while (true) {
            System.out.println("\n--- Employee Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. Mark Attendance");
            System.out.println("3. Apply Leave");
            System.out.println("4. View Assigned Tasks");
            System.out.println("5. Mark Task Completed");
            System.out.println("6. Logout");
            System.out.print("Select option: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1": viewProfile(emp); break;
                    case "2": markAttendance(emp); break;
                    case "3": applyLeave(emp); break;
                    case "4": viewAssignedTasks(emp); break;
                    case "5": completeTask(emp); break;
                    case "6": return;
                    default: System.out.println("Invalid choice."); break;
                }
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }
    }

    // --- Admin action methods ---

    private static void addEmployee() throws IOException {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();
        System.out.print("Department: ");
        String dept = scanner.nextLine();
        System.out.print("Contact: ");
        String contact = scanner.nextLine();
        Employee emp = new Employee(name, id, pass, dept, contact);
        EmployeeManager.addEmployee(emp);
        System.out.println("Employee added.");
    }

    private static void editEmployee() throws IOException {
        System.out.print("Enter employee ID to edit: ");
        String id = scanner.nextLine();
        System.out.print("New Name: ");
        String name = scanner.nextLine();
        System.out.print("New Password: ");
        String pass = scanner.nextLine();
        System.out.print("New Department: ");
        String dept = scanner.nextLine();
        System.out.print("New Contact: ");
        String contact = scanner.nextLine();
        Employee updated = new Employee(name, id, pass, dept, contact);
        EmployeeManager.editEmployee(id, updated);
        System.out.println("Employee updated.");
    }

    private static void deleteEmployee() throws IOException {
        System.out.print("Enter employee ID to delete: ");
        String id = scanner.nextLine();
        EmployeeManager.deleteEmployee(id);
        System.out.println("Employee deleted (if existed).");
    }

    private static void viewAllEmployees() throws IOException {
        List<Employee> employees = EmployeeManager.getAllEmployees();
        System.out.println("All Employees:");
        for (Employee e : employees) {
            System.out.println(e.getId() + " - " + e.getName() + " (" + e.getDepartment() + ")");
        }
    }

    private static void viewLeaveRequests() throws IOException {
        List<LeaveRequest> requests = LeaveManager.getAllRequests();
        System.out.println("Leave Requests:");
        for (LeaveRequest lr : requests) {
            System.out.println(lr.getRequestId() + ": " + lr.getEmployeeId() +
                " on " + lr.getDate() + " Reason: " + lr.getReason() +
                " [Status: " + lr.getStatus() + "]");
        }
    }

    private static void approveRejectLeave() throws IOException {
        viewLeaveRequests();
        System.out.print("Enter request ID to process: ");
        String rid = scanner.nextLine();
        System.out.print("Enter 'A' to approve or 'R' to reject: ");
        String decision = scanner.nextLine();
        if (decision.equalsIgnoreCase("A")) {
            LeaveManager.updateLeaveStatus(rid, "Approved");
            System.out.println("Leave approved.");
        } else if (decision.equalsIgnoreCase("R")) {
            LeaveManager.updateLeaveStatus(rid, "Rejected");
            System.out.println("Leave rejected.");
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void viewAttendanceReport() throws IOException {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);
        List<AttendanceRecord> records = AttendanceManager.getAttendanceByDate(date);
        System.out.println("Attendance on " + date + ":");
        for (AttendanceRecord ar : records) {
            System.out.println(" - " + ar.getEmployeeId());
        }
    }

    private static void assignTask() throws IOException {
        System.out.print("Task ID: ");
        String tid = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Due date (YYYY-MM-DD): ");
        LocalDate due = LocalDate.parse(scanner.nextLine());
        System.out.print("Assign to employee ID: ");
        String empId = scanner.nextLine();
        Task t = new Task(tid, desc, due, empId);
        TaskManager.addTask(t);
        System.out.println("Task assigned.");
    }

    private static void viewAllTasks() throws IOException {
        List<Task> tasks = TaskManager.getAllTasks();
        System.out.println("All Tasks:");
        for (Task t : tasks) {
            System.out.println(t.getTaskId() + ": " + t.getDescription() +
                " (Due: " + t.getDueDate() + ") [Status: " + t.getStatus() +
                "] Assigned to: " + t.getAssignedEmployeeId());
        }
    }

    // --- Employee action methods ---

    private static void viewProfile(Employee emp) {
        System.out.println("Profile:");
        System.out.println("Name      : " + emp.getName());
        System.out.println("ID        : " + emp.getId());
        System.out.println("Department: " + emp.getDepartment());
        System.out.println("Contact   : " + emp.getContact());
    }

    private static void markAttendance(Employee emp) throws IOException {
        LocalDate today = LocalDate.now();
        AttendanceManager.markAttendance(emp.getId(), today);
        System.out.println("Attendance marked for " + today + ".");
    }

    private static void applyLeave(Employee emp) throws IOException {
        System.out.print("Leave Request ID: ");
        String rid = scanner.nextLine();
        System.out.print("Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Reason: ");
        String reason = scanner.nextLine();
        LeaveRequest lr = new LeaveRequest(rid, emp.getId(), date, reason);
        LeaveManager.applyLeave(lr);
        System.out.println("Leave request submitted.");
    }

    private static void viewAssignedTasks(Employee emp) throws IOException {
        List<Task> tasks = TaskManager.getTasksByEmployee(emp.getId());
        System.out.println("Your Tasks:");
        for (Task t : tasks) {
            System.out.println(t.getTaskId() + ": " + t.getDescription() +
                " (Due: " + t.getDueDate() + ") [Status: " + t.getStatus() + "]");
        }
    }

    private static void completeTask(Employee emp) throws IOException {
        viewAssignedTasks(emp);
        System.out.print("Enter Task ID to mark completed: ");
        String tid = scanner.nextLine();
        TaskManager.updateTaskStatus(tid, emp.getId(), "Completed");
        System.out.println("Task marked as completed.");
    }
}
