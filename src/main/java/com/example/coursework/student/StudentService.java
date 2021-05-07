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
import java.time.LocalDate;
import java.util.*;

@Service
public class StudentService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new IllegalStateException("User doesn't exist");
        }
    }

    public void addUser(User user) {
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
        userRepository.save(user);
    }

    public void deleteStudent(Integer studentId) {
        boolean exists = userRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + " doesn't exist");
        }
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " doesn't exist"));
        if (user.getRole().equals("ADMIN")) {
            throw new IllegalStateException("Can't delete admin");
        }
        userRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudentByAdmin(Integer studentId, String name, String surname, String patronymic,
                                     String email, String phone, String username, String password,
                                     LocalDate dayOfBirth) {
        Optional<User> userOptional = userRepository.findById(studentId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            updateStudent(user, name, surname, patronymic, email, phone, username, password, dayOfBirth);
        }
    }

    @Transactional
    public void updateStudentByHimself(Integer user_id, String username, String name, String surname, String patronymic,
                                       String email, String phone, String username_update, String password,
                                       LocalDate dayOfBirth) {
        User user = userRepository.findByUsername(username);
        User user_byId = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalStateException("Student with id " + user_id + " doesn't exist"));
        if (!user.equals(user_byId)) {
            throw new IllegalStateException("User can not update someone else's information");
        } else {
            updateStudent(user, name, surname, patronymic, email, phone, username_update, password, dayOfBirth);
        }
    }

    @Transactional
    public void updateStudent(User user, String name, String surname, String patronymic,
                              String email, String phone, String username, String password,
                              LocalDate dayOfBirth) {

        if (name != null &&
                name.length() > 0 &&
                !Objects.equals(user.getName(), name)) {
            user.setName(name);
        }
        if (surname != null &&
                surname.length() > 0 &&
                !Objects.equals(user.getSurname(), surname)) {
            user.setSurname(surname);
        }
        if (patronymic != null &&
                patronymic.length() > 0 &&
                !Objects.equals(user.getPatronymic(), patronymic)) {
            user.setPatronymic(patronymic);
        }

        if (dayOfBirth != null &&
                !Objects.equals(user.getDayOfBirth(), dayOfBirth)) {
            user.setDayOfBirth(dayOfBirth);
        }
        if (email != null &&
                email.length() > 0 &&
                !Objects.equals(user.getEmail(), email)) {
            User student
                    = userRepository.findByEmail(email);
            if (student != null) {
                throw new IllegalStateException("Email is taken");
            }
            user.setEmail(email);
        }
        if (phone != null &&
                phone.length() > 0 &&
                !Objects.equals(user.getPhone(), phone)) {
            User student
                    = userRepository.findByPhone(phone);
            if (student != null) {
                throw new IllegalStateException("Phone is taken");
            }
            user.setPhone(phone);
        }
        if (username != null &&
                username.length() > 0 &&
                !Objects.equals(user.getUsername(), username)) {
            User anotherUser
                    = userRepository.findByUsername(username);
            if (anotherUser != null) {
                throw new IllegalStateException("Username is taken");
            }
            user.setUsername(username);
        }
        if (password != null &&
                password.length() > 0 &&
                !Objects.equals(user.getPassword(), password)) {
            user.setPassword(password);
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        String role = user.getRole();
       /* if (username.endsWith("admin")) {
            role = "ADMIN";
        } else if (username.endsWith("leader")) {
            role = "LEADER";
        } else {
            role = "STUDENT";
        }*/
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        if (role.equals("ADMIN")) {
            for (SimpleGrantedAuthority s : UserRole.ADMIN.getGrantedAuthorities()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(s.getAuthority()));
            }
            user.setGrantedAuthorities(UserRole.ADMIN.getGrantedAuthorities());
        } else if (role.equals("LEADER")) {
            for (SimpleGrantedAuthority s : UserRole.LEADER.getGrantedAuthorities()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(s.getAuthority()));
            }
            user.setGrantedAuthorities(UserRole.LEADER.getGrantedAuthorities());
        } else {
            for (SimpleGrantedAuthority s : UserRole.STUDENT.getGrantedAuthorities()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(s.getAuthority()));
            }
            user.setGrantedAuthorities(UserRole.STUDENT.getGrantedAuthorities());
        }
        String password = passwordEncoder.encode(user.getPassword());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), password, grantedAuthorities);
    }
}