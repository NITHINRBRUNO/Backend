package com.hr.onboarding;
import com.hr.common.Employee;
import com.hr.common.EmployeeStore;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.util.*;
class OnboardingSession {
    private String sessionId;
    private int    currentStep;       // 1=Personal, 2=Job, 3=Salary, 4=Documents
    private Map<String, String> data; // accumulates fields across steps
    public OnboardingSession(String sessionId) {
        this.sessionId   = sessionId;
        this.currentStep = 1;
        this.data        = new HashMap<>();
    }
    public String getSessionId()                    { return sessionId; }
    public int    getCurrentStep()                  { return currentStep; }
    public void   setCurrentStep(int step)          { this.currentStep = step; }
    public Map<String, String> getData()            { return data; }
    public void mergeData(Map<String, String> more) { data.putAll(more); }
}
@Service
class OnboardingService {

    // Temp session store: sessionId → OnboardingSession
    private final Map<String, OnboardingSession> sessions = new HashMap<>();
    private final EmployeeStore employeeStore;
    OnboardingService(EmployeeStore employeeStore) {
        this.employeeStore = employeeStore;
    }
    Map<String, Object> saveStep(Map<String, String> body) {
        String sessionId = body.get("sessionId");

        // Create new session if first step
        if (sessionId == null || !sessions.containsKey(sessionId)) {
            sessionId = UUID.randomUUID().toString().substring(0, 8);
            sessions.put(sessionId, new OnboardingSession(sessionId));
        }
        OnboardingSession session = sessions.get(sessionId);
        // Merge this step's data (excluding control fields)
        Map<String, String> stepData = new HashMap<>(body);
        stepData.remove("sessionId");
        stepData.remove("step");
        session.mergeData(stepData);
        session.setCurrentStep(session.getCurrentStep() + 1);

        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", sessionId);
        response.put("nextStep", session.getCurrentStep());
        response.put("message", "Step saved successfully");
        return response;
    }

    Map<String, Object> complete(String sessionId) {
        OnboardingSession session = sessions.get(sessionId);
        if (session == null) {
            return Map.of("error", "Session not found: " + sessionId);
        }

        Map<String, String> d = session.getData();
        String newId = employeeStore.nextId();
        double salary;
        try {
            salary = Double.parseDouble(d.getOrDefault("salary", "50000"));
        } catch (NumberFormatException ex) {
            return Map.of("error", "Invalid salary: " + d.get("salary"));
        }

        Employee emp = new Employee(
                newId,
                d.getOrDefault("name",        "Unknown"),
                d.getOrDefault("email",       "unknown@company.com"),
                d.getOrDefault("phone",       "0000000000"),
                d.getOrDefault("department",  "General"),
                d.getOrDefault("designation", "Employee"),
                d.getOrDefault("joinDate",    java.time.LocalDate.now().toString()),
                "Active",
                salary
        );

        employeeStore.add(emp);
        sessions.remove(sessionId);

        return Map.of(
                "message",    "Employee onboarded successfully",
                "employeeId", newId,
                "name",       emp.getName()
        );
    }

    /** Returns current state of an onboarding session — used to resume or preview. */
    OnboardingSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }
}
@RestController
@RequestMapping("/api/onboarding")
class OnboardingController {

    private final OnboardingService service;

    OnboardingController(OnboardingService service) { this.service = service; }

    /**
     * POST /api/onboarding/step
     * Called after each step of the multi-step form.
     * Body includes "sessionId" (empty on step 1) + this step's form fields.
     *
     * Step 1 body: { "name": "...", "email": "...", "phone": "..." }
     * Step 2 body: { "sessionId": "abc123", "department": "...", "designation": "...", "joinDate": "..." }
     * Step 3 body: { "sessionId": "abc123", "salary": "65000" }
     */
    @PostMapping("/step")
    public ResponseEntity<Map<String, Object>> step(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(service.saveStep(body));
    }

    /**
     * POST /api/onboarding/complete
     * Body: { "sessionId": "abc123" }
     * Called on the final "Submit" button — creates the employee record.
     */
    @PostMapping("/complete")
    public ResponseEntity<Map<String, Object>> complete(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(service.complete(body.get("sessionId")));
    }

    /**
     * GET /api/onboarding/abc123
     * Returns current session state — used to show a preview or resume a session.
     */
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSession(@PathVariable String sessionId) {
        OnboardingSession session = service.getSession(sessionId);
        if (session == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(session);
    }
}
