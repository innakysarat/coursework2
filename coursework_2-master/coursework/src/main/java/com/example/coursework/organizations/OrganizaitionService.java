package com.example.coursework.organizations;

import com.example.coursework.internships.Internship;
import com.example.coursework.student.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrganizaitionService {
    private final OrganizaitionRepository organizaitionRepository;

    @Autowired
    public OrganizaitionService(OrganizaitionRepository organizaitionRepository) {
        this.organizaitionRepository = organizaitionRepository;
    }

    public Organization addOrganization(Organization organization) {
      /*  if (organizaitionRepository.existsByName(organization.getName())) {
            throw new IllegalStateException("Organization exists");
        }*/
        return organizaitionRepository.save(organization);
    }

    public Organization getOrganization(Long organization_id) {
        Optional<Organization> organization = organizaitionRepository.findById(organization_id);
        if (organization.isPresent()) {
            return organization.get();
        } else {
            throw new IllegalStateException("Organization doesn't exist");
        }
    }

    public List<Organization> getOrganizations() {
        return organizaitionRepository.findAll();
    }

    public Set<User> getLeadersOfOrganization(Long organization_id) {
        Optional<Organization> organizationOptional = organizaitionRepository.findById(organization_id);
        Organization organization;
        Set<User> users = new HashSet<>();
       // String users = "";
        if (organizationOptional.isPresent()) {
            organization = organizationOptional.get();
            users = organization.getLeaders();

         /*   User u = organization.getLeaders().stream().findFirst().get();
            users = u.getUsername();*/
        }
        return users;
    }

}
