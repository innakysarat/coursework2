package com.example.coursework.student;

import com.example.coursework.organizations.Organization;
import com.example.coursework.security.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Set;

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
            columnDefinition = "TEXT"
    )
    private String username;
    @Column(
            name = "role",
            columnDefinition = "TEXT"
    )
    private String role;
    @Column(
            name = "password",
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
    /*@Transient // = not a column
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

    public User() {
        grantedAuthorities = UserRole.STUDENT.getGrantedAuthorities();
    }

    @ManyToMany
    @JoinTable(
            name = "leaders_organizations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    Set<Organization> organizations;
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
       /* isAccountNonExpired = true;
        isAccountNonLocked = true;
        isCredentialsNonExpired = true;
        isEnabled = true;*/
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
      /*  isAccountNonExpired = true;
        isAccountNonLocked = true;
        isCredentialsNonExpired = true;
        isEnabled = true;*/
        // this.age = Period.between(dayOfBirth, LocalDate.now()).getYears();
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setGrantedAuthorities(Set<? extends GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public Integer getAge() {
        return Period.between(dayOfBirth, LocalDate.now()).getYears();
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setUser_id(Integer studentId) {
        this.user_id = studentId;
    }

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

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }

    // @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (role.equals("ADMIN"))
            return UserRole.ADMIN.getGrantedAuthorities();
        if (role.equals("LEADER")) {
            return UserRole.LEADER.getGrantedAuthorities();
        } else {
            return UserRole.STUDENT.getGrantedAuthorities();
        }
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Student [id=" + user_id
                + ", name=" + name
                + ", email=" + email;
        //  + ", dob=" + dayOfBirth;
        // + ", age=" + age + "]";
    }
}

