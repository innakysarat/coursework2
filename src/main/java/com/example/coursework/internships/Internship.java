package com.example.coursework.internships;

import com.example.coursework.organizations.Organization;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "Internship")
@Table(
        name = "internship"
)
public class Internship {

    @Id
    @SequenceGenerator(
            name = "internship_sequence",
            sequenceName = "internship_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "internship_sequence"
    )
    @Column(
            name = "internship_id",
            updatable = false
    )
    private Long internship_id;
    @Column(
            name = "internship_name",
            columnDefinition = "TEXT"
    )
    private String name;
    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;
    @Column(
            name = "start_date"
    )
    private LocalDate startDate;
    @Column(
            name = "finish_date"
    )
    private LocalDate finishDate;
    @Column(
            name = "is_checked"
    )
    private boolean isChecked;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "organization_id")
    private Organization organization;

    public Long getInternship_id() {
        return internship_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void assignOrganization(Organization organization) {
        this.organization = organization;
    }
}
