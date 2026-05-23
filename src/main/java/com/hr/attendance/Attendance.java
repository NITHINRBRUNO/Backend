package com.hr.attendance;

public class Attendance {

    private String id;
    private String employeeId;
    private String employeeName;
    private String date;
    private String status;       // Present, Absent, Half-Day, Late

    public Attendance() {}

    public Attendance(String id, String employeeId, String employeeName,
                      String date, String status) {
        this.id           = id;
        this.employeeId   = employeeId;
        this.employeeName = employeeName;
        this.date         = date;
        this.status       = status;
    }

    public String getId()             { return id; }
    public void   setId(String id)    { this.id = id; }

    public String getEmployeeId()                   { return employeeId; }
    public void   setEmployeeId(String employeeId)  { this.employeeId = employeeId; }

    public String getEmployeeName()                     { return employeeName; }
    public void   setEmployeeName(String employeeName)  { this.employeeName = employeeName; }

    public String getDate()               { return date; }
    public void   setDate(String date)    { this.date = date; }

    public String getStatus()                 { return status; }
    public void   setStatus(String status)    { this.status = status; }
}
