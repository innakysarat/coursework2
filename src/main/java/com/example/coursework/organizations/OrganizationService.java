package com.example.coursework.organizations;

import com.example.coursework.bucket.BucketName;
import com.example.coursework.filestore.FileStore;
import com.example.coursework.images.ImageService;
import com.example.coursework.internships.Internship;
import com.example.coursework.student.User;
import com.example.coursework.student.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final FileStore fileStore;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, UserRepository userRepository, ImageService imageService, FileStore fileStore) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.fileStore = fileStore;
    }

    public void addOrganization(String username, Organization organization) {
        User user = userRepository.findByUsername(username);
        organization.addLeader(user); // добавляем в список руководителей организации данного руководителя
        userRepository.save(user);
        organizationRepository.save(organization);
    }

    public void deleteLeader(String username, String organization_name) {
        User user = userRepository.findByUsername(username);
        Organization organization = organizationRepository.findByName(organization_name);
        if (user != null && organization != null) {
            organization.removeLeader(user);
            userRepository.save(user);
            organizationRepository.save(organization);
        } else {
            throw new IllegalStateException("Leader and/or organization don't exist");
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

    public void uploadOrganizationImage(Long organization_id, MultipartFile file) {
        imageService.isFileEmpty(file);
        imageService.isImage(file);

        Optional<Organization> optionalOrganization = organizationRepository.findById(organization_id);
        if (optionalOrganization.isPresent()) {
            Organization organization = optionalOrganization.get();
            Map<String, String> metadata = imageService.extractMetadata(file);

            String path = String.format("%s/%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), "organization", organization.getOrganization_id());
            String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

            try {
                fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
                organization.setOrganizationImageLink(filename);
                organizationRepository.save(organization);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            throw new IllegalStateException("Organization not found");
        }

    }

    public byte[] downloadOrganizationImage(Long organization_id) {
        Optional<Organization> optionalOrganization = organizationRepository.findById(organization_id);
        if (optionalOrganization.isPresent()) {
            Organization organization = optionalOrganization.get();
            String path = String.format("%s/%s/%s",
                    BucketName.PROFILE_IMAGE.getBucketName(),
                    "organization",
                    organization.getOrganization_id());
            return organization.getOrganizationImageLink()
                    .map(key -> fileStore.download(path, key))
                    .orElse(new byte[0]);
        } else {
            throw new IllegalStateException("Failed to download organization image");
        }
    }

    public void deleteImage(Long organization_id) {
        Optional<Organization> optionalOrganization = organizationRepository.findById(organization_id);
        if (optionalOrganization.isPresent()) {
            Organization organization = optionalOrganization.get();
            String path = String.format("%s/%s/%s",
                    BucketName.PROFILE_IMAGE.getBucketName(),
                    "organization",
                    organization.getOrganization_id());
            Optional<String> filename = organization.getOrganizationImageLink();
            if (filename.isPresent()) {
                String image_filename = filename.get();
                fileStore.deleteFile(path, image_filename);
                organization.setOrganizationImageLink(null);
                organizationRepository.save(organization);
            } else {
                throw new IllegalStateException("Image not found");
            }
        } else {
            throw new IllegalStateException("Failed to delete organization image");
        }
    }
}
