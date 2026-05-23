package com.hr.attendance;
import com.hr.common.Employee;
import com.hr.common.EmployeeStore;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
@Service
public class AttendanceService {
    private final AttendanceStore store;
    private final EmployeeStore employeeStore;
    public AttendanceService(AttendanceStore store, EmployeeStore employeeStore) {
        this.store         = store;
        this.employeeStore = employeeStore;
    }
   
    public String markAttendance(Map<String, String> request) {
        String employeeId = request.get("employeeId");
        String date       = request.get("date");
        String status     = request.get("status");

        if (employeeId == null || date == null || status == null) {
            return "Missing required fields: employeeId, date, status";
        }
        if (store.alreadyMarked(employeeId, date)) {
            return "Attendance already marked for " + employeeId + " on " + date;
        }
        Employee emp = employeeStore.findById(employeeId);
        String name  = emp != null ? emp.getName() : "Unknown";
        Attendance record = new Attendance(
                store.nextId(), employeeId, name, date, status);
        store.add(record);
        return "Attendance marked successfully";
    }
    
    public List<Attendance> getByDate(String date) {
        return store.getByDate(date);
    }

    public List<Attendance> getByEmployee(String employeeId) {
        return store.getByEmployee(employeeId);
    }

    public List<Attendance> getAll() {
        return store.getAll();
    }

    public Attendance updateStatus(String id, String status) {
        Attendance record = store.findById(id);
        if (record == null || status == null || status.isBlank()) return null;
        record.setStatus(status);
        return record;
    }
}
