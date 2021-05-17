package com.example.coursework.student;

import com.example.coursework.organizations.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("management/api/students")
public class StudentManagementController {
    private final StudentService studentService;

    @Autowired
    public StudentManagementController(StudentService studentService) {
        this.studentService = studentService;
    }

    @CrossOrigin
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_LEADER')")
    public List<User> getAllStudents() {
        return studentService.getUsers();
    }

    @CrossOrigin
    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void registerNewStudent(@RequestBody User user) {
        studentService.addUser(user);
    }

    @CrossOrigin
    @DeleteMapping(path = "/{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            studentService.deleteStudent(studentId, username);
        }
    }

    @CrossOrigin
    @PutMapping(path = "/{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody User user) {
        studentService.updateStudentByAdmin(studentId, user.getName(), user.getSurname(), user.getPatronymic(),
                user.getEmail(), user.getPhone(), user.getUsername(), user.getPassword(), user.getDayOfBirth());
    }

    @CrossOrigin
    @GetMapping(path = "/organizations/{user_id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_LEADER')")
    public Set<Organization> getAllOrganizations(@PathVariable Integer user_id) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            return studentService.getOrganizations(user_id, username);
        } else {
            throw new IllegalStateException("User must login");
        }
    }

}
