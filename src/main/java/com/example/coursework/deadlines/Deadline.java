package com.example.coursework.deadlines;

import com.example.coursework.internships.Internship;
import com.example.coursework.student.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "deadline")
public class Deadline {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
            name = "deadline_id",
            updatable = false
    )
    private Long deadline_id;
    @Column(
            columnDefinition = "TEXT"
    )
    private String description;
    @Column
    private boolean isCompleted;
    @Column
    private LocalDate start;
    @Column
    private LocalDate finish;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "internship_id")
    private Internship internship;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Deadline() {

    }

    public Long getDeadline_id() {
        return deadline_id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getFinish() {
        return finish;
    }

    public Internship getInternship() {
        return internship;
    }

    public User getUser() {
        return user;
    }

    public void setDeadline_id(Long deadline_id) {
        this.deadline_id = deadline_id;
    }

    public void setInternship(Internship internship) {
        this.internship = internship;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public void setFinish(LocalDate finish) {
        this.finish = finish;
    }
}
