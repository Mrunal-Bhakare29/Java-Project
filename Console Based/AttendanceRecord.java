import java.time.LocalDate;

public class AttendanceRecord {
    private String employeeId;
    private LocalDate date;

    public AttendanceRecord(String employeeId, LocalDate date) {
        this.employeeId = employeeId;
        this.date = date;
    }

    public String getEmployeeId() { return employeeId; }
    public LocalDate getDate() { return date; }

    // Convert to CSV line: employeeId,date
    @Override
    public String toString() {
        return employeeId + "," + date.toString();
    }
}
