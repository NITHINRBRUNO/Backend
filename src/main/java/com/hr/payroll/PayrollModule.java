package com.hr.payroll;

import com.hr.common.Employee;
import com.hr.common.EmployeeStore;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

class Payslip {

    private String employeeId;
    private String employeeName;
    private String department;
    private String designation;
    private String month;           // e.g. "June 2024"
    private double basicSalary;
    private double hra;             // 40% of basic
    private double allowances;      // 10% of basic
    private double deductions;      // PF 12% of basic
    private double netSalary;
    public Payslip(Employee emp, String month) {
        this.employeeId   = emp.getId();
        this.employeeName = emp.getName();
        this.department   = emp.getDepartment();
        this.designation  = emp.getDesignation();
        this.month        = month;
        this.basicSalary  = emp.getSalary();
        this.hra          = Math.round(basicSalary * 0.40);
        this.allowances   = Math.round(basicSalary * 0.10);
        this.deductions   = Math.round(basicSalary * 0.12);
        this.netSalary    = basicSalary + hra + allowances - deductions;
    }
    public String getEmployeeId()     { return employeeId; }
    public String getEmployeeName()   { return employeeName; }
    public String getDepartment()     { return department; }
    public String getDesignation()    { return designation; }
    public String getMonth()          { return month; }
    public double getBasicSalary()    { return basicSalary; }
    public double getHra()            { return hra; }
    public double getAllowances()      { return allowances; }
    public double getDeductions()     { return deductions; }
    public double getNetSalary()      { return netSalary; }
}
@Service
class PayrollService {

    private final EmployeeStore store;

    PayrollService(EmployeeStore store) { this.store = store; }

    /** Returns payslips for all employees for the current month. */
    List<Payslip> getAllPayslips(String month) {
        String m = (month != null) ? month : currentMonth();
        return store.getAll().stream()
                .map(emp -> new Payslip(emp, m))
                .collect(Collectors.toList());
    }

    Payslip getPayslip(String employeeId, String month) {
        Employee emp = store.findById(employeeId);
        if (emp == null) return null;
        return new Payslip(emp, (month != null) ? month : currentMonth());
    }

    private String currentMonth() {
        return java.time.LocalDate.now()
                .getMonth().name().charAt(0)
                + java.time.LocalDate.now().getMonth().name().substring(1).toLowerCase()
                + " " + java.time.LocalDate.now().getYear();
    }
}

@RestController
@RequestMapping("/api/payroll")
class PayrollController {
    private final PayrollService service;
    PayrollController(PayrollService service) { this.service = service; }
    @GetMapping
    public ResponseEntity<List<Payslip>> getAll(
            @RequestParam(required = false) String month) {
        return ResponseEntity.ok(service.getAllPayslips(month));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<?> getOne(
            @PathVariable String employeeId,
            @RequestParam(required = false) String month) {
        Payslip payslip = service.getPayslip(employeeId, month);
        if (payslip == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(payslip);
    }
}
