package com.example.coursework.organizations;

import com.example.coursework.internships.Internship;
import com.example.coursework.student.User;
import com.example.coursework.student.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, UserRepository userRepository) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    public void addOrganization(String username, Organization organization) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.addOrganization(organization); // добавляем связь руководитель - организация
            //  organization.addLeader(user); // добавляем в список руководителей организации данного руководителя
            userRepository.save(user);
            organizationRepository.save(organization);
        } else {
            throw new IllegalStateException("Leader is absent");
        }
    }

    public Organization getOrganization(Long organization_id) {
        Optional<Organization> organization = organizationRepository.findById(organization_id);
        if (organization.isPresent()) {
            return organization.get();
        } else {
            throw new IllegalStateException("Organization doesn't exist");
        }
    }

    public List<Organization> getOrganizations() {
        return organizationRepository.findAll();
    }

    public Set<User> getLeadersOfOrganization(Long organization_id) {
        Optional<Organization> organizationOptional = organizationRepository.findById(organization_id);
        Organization organization;
        Set<User> users = new HashSet<>();
        if (organizationOptional.isPresent()) {
            organization = organizationOptional.get();
            users = organization.getLeaders();
        }
        return users;
    }

    public void deleteOrganization(Long organization_id) {
        boolean exists = organizationRepository.existsById(organization_id);
        if (!exists) {
            throw new IllegalStateException("Internship with id " + organization_id + " doesn't exist");
        }
        organizationRepository.deleteById(organization_id);
    }

    @Transactional
    public void updateOrganization(Long organization_id, String name, String description, String reference) {
        Optional<Organization> organizationOptional = organizationRepository.findById(organization_id);
        if (organizationOptional.isPresent()) {
            Organization organization = organizationOptional.get();
            if (name != null &&
                    name.length() > 0 &&
                    !Objects.equals(organization.getName(), name)) {
                organization.setName(name);
            }
            if (description != null &&
                    description.length() > 0 &&
                    !Objects.equals(organization.getDescription(), description)) {
                organization.setDescription(description);
            }
            if (reference != null &&
                    reference.length() > 0 &&
                    !Objects.equals(organization.getReference(), reference)) {
                organization.setReference(reference);
            }
            organizationRepository.save(organization);
        }
    }
}
