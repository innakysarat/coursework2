package com.example.coursework.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// responsible for data access
@Repository
public interface StudentRepository extends
        JpaRepository<User, Integer> {

    //@Query("SELECT s FROM User s WHERE s.email = ?1")
    User findStudentByEmail(String email);

    User findByUsername(String username);
    //User findById(Integer id);
    // public Optional<User> selectUserByUsername(String username);
}
