package com.example.coursework.organizations;

import com.example.coursework.student.StudentService;
import com.example.coursework.student.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("organizations")
public class OrganizationController {

    private final OrganizaitionService organizaitionService;
    private final StudentService studentService;

    @Autowired
    public OrganizationController(OrganizaitionService organizaitionService, StudentService studentService) {
        this.organizaitionService = organizaitionService;
        this.studentService = studentService;
    }

    @PutMapping("/{organization_id}/leaders/{user_id}")
    public void addOrganizationToLeader(
            @PathVariable Long organization_id,
            @PathVariable Integer user_id
    ) {
        Organization organization = organizaitionService.getOrganization(organization_id);
        User user = studentService.getUser(user_id);
        organization.addLeader(user);
        organizaitionService.addOrganization(organization);
    }

    @PostMapping(path = "/register")
    public void addOrganization(@RequestBody Organization organization) {
        organizaitionService.addOrganization(organization);
    }
}
