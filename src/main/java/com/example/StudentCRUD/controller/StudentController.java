package com.example.StudentCRUD.controller;

import com.example.StudentCRUD.dto.StudentDTO;
import com.example.StudentCRUD.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getStudents(){
        return studentService.getAllStudents();
    }

    @PostMapping
    public ResponseEntity<String> createNewStudent(@RequestBody StudentDTO student){
        return studentService.createNewStudent(student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable("id") Long id){
        return studentService.deleteStudentById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudentById(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        return studentService.updateStudentById(id, studentDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id){
        return studentService.getStudentById(id);
    }

}
