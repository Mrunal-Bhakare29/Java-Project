import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class LeaveManager {
    private static final String LEAVE_FILE = "leaves.txt";

    // Apply for leave (append a new request)
    public static void applyLeave(LeaveRequest lr) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LEAVE_FILE, true))) {
            bw.write(lr.toFileString()); // ✅ Use proper CSV format
            bw.newLine();
        }
    }

    // Get all leave requests
    public static List<LeaveRequest> getAllRequests() throws IOException {
        List<LeaveRequest> requests = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(LEAVE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 5) {
                    try {
                        LeaveRequest lr = new LeaveRequest(
                            parts[0].trim(),
                            parts[1].trim(),
                            LocalDate.parse(parts[2].trim()),
                            parts[3].trim()
                        );
                        lr.setStatus(parts[4].trim());
                        requests.add(lr);
                    } catch (Exception e) {
                        System.out.println("Skipping invalid line: " + line);
                    }
                }
            }
        }
        return requests;
    }

    // Update the status of a leave request (approve/reject)
    public static void updateLeaveStatus(String requestId, String status) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(LEAVE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 5 && parts[0].equals(requestId)) {
                    parts[4] = status; // Update status
                    lines.add(String.join(",", parts));
                } else {
                    lines.add(line);
                }
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LEAVE_FILE))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        }
    }
}
