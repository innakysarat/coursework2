package com.example.coursework.organizations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizaitionService {
    private final OrganizaitionRepository organizaitionRepository;

    @Autowired
    public OrganizaitionService(OrganizaitionRepository organizaitionRepository) {
        this.organizaitionRepository = organizaitionRepository;
    }

    public void addOrganization(Organization organization) {
      /*  if (organizaitionRepository.existsByName(organization.getName())) {
            throw new IllegalStateException("Organization exists");
        }*/
        organizaitionRepository.save(organization);
    }

    public Organization getOrganization(Long organization_id) {
        Optional<Organization> organization = organizaitionRepository.findById(organization_id);
        if (organization.isPresent()) {
            return organization.get();
        } else {
            throw new IllegalStateException("Organization doesn't exist");
        }
    }

}
