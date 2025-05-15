import java.time.LocalDate;

public class LeaveRequest {
    private String requestId;
    private String employeeId;
    private LocalDate date;
    private String reason;
    private String status; // Pending, Approved, Rejected

    public LeaveRequest(String requestId, String employeeId, LocalDate date, String reason) {
        this.requestId = requestId;
        this.employeeId = employeeId;
        this.date = date;
        this.reason = reason;
        this.status = "Pending";
    }

    public String getRequestId() {
        return requestId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toFileString() {
        return requestId + "," + employeeId + "," + date.toString() + "," + reason + "," + status;
    }

    @Override
    public String toString() {
        return toFileString(); 
    }
}
