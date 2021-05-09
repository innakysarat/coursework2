package com.example.coursework.options;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class SubjectDao {
    private final Set<String> subjects;

    public SubjectDao(){
        subjects = new LinkedHashSet<>();
    }
    public void addSubject(String subject) {
        subjects.add(subject);
    }

    public Set<String> getSubjects() {
        return subjects;
    }

/*    @OneToMany(mappedBy = "subject", fetch = FetchType.EAGER)
    private Set<Internship> internships = new HashSet<>();
    public void addInternship(Internship internship){
        internships.add(internship);
    }*/
}
