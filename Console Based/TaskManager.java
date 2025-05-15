import java.io.*;
import java.time.LocalDate;
import java.util.*;


public class TaskManager {
    private static final String TASK_FILE = "tasks.txt";

    // Add a new task (append to file)
    public static void addTask(Task task) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TASK_FILE, true))) {
            bw.write(task.toString());
            bw.newLine();
        }
    }

    // Get tasks assigned to a specific employee
    public static List<Task> getTasksByEmployee(String empId) throws IOException {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TASK_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].equals(empId)) {
                    Task t = new Task(parts[0], parts[1], LocalDate.parse(parts[2]), parts[4]);
                    t.setStatus(parts[3]);
                    tasks.add(t);
                }
            }
        }
        return tasks;
    }

    // Update the status of a task (mark as completed)
    public static void updateTaskStatus(String taskId, String empId, String status) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TASK_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(taskId) && parts[4].equals(empId)) {
                    parts[3] = status;  // Update status field
                    lines.add(String.join(",", parts));
                } else {
                    lines.add(line);
                }
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TASK_FILE))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        }
    }

    // Get all tasks in the system
    public static List<Task> getAllTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TASK_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    Task t = new Task(parts[0], parts[1], LocalDate.parse(parts[2]), parts[4]);
                    t.setStatus(parts[3]);
                    tasks.add(t);
                }
            }
        }
        return tasks;
    }
}
