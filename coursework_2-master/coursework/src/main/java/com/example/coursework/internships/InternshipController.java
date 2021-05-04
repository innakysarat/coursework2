package com.example.coursework.internships;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("internships")
public class InternshipController {
    private final InternshipService internshipService;

    @Autowired
    public InternshipController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    @PostMapping(path = "/add")
    @PreAuthorize("hasAuthority('course:write')")
    public void addInternship(@RequestBody Internship internship) {
        internshipService.addInternship(internship);
    }

    @GetMapping(path = "/{internship_id}")
    @PreAuthorize("hasAuthority('course:read')")
    public Internship getInternship(@PathVariable Long internship_id) {
        return internshipService.getInternship(internship_id);
    }
}
