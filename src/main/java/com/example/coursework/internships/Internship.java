package com.example.coursework.internships;

import com.example.coursework.deadlines.Deadline;
import com.example.coursework.organizations.Organization;
import com.example.coursework.reviews.Review;
import com.example.coursework.student.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
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
    @Column
    private Integer price;
    @Column
    private Integer age_min;
    @Column
    private Integer age_max;

    @Column(
            name = "is_checked"
    )
    private boolean isChecked;
    @Column(
            name = "image",
            columnDefinition = "TEXT"
    )
    private String internshipImageLink;
    @Column(
            nullable = true
    )
    private Double score;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @JsonIgnore
    @OneToMany(mappedBy = "internship", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<Deadline> deadlines = new HashSet<>();

    public Internship() {
    }

    @OneToMany(mappedBy = "internship", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Review> reviews = new HashSet<>();

    // @JsonIgnore
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "users_internships",
            joinColumns = @JoinColumn(name = "internship_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public Set<User> favourites = new HashSet<>();

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

    public Optional<String> getInternshipImageLink() {
        return Optional.ofNullable(internshipImageLink);
    }

    public Double getScore() {
        return reviews.stream()
                .mapToDouble(Review::getScore)
                .average()
                .orElse(Double.NaN);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public void addDeadline(Deadline deadline) {
        deadlines.add(deadline);
    }

    public void assignOrganization(Organization organization) {
        this.organization = organization;
    }

    public void addUsers(User user) {
        favourites.add(user);
        user.getInternships().add(this);
    }

    public void removeUser(User user) {
        favourites.remove(user);
        user.getInternships().remove(this);
    }

    public void removeDeadline(Deadline deadline) {
        deadlines.remove(deadline);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
    }

    public Set<User> getFavourites() {
        return favourites;
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

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setInternshipImageLink(String internshipImageLink) {
        this.internshipImageLink = internshipImageLink;
    }

    public void setAge_min(Integer age_min) {
        this.age_min = age_min;
    }

    public void setAge_max(Integer age_max) {
        this.age_max = age_max;
    }
}
