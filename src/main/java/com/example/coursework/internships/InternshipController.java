package com.example.coursework.internships;

import com.example.coursework.student.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("internships")
public class InternshipController {
    private final InternshipService internshipService;

    @Autowired
    public InternshipController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('course:write')")
    public void addInternship(@RequestBody Internship internship,
                              @RequestParam("organization_id") Long organization_id) {
        internshipService.addInternship(organization_id, internship);
    }

    @GetMapping()
    public List<Internship> getInternships() {
        return internshipService.getInternships();
    }

    @GetMapping(path = "{internship_id}")
    public Internship getInternship(@PathVariable Long internship_id) {
        return internshipService.getInternship(internship_id);
    }

    @PutMapping(path = "check/{internship_id}")
    @PreAuthorize("hasAuthority('course:check')")
    public void checkInternship(@PathVariable Long internship_id) {
        internshipService.checkInternship(internship_id);
    }

    @PutMapping(path = "/{internship_id}")
    @PreAuthorize("hasAuthority('course:write')")
    public void updateInternship(@PathVariable Long internship_id,
                                 @RequestBody Internship internship) {
        // добавить проверку на организацию - та ли исправляет
        internshipService.updateInternship(internship_id, internship.getName(), internship.getDescription(),
                internship.getStartDate(), internship.getFinishDate());
    }

    @DeleteMapping(path = "/{internship_id}")
    public void deleteInternship(@PathVariable Long internship_id) {
        internshipService.deleteInternship(internship_id);
    }
}
