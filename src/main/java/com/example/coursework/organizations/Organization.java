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
    @GeneratedValue(strategy = GenerationType.AUTO)
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
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private final Set<User> leaders = new HashSet<>();
    public Organization(){

    }

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
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
        return this.internships;
    }

    public void addInternship(Internship internship) {
        this.internships.add(internship);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
