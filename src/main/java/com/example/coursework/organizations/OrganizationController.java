package com.example.coursework.organizations;

import com.example.coursework.internships.Internship;
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

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('organization:write')")
    public void addOrganization(@RequestBody Organization organization) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (username != null) {
            organizationService.addOrganization(username, organization);
        }// else {
        //     throw new IllegalStateException("User must login");
        //  }
    }

    @GetMapping(path = "/{organization_id}")
    public Organization getOrganization(
            @PathVariable Long organization_id) {
        // username correct & can get &
        return organizationService.getOrganization(organization_id);
    }

    @GetMapping
    public List<Organization> getOrganizations() {
        return organizationService.getOrganizations();
    }

    @GetMapping(path = "/leaders")
    //@PreAuthorize("hasAuthority('organization:read')")
    public Set<User> getLeadersOfOrganization(
            @RequestParam("organization") Long organization_id
    ) {
        return organizationService.getLeadersOfOrganization(organization_id);
    }

    @PostMapping("/{organization_id}")
    @PreAuthorize("hasAuthority('organization:write')")
    public void addLeadersToOrganization(
            @PathVariable Long organization_id,
            @RequestParam("username") String username
    ) {
        Organization organization = organizationService.getOrganization(organization_id);
        organizationService.addOrganization(username, organization);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('organization:write')")
    public void removeLeaderFromOrganization(
            @RequestParam(value = "username") String username,
            @RequestParam("organization_name") String organization_name
    ) {
        organizationService.deleteLeader(username, organization_name);
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
        organizationService.deleteOrganization(organization_id);
    }

    @PutMapping(path = "/{organization_id}")
    @PreAuthorize("hasAuthority('organization:write')")
    public void updateInternship(@PathVariable Long organization_id,
                                 @RequestBody Organization organization) {
        organizationService.updateOrganization(organization_id, organization.getName(), organization.getDescription(),
                organization.getReference());
    }
}
