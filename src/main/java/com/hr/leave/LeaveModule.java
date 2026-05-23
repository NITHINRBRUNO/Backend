package com.hr.leave;

import com.hr.common.Employee;
import com.hr.common.EmployeeStore;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@Service
class LeaveService {
    private final LeaveStore store;
    private final EmployeeStore employeeStore;
    LeaveService(LeaveStore store, EmployeeStore employeeStore) {
        this.store         = store;
        this.employeeStore = employeeStore;
    }
    /** Applies a new leave — called when employee submits the leave form. */
    String applyLeave(Map<String, String> body) {
        String employeeId = body.get("employeeId");
        Employee emp      = employeeStore.findById(employeeId);
        if (emp == null) return "Employee not found: " + employeeId;
        LeaveRequest req = new LeaveRequest(
                store.nextId(),
                employeeId,
                emp.getName(),
                body.get("leaveType"),
                body.get("fromDate"),
                body.get("toDate"),
                body.get("reason"),
                "Pending"
        );
        store.add(req);
        return "Leave applied successfully. ID: " + req.getId();
    }
    /** Admin approves a leave — triggered by the Approve button. */
    String approveLeave(String id) {
        LeaveRequest req = store.findById(id);
        if (req == null) return "Leave request not found: " + id;
        req.setStatus("Approved");
        return "Leave approved";
    }

    /** Admin rejects a leave — triggered by the Reject button. */
    String rejectLeave(String id) {
        LeaveRequest req = store.findById(id);
        if (req == null) return "Leave request not found: " + id;
        req.setStatus("Rejected");
        return "Leave rejected";
    }

    List<LeaveRequest> getAll() { return store.getAll(); }
}

@RestController
@RequestMapping("/api/leave")
class LeaveController {

    private final LeaveService service;

    LeaveController(LeaveService service) { this.service = service; }

    /**
     * POST /api/leave/apply
     * Body: { "employeeId":"E002", "leaveType":"Sick",
     *         "fromDate":"2024-06-05", "toDate":"2024-06-05", "reason":"..." }
     */
    @PostMapping("/apply")
    public ResponseEntity<String> apply(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(service.applyLeave(body));
    }

    /**
     * PUT /api/leave/L002/approve
     * Admin clicks Approve button on the leave table row.
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approve(@PathVariable String id) {
        return ResponseEntity.ok(service.approveLeave(id));
    }

    /**
     * PUT /api/leave/L002/reject
     * Admin clicks Reject button on the leave table row.
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<String> reject(@PathVariable String id) {
        return ResponseEntity.ok(service.rejectLeave(id));
    }

    /**
     * GET /api/leave
     * Loads the full leave table.
     */
    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
