package com.example.coursework.organizations;

import com.example.coursework.student.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "organization",
        uniqueConstraints = {
                @UniqueConstraint(name = "name_unique", columnNames = "organization_name"),
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
            name = "organization_name",
            columnDefinition = "TEXT"
    )
    private String name;
    @Column(
            name = "organization_description",
            columnDefinition = "TEXT"
    )
    private String description;
    @Column(
            name = "reference",
            columnDefinition = "TEXT"
    )
    private String reference;
    @JsonIgnore
    @ManyToMany(mappedBy = "organizations")
    Set<User> leaders;

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
        leaders.add(user);
    }
}
