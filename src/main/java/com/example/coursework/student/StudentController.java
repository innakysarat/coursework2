package com.example.coursework.student;

import com.example.coursework.internships.Internship;
import com.example.coursework.internships.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("api/students")
public class StudentController {
    private final StudentService studentService;
    private final InternshipService internshipService;

    @Autowired
    public StudentController(StudentService studentService, InternshipService internshipService) {
        this.studentService = studentService;
        this.internshipService = internshipService;
    }

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
    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping
    public void registerNewStudent(@RequestBody User user) {
        studentService.addUser(user);
    }

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

    @GetMapping("/favourites")
    public Set<Internship> getFavourites(
            @RequestParam(value = "username") String username
    ) {
        return studentService.getFavourites(username);
    }

    @PostMapping(
            path = "/{user_id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserImage(@PathVariable("user_id") Integer user_id,
                                @RequestParam("file") MultipartFile file) {
        studentService.uploadUserImage(user_id, file);
    }

    @GetMapping(path = "/{user_id}/image")
    public byte[] downloadUserImage(@PathVariable("user_id") Integer user_id) {
        return studentService.downloadUserImage(user_id);
    }
    @DeleteMapping(path = "/{user_id}/image")
    public void deleteFile(@PathVariable Integer user_id) {
        studentService.deleteImage(user_id);
    }

}
