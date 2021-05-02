/*package com.example.coursework.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class StudentConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            Student mariam = new Student(
                    1,
                    "Mariam"
            );

            Student jannet = new Student(
                    2,
                    "Janet"
            );

            List<Student> list = new ArrayList<Student>();
            list.add(mariam);
            list.add(jannet);
            repository.saveAll(list);
        };
    }
}*/
