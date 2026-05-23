package com.hr.attendance;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttendanceStore {

    private final List<Attendance> records = new ArrayList<>();
    private int idCounter = 1;

    public AttendanceStore() {
        seed();
    }

    private void seed() {
        String today     = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE);

        // Today's attendance
        add(new Attendance(nextId(), "E001", "Aisha Sharma",  today,     "Present"));
        add(new Attendance(nextId(), "E002", "Rahul Mehta",   today,     "Present"));
        add(new Attendance(nextId(), "E003", "Priya Nair",    today,     "Absent"));
        add(new Attendance(nextId(), "E004", "Kiran Patel",   today,     "Half-Day"));
        add(new Attendance(nextId(), "E005", "Deepak Joshi",  today,     "Present"));
        add(new Attendance(nextId(), "E006", "Sneha Rao",     today,     "Late"));
        add(new Attendance(nextId(), "E007", "Amit Verma",    today,     "Present"));
        add(new Attendance(nextId(), "E008", "Nidhi Gupta",   today,     "Present"));

        // Yesterday's attendance
        add(new Attendance(nextId(), "E001", "Aisha Sharma",  yesterday, "Present"));
        add(new Attendance(nextId(), "E002", "Rahul Mehta",   yesterday, "Absent"));
        add(new Attendance(nextId(), "E003", "Priya Nair",    yesterday, "Present"));
        add(new Attendance(nextId(), "E005", "Deepak Joshi",  yesterday, "Present"));
    }

    public List<Attendance> getAll()                { return records; }

    public void add(Attendance a)                   { records.add(a); }

    public String nextId() {
        return "ATT" + String.format("%03d", idCounter++);
    }

    public int countPresentOnDate(String date) {
        return (int) records.stream()
                .filter(a -> a.getDate().equals(date) &&
                        (a.getStatus().equals("Present") || a.getStatus().equals("Late")))
                .count();
    }

    public List<Attendance> getByDate(String date) {
        return records.stream()
                .filter(a -> a.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<Attendance> getByEmployee(String employeeId) {
        return records.stream()
                .filter(a -> a.getEmployeeId().equalsIgnoreCase(employeeId))
                .collect(Collectors.toList());
    }

    public Attendance findById(String id) {
        return records.stream()
                .filter(a -> a.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public boolean alreadyMarked(String employeeId, String date) {
        return records.stream()
                .anyMatch(a -> a.getEmployeeId().equalsIgnoreCase(employeeId)
                        && a.getDate().equals(date));
    }
}
