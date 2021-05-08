package com.example.coursework.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping()
    public User getInfoAboutMyself() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        User user;
        if (username != null) {
            user = studentService.getUser(username);
        } else {
            throw new IllegalStateException("User is not found");
        }
        return user;
    }

    @PostMapping()
    public void registerNewStudent(@RequestBody User user) {
        studentService.addUser(user);
    }

    @PutMapping(path = "/{user_id}")
    public void updateInfo(@PathVariable Integer user_id,
                           @RequestBody User user_update) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (username != null) {
            studentService.updateStudentByHimself(user_id, username, user_update.getName(), user_update.getSurname(),
                    user_update.getPatronymic(), user_update.getEmail(), user_update.getPhone(),
                    user_update.getUsername(), user_update.getPassword(),
                    user_update.getDayOfBirth());
        }
    }
}