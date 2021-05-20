package com.example.coursework.student;

import com.example.coursework.deadlines.Deadline;
import com.example.coursework.internships.Internship;
import com.example.coursework.organizations.Organization;
import com.example.coursework.reviews.Review;
import com.example.coursework.security.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Entity(name = "User")
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "email_unique", columnNames = "email"),
                @UniqueConstraint(name = "phone_unique", columnNames = "phone"),
                @UniqueConstraint(name = "username_unique", columnNames = "username")
        }
)
public class User implements UserDetails {
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    @Column(
            name = "user_id",
            updatable = false
    )
    private Integer user_id;
    @Column(
            name = "username",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String username;
    @Column(
            name = "role",
            columnDefinition = "TEXT"
    )
    private String role;
    @JsonIgnore
    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String password;
    @Column(
            name = "name",
            columnDefinition = "TEXT"
    )
    private String name;
    @Column(
            name = "surname",
            columnDefinition = "TEXT"
    )
    private String surname;
    @Column(
            name = "patronymic",
            columnDefinition = "TEXT"
    )
    private String patronymic;
    @Column(
            name = "birth"
    )
    private LocalDate dayOfBirth;
    /* @Transient // = not a column
     private Integer age;*/
    @Column(
            name = "email",
            columnDefinition = "TEXT"
    )
    private String email;
    @Column(
            name = "phone",
            columnDefinition = "TEXT"
    )
    private String phone;
    @Column(
            columnDefinition = "TEXT"
    )
    private String userImageLink;

    public User() {
        grantedAuthorities = UserRole.STUDENT.getGrantedAuthorities();
      /*  if (dayOfBirth != null) {
            this.age = Period.between(dayOfBirth, LocalDate.now()).getYears();
        }*/
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "leaders")
    private final Set<Organization> organizations = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<Review> reviews = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "favourites")
    private final Set<Internship> internships = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<Deadline> deadlines = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(user_id, user.user_id);
    }


    @Override
    public int hashCode() {
        return Objects.hash(user_id);
    }

    @Transient
    private Set<? extends GrantedAuthority> grantedAuthorities;
    @Transient
    private boolean isAccountNonExpired;
    @Transient
    private boolean isAccountNonLocked;
    @Transient
    private boolean isCredentialsNonExpired;
    @Transient
    private boolean isEnabled;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        if (role.equals("ADMIN"))
            grantedAuthorities = UserRole.ADMIN.getGrantedAuthorities();
        if (role.equals("LEADER")) {
            grantedAuthorities = UserRole.LEADER.getGrantedAuthorities();
        } else {
            grantedAuthorities = UserRole.STUDENT.getGrantedAuthorities();
        }
    }

    // +image
    public User(String username, String password,
                String name, String surname,
                String patronymic, LocalDate dayOfBirth,
                String email, String phone, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dayOfBirth = dayOfBirth;
        this.email = email;
        this.phone = phone;
        this.role = role;
        if (role.equals("ADMIN"))
            grantedAuthorities = UserRole.ADMIN.getGrantedAuthorities();
        if (role.equals("LEADER")) {
            grantedAuthorities = UserRole.LEADER.getGrantedAuthorities();
        } else {
            grantedAuthorities = UserRole.STUDENT.getGrantedAuthorities();
        }
        // this.age = Period.between(dayOfBirth, LocalDate.now()).getYears();
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

 /*   public Integer getAge() {
        return Period.between(dayOfBirth, LocalDate.now()).getYears();
    }*/

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getDayOfBirth() {
        return dayOfBirth;
    }

    public Optional<String> getUserImageLink() {
        return Optional.ofNullable(userImageLink);
    }

    public Set<Deadline> getDeadlines() { return deadlines; }


    public void setName(String studentName) {
        this.name = studentName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setDayOfBirth(LocalDate dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    /*public void setAge(Integer age) {
        this.age = age;
    }*/

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserImageLink(String userImageLink) {
        this.userImageLink = userImageLink;
    }

    public void setGrantedAuthorities(Set<? extends GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (role.equals("ADMIN"))
            return UserRole.ADMIN.getGrantedAuthorities();
        if (role.equals("LEADER")) {
            return UserRole.LEADER.getGrantedAuthorities();
        } else {
            return UserRole.STUDENT.getGrantedAuthorities();
        }
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Organization> getOrganizations() {
        return organizations;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public void addDeadline(Deadline deadline) {
        deadlines.add(deadline);
    }

    public Set<Internship> getInternships() {
        return internships;
    }

    public void removeDeadline(Deadline deadline) {
        deadlines.remove(deadline);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
    }

    @Override
    public String toString() {
        return "User id=" + user_id
                + ", name=" + name
                + ", email=" + email;
        //  + ", dob=" + dayOfBirth;
        // + ", age=" + age + "]";
    }
}

