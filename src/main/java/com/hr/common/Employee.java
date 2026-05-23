package com.hr.common;

public class Employee {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String designation;
    private String joinDate;
    private String status;      // Active, Inactive
    private double salary;

    public Employee() {}

    public Employee(String id, String name, String email, String phone,
                    String department, String designation,
                    String joinDate, String status, double salary) {
        this.id          = id;
        this.name        = name;
        this.email       = email;
        this.phone       = phone;
        this.department  = department;
        this.designation = designation;
        this.joinDate    = joinDate;
        this.status      = status;
        this.salary      = salary;
    }

    // Getters and Setters
    public String getId()                        { return id; }
    public void   setId(String id)               { this.id = id; }

    public String getName()                      { return name; }
    public void   setName(String name)           { this.name = name; }

    public String getEmail()                     { return email; }
    public void   setEmail(String email)         { this.email = email; }

    public String getPhone()                     { return phone; }
    public void   setPhone(String phone)         { this.phone = phone; }

    public String getDepartment()                { return department; }
    public void   setDepartment(String dept)     { this.department = dept; }

    public String getDesignation()               { return designation; }
    public void   setDesignation(String desig)   { this.designation = desig; }

    public String getJoinDate()                  { return joinDate; }
    public void   setJoinDate(String joinDate)   { this.joinDate = joinDate; }

    public String getStatus()                    { return status; }
    public void   setStatus(String status)       { this.status = status; }

    public double getSalary()                    { return salary; }
    public void   setSalary(double salary)       { this.salary = salary; }
}
