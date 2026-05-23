package com.hr.attendance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    /**
     * GET /api/attendance
     * Returns all attendance records.
     */
    @GetMapping
    public ResponseEntity<List<Attendance>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * POST /api/attendance/mark
     * Body: { "employeeId": "E001", "date": "2024-06-01", "status": "Present" }
     * Used by the "Mark Attendance" button/form in the UI.
     */
    @PostMapping("/mark")
    public ResponseEntity<String> markAttendance(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(service.markAttendance(request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestBody Map<String, String> request) {
        Attendance updated = service.updateStatus(id, request.get("status"));
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    /**
     * GET /api/attendance/date?date=2024-06-01
     * Returns all records for a day — populates the daily attendance table.
     */
    @GetMapping("/date")
    public ResponseEntity<List<Attendance>> getByDate(@RequestParam String date) {
        return ResponseEntity.ok(service.getByDate(date));
    }
    /**
     * GET /api/attendance/employee/E001
     * Returns attendance history for one employee — used in the employee detail view.
     */
    @GetMapping("/employee/{id}")
    public ResponseEntity<List<Attendance>> getByEmployee(@PathVariable String id) {
        return ResponseEntity.ok(service.getByEmployee(id));
    }
}
