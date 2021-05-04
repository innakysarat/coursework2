package com.example.coursework.internships;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InternshipService {
    private final InternshipRepository internshipRepository;

    @Autowired
    public InternshipService(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    public Internship getInternship(Long internship_id) {
        Optional<Internship> internshipOptional = internshipRepository.findById(internship_id);
        Internship internship = null;
        if (internshipOptional.isPresent()) {
            internship = internshipOptional.get();
        }
        return internship;
    }

    public Internship addInternship(Internship internship) {
       return internshipRepository.save(internship);
    }
}
