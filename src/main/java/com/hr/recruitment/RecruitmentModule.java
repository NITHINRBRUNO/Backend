package com.hr.recruitment;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Candidate {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String position;       // Job role applied for
    private String department;
    private String appliedDate;
    private String status;         // Applied, Screening, Interview, Offered, Hired, Rejected

    public Candidate() {}

    public Candidate(String id, String name, String email, String phone,
                     String position, String department,
                     String appliedDate, String status) {
        this.id          = id;
        this.name        = name;
        this.email       = email;
        this.phone       = phone;
        this.position    = position;
        this.department  = department;
        this.appliedDate = appliedDate;
        this.status      = status;
    }

    public String getId()               { return id; }
    public void   setId(String id)      { this.id = id; }
    public String getName()             { return name; }
    public void   setName(String name)  { this.name = name; }
    public String getEmail()            { return email; }
    public void   setEmail(String e)    { this.email = e; }
    public String getPhone()            { return phone; }
    public void   setPhone(String p)    { this.phone = p; }
    public String getPosition()         { return position; }
    public void   setPosition(String p) { this.position = p; }
    public String getDepartment()               { return department; }
    public void   setDepartment(String d)       { this.department = d; }
    public String getAppliedDate()              { return appliedDate; }
    public void   setAppliedDate(String d)      { this.appliedDate = d; }
    public String getStatus()                   { return status; }
    public void   setStatus(String status)      { this.status = status; }
}
@Service
class RecruitmentService {

    private final List<Candidate> candidates = new ArrayList<>();
    private int idCounter = 5;

    RecruitmentService() {
        seed();
    }

    private void seed() {
        candidates.add(new Candidate("R001", "Arjun Tiwari",   "arjun@mail.com",  "9800000001",
                "Backend Developer",  "Engineering", "2024-05-10", "Interview"));
        candidates.add(new Candidate("R002", "Divya Menon",    "divya@mail.com",  "9800000002",
                "UI/UX Designer",     "Marketing",   "2024-05-12", "Screening"));
        candidates.add(new Candidate("R003", "Farhan Sheikh",  "farhan@mail.com", "9800000003",
                "Finance Manager",    "Finance",     "2024-05-15", "Applied"));
        candidates.add(new Candidate("R004", "Geeta Desai",    "geeta@mail.com",  "9800000004",
                "HR Recruiter",       "HR",          "2024-05-18", "Offered"));
    }

    List<Candidate> getAll() { return candidates; }

    String addCandidate(Candidate c) {
        c.setId("R" + String.format("%03d", idCounter++));
        if (c.getStatus() == null) c.setStatus("Applied");
        candidates.add(c);
        return "Candidate added. ID: " + c.getId();
    }

    String updateStatus(String id, String status) {
        Candidate c = candidates.stream()
                .filter(x -> x.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
        if (c == null) return "Candidate not found: " + id;
        c.setStatus(status);
        return "Status updated to: " + status;
    }
}

@RestController
@RequestMapping("/api/recruitment")
class RecruitmentController {
    private final RecruitmentService service;
    RecruitmentController(RecruitmentService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<Candidate>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    @PostMapping
    public ResponseEntity<String> add(@RequestBody Candidate candidate) {
        return ResponseEntity.ok(service.addCandidate(candidate));
    }

    
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable String id,
            @RequestParam(required = false) String status,
            @RequestBody(required = false) Map<String, String> body) {
        String nextStatus = status != null ? status : (body != null ? body.get("status") : null);
        if (nextStatus == null || nextStatus.isBlank()) {
            return ResponseEntity.badRequest().body("Missing required field: status");
        }
        return ResponseEntity.ok(service.updateStatus(id, normalizeStatus(nextStatus)));
    }

    private String normalizeStatus(String status) {
        String normalized = status.trim().toLowerCase().replace('_', ' ');
        return java.util.Arrays.stream(normalized.split("\\s+"))
                .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1))
                .collect(java.util.stream.Collectors.joining(" "));
    }
}
