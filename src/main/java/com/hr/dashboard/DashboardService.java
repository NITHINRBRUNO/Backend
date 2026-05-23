package com.hr.dashboard;

import com.hr.attendance.AttendanceStore;
import com.hr.common.Employee;
import com.hr.common.EmployeeStore;
import com.hr.leave.LeaveStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DashboardService {

    private final EmployeeStore employeeStore;
    private final AttendanceStore attendanceStore;
    private final LeaveStore leaveStore;

    public DashboardService(EmployeeStore employeeStore,
                            AttendanceStore attendanceStore,
                            LeaveStore leaveStore) {
        this.employeeStore   = employeeStore;
        this.attendanceStore = attendanceStore;
        this.leaveStore      = leaveStore;
    }

    /**
     * Builds the four stat cards shown on the dashboard.
     */
    public DashboardSummary getSummary() {
        List<Employee> all = employeeStore.getAll();
        int total        = all.size();
        String today     = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        int presentToday = attendanceStore.countPresentOnDate(today);
        int onLeave      = leaveStore.countApprovedLeavesToday(today);

        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        int newJoiners = (int) all.stream()
                .filter(e -> {
                    LocalDate joinDate = LocalDate.parse(e.getJoinDate());
                    return !joinDate.isBefore(thirtyDaysAgo);
                })
                .count();
        return new DashboardSummary(total, presentToday, onLeave, newJoiners);
    }
}
