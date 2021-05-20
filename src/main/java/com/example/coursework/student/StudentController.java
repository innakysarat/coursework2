package com.example.coursework.student;

import com.example.coursework.internships.Internship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("api/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ДОБАВИТЬ ВЗЯТИЕ ОРГАНИЗАЦИЙ РУКОВОДИТЕЛЯ
    @CrossOrigin
    @GetMapping
    public User getInfoAboutMyself() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        User user;
        if (!Objects.equals(username, "anonymousUser")) {
            user = studentService.getUser(username);
        } else {
            throw new IllegalStateException("User not found");
        }
        return user;
    }

    @CrossOrigin
    @PostMapping
    public void registerNewStudent(@RequestBody User user) {
        studentService.addUser(user);
    }

    @CrossOrigin
    @PutMapping(path = "/{user_id}")
    public void updateInfo(@PathVariable Integer user_id,
                           @RequestBody User user_update) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            studentService.updateStudentByHimself(user_id, username, user_update.getName(), user_update.getSurname(),
                    user_update.getPatronymic(), user_update.getEmail(), user_update.getPhone(),
                    user_update.getUsername(), user_update.getPassword(),
                    user_update.getDayOfBirth());
        }
    }

    @CrossOrigin
    @GetMapping("/favourites")
    public Set<Internship> getFavourites(
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            return studentService.getFavourites(username);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found"
            );
        }
    }

    @CrossOrigin
    @DeleteMapping(path = "/{user_id}")
    public void deleteStudent(@PathVariable("user_id") Integer studentId) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            studentService.deleteStudent(studentId, username);
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User must login"
            );
        }
    }

    @CrossOrigin
    @PostMapping(
            path = "/{user_id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserImage(@PathVariable("user_id") Integer user_id,
                                @RequestParam("file") MultipartFile file) {
        studentService.uploadUserImage(user_id, file);
    }

    @CrossOrigin
    @GetMapping(path = "/{user_id}/image")
    public byte[] downloadUserImage(@PathVariable("user_id") Integer user_id) {
        return studentService.downloadUserImage(user_id);
    }

    @CrossOrigin
    @DeleteMapping(path = "/{user_id}/image")
    public void deleteFile(@PathVariable Integer user_id) {
        studentService.deleteImage(user_id);
    }

}
