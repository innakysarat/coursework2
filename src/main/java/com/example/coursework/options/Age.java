/*package com.example.coursework.options;

import javax.persistence.*;

@Entity(name = "Age")
@Table(
        name = "ages"
)
public class Age {
    @Id
    @SequenceGenerator(
            name = "age_sequence",
            sequenceName = "age_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "age_sequence"
    )
    @Column(
            name = "age_id",
            updatable = false
    )
    private Long age_id;
    @Column(
            name = "age_min"
    )
    private Integer age_min;
    @Column(
            name = "age_max"
    )
    private Integer age_max;
}*/
