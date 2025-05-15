import java.io.*;
import java.util.*;

public class EmployeeManager {
    private static final String EMP_FILE = "employees.txt";

    // Add a new employee (append to file)
    public static void addEmployee(Employee emp) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMP_FILE, true))) {
            bw.write(emp.toString());
            bw.newLine();
        }
    }

    // Edit existing employee by ID
    public static void editEmployee(String id, Employee updatedEmp) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[1].equals(id)) {
                    // Replace with updated employee record
                    lines.add(updatedEmp.toString());
                } else {
                    lines.add(line);
                }
            }
        }
        // Write all lines back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMP_FILE))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        }
    }

    // Delete an employee by ID
    public static void deleteEmployee(String id) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (!parts[1].equals(id)) {
                    lines.add(line);
                }
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMP_FILE))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        }
    }

    // Get a list of all employees
    public static List<Employee> getAllEmployees() throws IOException {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    Employee emp = new Employee(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    employees.add(emp);
                }
            }
        }
        return employees;
    }

    // Login by ID and password
    public static Employee login(String id, String password) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[1].equals(id) && parts[2].equals(password)) {
                    return new Employee(parts[0], parts[1], parts[2], parts[3], parts[4]);
                }
            }
        }
        return null; // Not found or invalid credentials
    }
}
