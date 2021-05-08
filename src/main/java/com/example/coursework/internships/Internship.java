package com.example.coursework.internships;

import com.example.coursework.organizations.Organization;
import com.example.coursework.reviews.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "internship",
        uniqueConstraints = {
                @UniqueConstraint(name = "name_unique", columnNames = "internship_name"),
        }
)
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
            columnDefinition = "TEXT"
    )
    private String country;
    @Column(
            columnDefinition = "TEXT"
    )
    private String subject;
    @Column(
            columnDefinition = "TEXT"
    )
    private String language;
    @Column()
    private Integer price;
    @Column()
    private Integer age_min;
    @Column()
    private Integer age_max;

    @Column(
            name = "is_checked"
    )
    private boolean isChecked;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id")
    private Organization organization;
    public Internship() {
    }

    @OneToMany(mappedBy = "internship", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Review> reviews = new HashSet<>();


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

    public Set<Review> getReviews() {
        return reviews;
    }

    public String getCountry() {
        return country;
    }

    public String getSubject() {
        return subject;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getPrice() {
        return price;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public void assignOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

}
