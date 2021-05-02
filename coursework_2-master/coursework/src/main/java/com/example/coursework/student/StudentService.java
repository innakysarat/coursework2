package com.example.coursework.student;

import com.example.coursework.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class StudentService implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getStudents() {
        return studentRepository.findAll();
    }

    public User getUser(Integer user_id) {
        Optional<User> user = studentRepository.findById(user_id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new IllegalStateException("User doesn't exist");
        }
    }

    public void addNewStudent(User user) {
        String role;
        if (user.getUsername().endsWith("admin")) {
            role = "ADMIN";
        } else if (user.getUsername().endsWith("leader")) {
            role = "LEADER";
        } else {
            role = "STUDENT";
        }
        if (role.equals("ADMIN")) {
            user.setRole("ADMIN");
            user.setGrantedAuthorities(UserRole.ADMIN.getGrantedAuthorities());
        } else if (role.equals("LEADER")) {
            user.setRole("LEADER");
            user.setGrantedAuthorities(UserRole.LEADER.getGrantedAuthorities());
        } else {
            user.setRole("STUDENT");
            user.setGrantedAuthorities(UserRole.STUDENT.getGrantedAuthorities());
        }
        studentRepository.save(user);
    }

    public void deleteStudent(Integer studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + " doesn't exist");
        }
        User user = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " doesn't exist"));
        if (user.getRole().equals("ADMIN")) {
            throw new IllegalStateException("Can't delete admin");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Integer studentId, String name, String email) {
        User user = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " doesn't exist"));

        if (name != null &&
                name.length() > 0 &&
                !Objects.equals(user.getName(), name)) {
            user.setName(name);
        }

        if (email != null &&
                email.length() > 0 &&
                !Objects.equals(user.getEmail(), email)) {
            User student
                    = studentRepository.findStudentByEmail(email);
            if (student != null) {
                throw new IllegalStateException("Email is taken");
            }
            user.setEmail(email);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userDetails = studentRepository.findByUsername(username);
        String role;
        if (username.endsWith("admin")) {
            role = "ADMIN";
        } else if (username.endsWith("leader")) {
            role = "LEADER";
        } else {
            role = "STUDENT";
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        if (role.equals("ADMIN")) {
            for (SimpleGrantedAuthority s : UserRole.ADMIN.getGrantedAuthorities()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(s.getAuthority()));
            }
            userDetails.setGrantedAuthorities(UserRole.ADMIN.getGrantedAuthorities());
        } else if (role.equals("LEADER")) {
            userDetails.setGrantedAuthorities(UserRole.LEADER.getGrantedAuthorities());
        } else {
            for (SimpleGrantedAuthority s : UserRole.STUDENT.getGrantedAuthorities()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(s.getAuthority()));
            }
            userDetails.setGrantedAuthorities(UserRole.STUDENT.getGrantedAuthorities());
        }
        String password = passwordEncoder.encode(userDetails.getPassword());
        return new org.springframework.security.core.userdetails.User(userDetails.getUsername(), password, grantedAuthorities);
    }
}