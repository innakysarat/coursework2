package com.example.coursework.organizations;

import com.example.coursework.student.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("organizations")
public class OrganizationController {

    private final OrganizaitionService organizaitionService;

    @Autowired
    public OrganizationController(OrganizaitionService organizaitionService) {
        this.organizaitionService = organizaitionService;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('organization:write')")
    public void addOrganization(@RequestBody Organization organization) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (username != null) {
            organizaitionService.addOrganization(username, organization);
        }// else {
        //     throw new IllegalStateException("User must login");
        //  }
    }

    @GetMapping(path = "/{organization_id}")
    public Organization getOrganization(
            @PathVariable Long organization_id) {
        // username correct & can get &
        return organizaitionService.getOrganization(organization_id);
    }

    @GetMapping
    public List<Organization> getOrganizations() {
        return organizaitionService.getOrganizations();
    }

    @GetMapping(path = "/leaders")
    //@PreAuthorize("hasAuthority('organization:read')")
    public Set<User> getLeadersOfOrganization(
            @RequestParam("organization") Long organization_id
    ) {
        return organizaitionService.getLeadersOfOrganization(organization_id);
    }

    @PutMapping("/{organization_id}")
    @PreAuthorize("hasAuthority('organization:read')")
    public void addLeadersToOrganization(
            @PathVariable Long organization_id,
            @RequestParam("username") String username
    ) {
        Organization organization = organizaitionService.getOrganization(organization_id);
        organizaitionService.addOrganization(username, organization);
    }

    /*  @PutMapping("/{organization_id}/internships/{internship_id}")
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
     }*/
    @DeleteMapping(path = "/{organization_id}")
    public void deleteOrganization(@PathVariable Long organization_id) {
    organizaitionService.deleteOrganization(organization_id);
    }
}
