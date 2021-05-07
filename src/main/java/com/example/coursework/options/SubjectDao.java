package com.example.coursework.options;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class SubjectDao {
    Set<String> subjects = new LinkedHashSet<>();

    public void addSubject(String subject) {
        subjects.add(subject);
    }

/*    @OneToMany(mappedBy = "subject", fetch = FetchType.EAGER)
    private Set<Internship> internships = new HashSet<>();
    public void addInternship(Internship internship){
        internships.add(internship);
    }*/
}
