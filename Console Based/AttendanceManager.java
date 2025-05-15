import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class AttendanceManager {
    private static final String ATT_FILE = "attendance.txt";

    // Mark attendance for given employee ID and date
    public static void markAttendance(String empId, LocalDate date) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ATT_FILE, true))) {
            bw.write(empId + "," + date.toString());
            bw.newLine();
        }
    }

    // Get attendance records for a specific date
    public static List<AttendanceRecord> getAttendanceByDate(LocalDate date) throws IOException {
        List<AttendanceRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ATT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equals(date.toString())) {
                    records.add(new AttendanceRecord(parts[0], date));
                }
            }
        }
        return records;
    }

    // Get attendance records for a specific employee
    public static List<AttendanceRecord> getAttendanceByEmployee(String empId) throws IOException {
        List<AttendanceRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ATT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(empId)) {
                    records.add(new AttendanceRecord(parts[0], LocalDate.parse(parts[1])));
                }
            }
        }
        return records;
    }
}
