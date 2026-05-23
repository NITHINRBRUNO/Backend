package com.hr.common;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Single source of truth for employee data.
 * All modules inject this component instead of maintaining their own list.
 */
@Component
public class EmployeeStore {
    private final List<Employee> employees = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(13);
    public EmployeeStore() {
        seed();
    }
    private void seed() {
        employees.add(new Employee("E001", "Aisha Sharma",    "aisha@company.com",   "9876543210", "Engineering",  "Senior Developer",   "2021-03-15", "Active", 95000));
        employees.add(new Employee("E002", "Rahul Mehta",     "rahul@company.com",   "9876543211", "Engineering",  "Junior Developer",   "2022-07-01", "Active", 65000));
        employees.add(new Employee("E003", "Priya Nair",      "priya@company.com",   "9876543212", "HR",           "HR Manager",         "2020-01-10", "Active", 80000));
        employees.add(new Employee("E004", "Kiran Patel",     "kiran@company.com",   "9876543213", "Finance",      "Finance Analyst",    "2021-09-20", "Active", 72000));
        employees.add(new Employee("E005", "Deepak Joshi",    "deepak@company.com",  "9876543214", "Marketing",    "Marketing Lead",     "2019-06-05", "Active", 88000));
        employees.add(new Employee("E006", "Sneha Rao",       "sneha@company.com",   "9876543215", "Engineering",  "DevOps Engineer",    "2022-11-15", "Active", 78000));
        employees.add(new Employee("E007", "Amit Verma",      "amit@company.com",    "9876543216", "Sales",        "Sales Executive",    "2023-01-03", "Active", 55000));
        employees.add(new Employee("E008", "Nidhi Gupta",     "nidhi@company.com",   "9876543217", "HR",           "HR Executive",       "2023-04-18", "Active", 52000));
        employees.add(new Employee("E009", "Sanjay Kumar",    "sanjay@company.com",  "9876543218", "Finance",      "Accountant",         "2020-08-12", "Active", 61000));
        employees.add(new Employee("E010", "Lakshmi Iyer",    "lakshmi@company.com", "9876543219", "Marketing",    "Content Strategist", "2024-02-01", "Active", 59000));
        employees.add(new Employee("E011", "Rohit Singh",     "rohit@company.com",   "9876543220", "Engineering",  "QA Engineer",        "2023-06-10", "Active", 67000));
        employees.add(new Employee("E012", "Meera Pillai",    "meera@company.com",   "9876543221", "Sales",        "Sales Manager",      "2018-03-22", "Active", 91000));
    }
    public List<Employee> getAll() {
        return employees;
    }
    public Employee findById(String id) {
        return employees.stream()
                .filter(e -> e.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
    public void add(Employee employee) {
        employees.add(employee);
    }

    public String nextId() {
        return "E" + String.format("%03d", idCounter.getAndIncrement());
    }

    public boolean replace(String id, Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equalsIgnoreCase(id)) {
                employees.set(i, employee);
                return true;
            }
        }
        return false;
    }

    public boolean delete(String id) {
        return employees.removeIf(e -> e.getId().equalsIgnoreCase(id));
    }
}
