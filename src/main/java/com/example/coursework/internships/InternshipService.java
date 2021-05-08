package com.example.coursework.internships;

import com.example.coursework.options.*;
import com.example.coursework.organizations.OrganizaitionRepository;
import com.example.coursework.organizations.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InternshipService {
    private final InternshipRepository internshipRepository;
    private final OrganizaitionRepository organizationRepository;
    private final CountryDao countryDao;
    private final LanguageDao languageDao;
    private final PriceDao priceDao;
    private final SubjectDao subjectDao;


    @Autowired
    public InternshipService(InternshipRepository internshipRepository,
                             OrganizaitionRepository organizaitionRepository,
                             CountryDao countryDAO, LanguageDao languageDao,
                             PriceDao priceDao, SubjectDao subjectDao) {
        this.internshipRepository = internshipRepository;
        this.organizationRepository = organizaitionRepository;
        this.countryDao = countryDAO;
        this.languageDao = languageDao;
        this.priceDao = priceDao;
        this.subjectDao = subjectDao;
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
            organizationRepository.save(organization);
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
                                 LocalDate startDate, LocalDate finishDate) {
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
            internship.setChecked(false);
            internshipRepository.save(internship);
        }
    }
}

