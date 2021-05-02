package com.example.coursework.internships;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name="Internship")
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
            name = "startDate"
    )
    private LocalDate startDate;
    @Column(
            name = "finishDate"
    )
    private LocalDate finishDate;
    @Column(
            name = "isChecked"
    )
    private boolean isChecked;



}
