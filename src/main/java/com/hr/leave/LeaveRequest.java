package com.hr.leave;

public class LeaveRequest {

    private String id;
    private String employeeId;
    private String employeeName;
    private String leaveType;     // Sick, Casual, Earned, Maternity
    private String fromDate;
    private String toDate;
    private String reason;
    private String status;        // Pending, Approved, Rejected

    public LeaveRequest() {}

    public LeaveRequest(String id, String employeeId, String employeeName,
                        String leaveType, String fromDate, String toDate,
                        String reason, String status) {
        this.id           = id;
        this.employeeId   = employeeId;
        this.employeeName = employeeName;
        this.leaveType    = leaveType;
        this.fromDate     = fromDate;
        this.toDate       = toDate;
        this.reason       = reason;
        this.status       = status;
    }

    public String getId()                       { return id; }
    public void   setId(String id)              { this.id = id; }

    public String getEmployeeId()                    { return employeeId; }
    public void   setEmployeeId(String employeeId)   { this.employeeId = employeeId; }

    public String getEmployeeName()                      { return employeeName; }
    public void   setEmployeeName(String employeeName)   { this.employeeName = employeeName; }

    public String getLeaveType()                   { return leaveType; }
    public void   setLeaveType(String leaveType)   { this.leaveType = leaveType; }

    public String getFromDate()                  { return fromDate; }
    public void   setFromDate(String fromDate)   { this.fromDate = fromDate; }

    public String getToDate()                { return toDate; }
    public void   setToDate(String toDate)   { this.toDate = toDate; }

    public String getReason()                { return reason; }
    public void   setReason(String reason)   { this.reason = reason; }

    public String getStatus()                { return status; }
    public void   setStatus(String status)   { this.status = status; }
}
