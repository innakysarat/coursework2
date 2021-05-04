package com.example.coursework.student;

import com.example.coursework.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {
    private final StudentService studentService;
   // @Autowired
   //  private StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

  /*  @GetMapping
    public List<User> getStudents() {
        return null;
        // return studentService.getStudents();
    }*/

    @PostMapping(path = "/registration")
    public void registerNewStudent(@RequestBody User user) {
        studentService.addNewStudent(user);
    }
/*
    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
       // studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(
            @PathVariable("studentId") Integer studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {

     //   studentService.updateStudent(studentId, name, email);
    }*/

}
