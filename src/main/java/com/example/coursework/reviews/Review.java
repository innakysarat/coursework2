package com.example.coursework.reviews;

import com.example.coursework.internships.Internship;
import com.example.coursework.student.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity(name = "Review")
@Table(
        name = "review"
)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
            name = "review_id",
            updatable = false
    )
    private Long review_id;
    @Column(
            name = "textcomment",
            columnDefinition = "TEXT"
    )
    private String textcomment;
    @Min(value = 1, message = "Оценка должна быть не меньше 1")
    @Max(value = 10, message = "Оценка должна быть не больше 10")
    @Column(
            name = "score",
            scale = 1,
            precision = 3
    )
    private double score;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "internship_id")
    private Internship internship;
    public Review() {
    }

    public Long getReview_id() {
        return review_id;
    }

    public String getTextcomment() {
        return textcomment;
    }

    public double getScore() {
        return score;
    }

    public User getAuthor() {
        return author;
    }

    public Internship getInternship() {
        return internship;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setInternship(Internship internship) {
        this.internship = internship;
    }

    public void setTextcomment(String textcomment) {
        this.textcomment = textcomment;
    }

    public void setScore(double score) {
        this.score = score;
    }
}


