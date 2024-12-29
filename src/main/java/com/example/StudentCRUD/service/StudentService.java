package com.example.StudentCRUD.service;

import com.example.StudentCRUD.dto.StudentDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentService {
    public ResponseEntity<List<StudentDTO>> getAllStudents();
    public ResponseEntity<String> createNewStudent(StudentDTO student);
    public ResponseEntity<String> deleteStudentById(Long id);
    public ResponseEntity<String> updateStudentById(Long id, StudentDTO studentDTO);
    public ResponseEntity<StudentDTO> getStudentById(Long id);
}
