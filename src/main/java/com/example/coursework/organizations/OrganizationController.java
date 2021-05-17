package com.example.coursework.organizations;

import com.example.coursework.student.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }
    // ДОБАВИТЬ ВЗЯТИЕ ВСЕХ СТАЖИРОВОК ОРГАНИЗАЦИИ
    @CrossOrigin
    @PostMapping
    @PreAuthorize("hasAuthority('organization:write')")
    public void addOrganization(@RequestBody Organization organization) {
        // только руководитель
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            organizationService.addOrganization(username, organization);
        } else {
            throw new IllegalStateException("User must login");
        }
    }

    @CrossOrigin
    @GetMapping(path = "/{organization_id}")
    public Organization getOrganization(
            @PathVariable Long organization_id) {
        // любой пользователь, администратор и только нужный руководитель
        return organizationService.getOrganization(organization_id);
    }

    @CrossOrigin
    @GetMapping
    public List<Organization> getOrganizations() {
        // любой пользователь и администратор
        return organizationService.getOrganizations();
    }

    @CrossOrigin
    @GetMapping(path = "/leaders")
    @PreAuthorize("hasAuthority('organization:read')")
    public Set<User> getLeadersOfOrganization(
            @RequestParam("organization") Long organization_id
    ) {
        return organizationService.getLeadersOfOrganization(organization_id);
    }

    @CrossOrigin
    @PostMapping("/{organization_id}")
    @PreAuthorize("hasAuthority('organization:write')")
    public void addLeadersToOrganization(
            @PathVariable Long organization_id,
            @RequestParam("username") String username
    ) {
        // нужный руководитель + администратор
        Organization organization = organizationService.getOrganization(organization_id);
        organizationService.addLeaderToOrganization(username, organization);
    }

    @CrossOrigin
    @DeleteMapping
    @PreAuthorize("hasAuthority('organization:write')")
    public void removeLeaderFromOrganization(
            @RequestParam(value = "username") String username,
            @RequestParam("organization_name") String organization_name
    ) {
        // нужный руководитель + любой админ
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
    @CrossOrigin
    @DeleteMapping(path = "/{organization_id}")
    @PreAuthorize("hasAuthority('organization:write')")
    public void deleteOrganization(@PathVariable Long organization_id) {
        organizationService.deleteOrganization(organization_id);
    }

    @CrossOrigin
    @PutMapping(path = "/{organization_id}")
    @PreAuthorize("hasAuthority('organization:write')")
    public void updateInternship(@PathVariable Long organization_id,
                                 @RequestBody Organization organization) {
        // проверка на нужного руководителя
        // и все администраторы
        organizationService.updateOrganization(organization_id, organization.getName(), organization.getDescription(),
                organization.getReference());
    }

    @CrossOrigin
    @PostMapping(
            path = "{organization_id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('organization:write')")
    public void uploadOrganizationImage(@PathVariable("organization_id") Long organization_id,
                                        @RequestParam("image") MultipartFile file) {
        organizationService.uploadOrganizationImage(organization_id, file);
    }

    @CrossOrigin
    @GetMapping(path = "{organization_id}/image")
    public byte[] downloadOrganizationImage(@PathVariable("organization_id") Long organization_id) {
        return organizationService.downloadOrganizationImage(organization_id);
    }

    @CrossOrigin
    @DeleteMapping(path = "/{organization_id}/image")
    @PreAuthorize("hasAuthority('organization:write')")
    public void deleteFile(@PathVariable Long organization_id) {
        // нужный руководитель + любой администратор
        organizationService.deleteImage(organization_id);
    }
}
