package com.example.coursework.organizations;

import com.example.coursework.internships.Internship;
import com.example.coursework.internships.InternshipService;
import com.example.coursework.student.StudentService;
import com.example.coursework.student.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("organizations")
public class OrganizationController {

    private final OrganizaitionService organizaitionService;
    private final StudentService studentService;
    private final InternshipService internshipService;

    @Autowired
    public OrganizationController(OrganizaitionService organizaitionService, StudentService studentService, InternshipService internshipService) {
        this.organizaitionService = organizaitionService;
        this.studentService = studentService;
        this.internshipService = internshipService;
    }

    @PostMapping(path = "/registration")
    @PreAuthorize("hasAuthority('organization:write')")
    public void addOrganization(@RequestBody Organization organization) {
        organizaitionService.addOrganization(organization);
    }

    @GetMapping(path = "/{organization_id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_LEADER')")
    public Organization getOrganization(
            @PathVariable Long organization_id) {
        return organizaitionService.getOrganization(organization_id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('organization:read')")
    public List<Organization> getOrganizations() {
        return organizaitionService.getOrganizations();
    }

    @GetMapping(path = "/{organization_id}/leaders")
    @PreAuthorize("hasAuthority('organization:read')")
    public Set<User> getLeadersOfOrganization(
            @PathVariable Long organization_id
    ) {
        return organizaitionService.getLeadersOfOrganization(organization_id);
    }

    @PutMapping("/{organization_id}/leaders/{user_id}")
    @PreAuthorize("hasAuthority('organization:read')")
    public Organization addOrganizationToLeader(
            @PathVariable Long organization_id,
            @PathVariable Integer user_id
    ) {
        Organization organization = organizaitionService.getOrganization(organization_id);
        User user = studentService.getUser(user_id);
        organization.addLeader(user);
        return organizaitionService.addOrganization(organization);
    }

    @PutMapping("/{organization_id}/internships/{internship_id}")
    @PreAuthorize("hasAuthority('organization:write')")
    public Internship addInternshipToOrganization(
            @PathVariable Long organization_id,
            @PathVariable Long internship_id
    ) {
        Organization organization = organizaitionService.getOrganization(organization_id);
        Internship internship = internshipService.getInternship(internship_id);
        organization.addInternship(internship);
        internship.assignOrganization(organization);
        return internshipService.addInternship(internship);
    }


}
