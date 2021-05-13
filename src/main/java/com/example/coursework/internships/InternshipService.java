package com.example.coursework.internships;

import com.example.coursework.bucket.BucketName;
import com.example.coursework.filestore.FileStore;
import com.example.coursework.images.ImageService;
import com.example.coursework.options.*;
import com.example.coursework.organizations.OrganizationRepository;
import com.example.coursework.organizations.Organization;
import com.example.coursework.student.User;
import com.example.coursework.student.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class InternshipService {
    private final InternshipRepository internshipRepository;
    private final OrganizationRepository organizationRepository;
    private final CountryDao countryDao;
    private final LanguageDao languageDao;
    private final PriceDao priceDao;
    private final SubjectDao subjectDao;
    private final UserRepository userRepository;
    private final FileStore fileStore;
    private final ImageService imageService;

    @Autowired
    public InternshipService(InternshipRepository internshipRepository,
                             OrganizationRepository organizationRepository,
                             CountryDao countryDAO, LanguageDao languageDao,
                             PriceDao priceDao, SubjectDao subjectDao,
                             UserRepository userRepository,
                             FileStore fileStore, ImageService imageService) {
        this.internshipRepository = internshipRepository;
        this.organizationRepository = organizationRepository;
        this.countryDao = countryDAO;
        this.languageDao = languageDao;
        this.priceDao = priceDao;
        this.subjectDao = subjectDao;
        this.userRepository = userRepository;
        this.fileStore = fileStore;
        this.imageService = imageService;
    }

    public Internship getInternship(Long internship_id) {
        Optional<Internship> internshipOptional = internshipRepository.findById(internship_id);
        Internship internship = null;
        if (internshipOptional.isPresent()) {
            internship = internshipOptional.get();
        }
        return internship;
    }

    public List<Internship> getInternships() {
        return internshipRepository.findAll();
    }

    public void addInternship(Long organization_id, Internship internship) {
        Optional<Organization> organizationOptional = organizationRepository.findById(organization_id);
        if (organizationOptional.isPresent()) {
            Organization organization = organizationOptional.get();
            internship.assignOrganization(organization); // добавляем связь стажировка - организация
            organization.addInternship(internship); // добавляем к списку стажировок орагнизации данную стажировку
            // organizationRepository.save(organization);
            countryDao.addCountry(internship.getCountry());
            subjectDao.addSubject(internship.getSubject());
            languageDao.addLanguage(internship.getLanguage());
            priceDao.addPrice(internship.getPrice());
            internshipRepository.save(internship);
        }
    }

    public void checkInternship(Long internship_id) {
        Optional<Internship> internshipOptional = internshipRepository.findById(internship_id);
        if (internshipOptional.isPresent()) {
            Internship internship = internshipOptional.get();
            internship.setChecked(true);
            internshipRepository.save(internship);
        }
    }

    public void deleteInternship(Long internship_id) {
        boolean exists = internshipRepository.existsById(internship_id);
        if (!exists) {
            throw new IllegalStateException("Internship with id " + internship_id + " doesn't exist");
        }
        internshipRepository.deleteById(internship_id);
    }

    @Transactional
    public void updateInternship(Long internship_id, String name, String description,
                                 LocalDate startDate, LocalDate finishDate, Integer price, String country,
                                 String language, String subject) {
        Optional<Internship> internshipOptional = internshipRepository.findById(internship_id);
        if (internshipOptional.isPresent()) {
            Internship internship = internshipOptional.get();
            if (name != null &&
                    name.length() > 0 &&
                    !Objects.equals(internship.getName(), name)) {
                internship.setName(name);
            }
            if (description != null &&
                    description.length() > 0 &&
                    !Objects.equals(internship.getDescription(), description)) {
                internship.setDescription(description);
            }
            if (startDate != null &&
                    !Objects.equals(internship.getStartDate(), startDate)) {
                internship.setStartDate(startDate);
            }
            if (finishDate != null &&
                    !Objects.equals(internship.getFinishDate(), finishDate)) {
                internship.setFinishDate(finishDate);
            }
            if (price != null &&
                    !Objects.equals(internship.getPrice(), price)) {
                internship.setPrice(price);
            }
            if (language != null &&
                    !Objects.equals(internship.getLanguage(), language)) {
                internship.setLanguage(language);
            }
            if (subject != null &&
                    !Objects.equals(internship.getSubject(), subject)) {
                internship.setSubject(subject);
            }
            if (country != null &&
                    !Objects.equals(internship.getCountry(), country)) {
                internship.setCountry(country);
            }
            internship.setChecked(false);
            internshipRepository.save(internship);
        }
    }

    public Set<String> findSubjects() {
        return subjectDao.getSubjects();
    }

    public Set<String> findCountries() {
        return countryDao.getCountries();
    }

    public Set<String> findLanguages() {
        return languageDao.getLanguages();
    }

    public Set<Integer> findPrices() {
        return priceDao.getPrices();
    }

    public void addFavourites(String username, String internship_name) {
        User user = userRepository.findByUsername(username);
        Internship internship = internshipRepository.findByName(internship_name);
        if (user != null && internship != null) {
            internship.addUsers(user);
            userRepository.save(user);
            internshipRepository.save(internship);
        } else {
            throw new IllegalStateException("User/internship is absent");
        }
    }

    public Set<User> getUsersFavourites(String internship_name) {
        Internship internship = internshipRepository.findByName(internship_name);
        if (internship != null) {
            return internship.getFavourites();
        } else {
            throw new IllegalStateException("Internship doesn't exist");
        }
    }

    public void deleteFavourites(String username, String internship_name) {
        User user = userRepository.findByUsername(username);
        Internship internship = internshipRepository.findByName(internship_name);
        if (user != null && internship != null) {
            internship.removeUser(user);
            userRepository.save(user);
            internshipRepository.save(internship);
        } else {
            throw new IllegalStateException("User/internship is absent");
        }
    }


    public void uploadInternshipImage(Long internship_id, MultipartFile file) {
        imageService.isFileEmpty(file);
        imageService.isImage(file);

        Optional<Internship> optionalInternship = internshipRepository.findById(internship_id);
        if (optionalInternship.isPresent()) {
            Internship internship = optionalInternship.get();

            Map<String, String> metadata = imageService.extractMetadata(file);

            String path = String.format("%s/%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), "internship", internship.getInternship_id());
            String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

            try {
                fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
                internship.setInternshipImageLink(filename);
                internshipRepository.save(internship);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            throw new IllegalStateException("Internship not found");
        }
    }

    byte[] downloadInternshipImage(Long internship_id) {
        Optional<Internship> optionalInternship = internshipRepository.findById(internship_id);
        if (optionalInternship.isPresent()) {
            Internship internship = optionalInternship.get();

            String path = String.format("%s/%s/%s",
                    BucketName.PROFILE_IMAGE.getBucketName(),
                    "internship",
                    internship.getInternship_id());
            return internship.getInternshipImageLink()
                    .map(key -> fileStore.download(path, key))
                    .orElse(new byte[0]);
        } else {
            throw new IllegalStateException("Failed to download internship image");
        }

    }

    public void deleteImage(Long internship_id) {
        Optional<Internship> optionalInternship = internshipRepository.findById(internship_id);
        if (optionalInternship.isPresent()) {
            Internship internship = optionalInternship.get();
            String path = String.format("%s/%s/%s",
                    BucketName.PROFILE_IMAGE.getBucketName(),
                    "internship",
                    internship.getInternship_id());
            Optional<String> filename = internship.getInternshipImageLink();
            if (filename.isPresent()) {
                String image_filename = filename.get();
                fileStore.deleteFile(path, image_filename);
                internship.setInternshipImageLink(null);
                internshipRepository.save(internship);
            } else {
                throw new IllegalStateException("Image not found");
            }

        } else {
            throw new IllegalStateException("Failed to delete internship image");
        }
    }
}

