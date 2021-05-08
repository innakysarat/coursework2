package com.example.coursework.organizations;

import com.example.coursework.internships.Internship;
import com.example.coursework.student.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organization",
        uniqueConstraints = {
                @UniqueConstraint(name = "name_unique", columnNames = "name"),
        })
public class Organization {
    @Id
    @SequenceGenerator(
            name = "organization_sequence",
            sequenceName = "organization_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "organization_sequence"
    )
    @Column(
            name = "organization_id",
            updatable = false
    )
    private Long organization_id;
    @Column(
            columnDefinition = "TEXT"
    )
    private String name;
    @Column(
            columnDefinition = "TEXT"
    )
    private String description;
    @Column(
            columnDefinition = "TEXT"
    )
    private String reference;
    @ManyToMany
    @JoinTable(
            name = "leaders_organizations",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "leader_id")
    )
    private Set<User> leaders;

    @OneToMany(mappedBy = "organization")
    private final Set<Internship> internships = new HashSet<>();

    public Long getOrganization_id() {
        return organization_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getReference() {
        return reference;
    }

    public void addLeader(User user) {
        this.leaders.add(user);
    }

    public Set<User> getLeaders() {
        return leaders;
    }

    public Set<Internship> getInternships() {
        return internships;
    }

    public void addInternship(Internship internship) {
        internships.add(internship);
    }
}
