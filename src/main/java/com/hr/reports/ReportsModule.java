package com.hr.reports;

import com.hr.attendance.AttendanceStore;
import com.hr.leave.LeaveStore;
import com.hr.leave.LeaveRequest;
import com.hr.attendance.Attendance;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
class AttendanceSummary {
    private String date;
    private long present;
    private long absent;
    private long halfDay;
    private long late;
    public AttendanceSummary(String date, long present, long absent, long halfDay, long late) {
        this.date    = date;
        this.present = present;
        this.absent  = absent;
        this.halfDay = halfDay;
        this.late    = late;
    }
    public String getDate()    { return date; }
    public long getPresent()   { return present; }
    public long getAbsent()    { return absent; }
    public long getHalfDay()   { return halfDay; }
    public long getLate()      { return late; }
}
class LeaveSummary {
    private long totalRequests;
    private long approved;
    private long pending;
    private long rejected;
    private Map<String, Long> byType;
    public LeaveSummary(long totalRequests, long approved,
                        long pending, long rejected, Map<String, Long> byType) {
        this.totalRequests = totalRequests;
        this.approved      = approved;
        this.pending       = pending;
        this.rejected      = rejected;
        this.byType        = byType;
    }
    public long getTotalRequests()    { return totalRequests; }
    public long getApproved()         { return approved; }
    public long getPending()          { return pending; }
    public long getRejected()         { return rejected; }
    public Map<String, Long> getByType() { return byType; }
}
@Service
class ReportsService {
    private final AttendanceStore attendanceStore;
    private final LeaveStore leaveStore;
    ReportsService(AttendanceStore attendanceStore, LeaveStore leaveStore) {
        this.attendanceStore = attendanceStore;
        this.leaveStore      = leaveStore;
    }
    List<AttendanceSummary> getAttendanceSummary() {
        Map<String, List<Attendance>> byDate = attendanceStore.getAll().stream()
                .collect(Collectors.groupingBy(Attendance::getDate));
        return byDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    List<Attendance> records = entry.getValue();
                    return new AttendanceSummary(
                            entry.getKey(),
                            records.stream().filter(a -> a.getStatus().equals("Present")).count(),
                            records.stream().filter(a -> a.getStatus().equals("Absent")).count(),
                            records.stream().filter(a -> a.getStatus().equals("Half-Day")).count(),
                            records.stream().filter(a -> a.getStatus().equals("Late")).count()
                    );
                })
                .collect(Collectors.toList());
    }
    LeaveSummary getLeaveSummary() {
        List<LeaveRequest> all = leaveStore.getAll();
        Map<String, Long> byType = all.stream()
                .collect(Collectors.groupingBy(LeaveRequest::getLeaveType, Collectors.counting()));
        return new LeaveSummary(
                all.size(),
                all.stream().filter(l -> l.getStatus().equals("Approved")).count(),
                all.stream().filter(l -> l.getStatus().equals("Pending")).count(),
                all.stream().filter(l -> l.getStatus().equals("Rejected")).count(),
                byType
        );
    }
}
@RestController
@RequestMapping("/api/reports")
class ReportsController {
    private final ReportsService service;
    ReportsController(ReportsService service) { this.service = service; }
    @GetMapping("/attendance")
    public ResponseEntity<List<AttendanceSummary>> attendance() {
        return ResponseEntity.ok(service.getAttendanceSummary());
    }
    @GetMapping("/leave")
    public ResponseEntity<LeaveSummary> leave() {
        return ResponseEntity.ok(service.getLeaveSummary());
    }
}
