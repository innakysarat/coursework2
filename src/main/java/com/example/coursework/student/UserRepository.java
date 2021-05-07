package com.example.coursework.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// responsible for data access
@Repository
public interface UserRepository extends
        JpaRepository<User, Integer> {

    //@Query("SELECT s FROM User s WHERE s.email = ?1")
    User findByEmail(String email);

    User findByUsername(String username);

    User findByPhone(String phone);
    //User findById(Integer id);
    // public Optional<User> selectUserByUsername(String username);
}
