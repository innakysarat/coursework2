package com.example.coursework.organizations;

import com.example.coursework.student.User;
import com.example.coursework.student.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrganizaitionService {
    private final OrganizaitionRepository organizaitionRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrganizaitionService(OrganizaitionRepository organizaitionRepository, UserRepository userRepository) {
        this.organizaitionRepository = organizaitionRepository;
        this.userRepository = userRepository;
    }

    public void addOrganization(String username, Organization organization) {
      /*  if (organizaitionRepository.existsByName(organization.getName())) {
            throw new IllegalStateException("Organization exists");
        }*/
        System.out.println(username);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.addOrganization(organization); // добавляем связь руководитель - организация
            // organization.addLeader(user); // добавляем в список руководителей организации данного руководителя
            organizaitionRepository.save(organization);
        } else {
            throw new IllegalStateException("Leader is absent");
        }
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
        if (organizationOptional.isPresent()) {
            organization = organizationOptional.get();
            users = organization.getLeaders();
        }
        return users;
    }

}
