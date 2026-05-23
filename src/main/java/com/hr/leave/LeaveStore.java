package com.hr.leave;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Component
public class LeaveStore {
    private final List<LeaveRequest> requests = new ArrayList<>();
    private int idCounter = 1;
    public LeaveStore() {
        seed();
    }
    private void seed() {
        String today = LocalDate.now().toString();
        String tomorrow = LocalDate.now().plusDays(1).toString();

        requests.add(new LeaveRequest("L001", "E003", "Priya Nair",
                "Sick", today, today, "Not feeling well", "Approved"));
        requests.add(new LeaveRequest("L002", "E007", "Amit Verma",
                "Casual", tomorrow, LocalDate.now().plusDays(2).toString(),
                "Personal work", "Pending"));
        requests.add(new LeaveRequest("L003", "E009", "Sanjay Kumar",
                "Earned", "2024-05-20", "2024-05-22",
                "Family trip", "Approved"));
        requests.add(new LeaveRequest("L004", "E011", "Rohit Singh",
                "Sick", "2024-05-28", "2024-05-28",
                "Fever", "Rejected"));
    }
    public List<LeaveRequest> getAll() { return requests; }
    public String nextId() {
        return "L" + String.format("%03d", idCounter + requests.size());
    }
    public void add(LeaveRequest req) { requests.add(req); }

    public LeaveRequest findById(String id) {
        return requests.stream()
                .filter(r -> r.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }
    public int countApprovedLeavesToday(String today) {
        LocalDate todayDate = LocalDate.parse(today);
        return (int) requests.stream()
                .filter(r -> r.getStatus().equals("Approved"))
                .filter(r -> {
                    LocalDate from = LocalDate.parse(r.getFromDate());
                    LocalDate to   = LocalDate.parse(r.getToDate());
                    return !todayDate.isBefore(from) && !todayDate.isAfter(to);
                })
                .count();
    }
}
