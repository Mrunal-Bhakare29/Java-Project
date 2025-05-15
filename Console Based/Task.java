import java.time.LocalDate;

public class Task {
    private String taskId;
    private String description;
    private LocalDate dueDate;
    private String status; // "Pending" or "Completed"
    private String assignedEmployeeId;

    public Task(String taskId, String description, LocalDate dueDate, String assignedEmployeeId) {
        this.taskId = taskId;
        this.description = description;
        this.dueDate = dueDate;
        this.status = "Pending";
        this.assignedEmployeeId = assignedEmployeeId;
    }

    // Getters
    public String getTaskId() { return taskId; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public String getStatus() { return status; }
    public String getAssignedEmployeeId() { return assignedEmployeeId; }

    // Set status (to "Completed")
    public void setStatus(String status) { this.status = status; }

    // Convert to CSV line: taskId,description,dueDate,status,assignedEmployeeId
    @Override
    public String toString() {
        return taskId + "," + description + "," + dueDate.toString() + "," + status + "," + assignedEmployeeId;
    }
}
