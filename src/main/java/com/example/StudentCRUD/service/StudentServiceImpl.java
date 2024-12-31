package com.example.StudentCRUD.service;

import com.example.StudentCRUD.dto.StudentDTO;
import com.example.StudentCRUD.entity.Student;
import com.example.StudentCRUD.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    public StudentServiceImpl(StudentRepository studentRepository,ModelMapper modelMapper){
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        try {
            List<StudentDTO> studentList = studentRepository.findAll()
                    .stream()
                    .map(studentEntity -> modelMapper.map(studentEntity, StudentDTO.class))
                    .collect(Collectors.toList());
            log.info("Retrieved {} students successfully.", studentList.size());
            return new ResponseEntity<>(studentList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to retrieve students: {}", e.getMessage(), e);
            return new ResponseEntity<>(List.of(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> createNewStudent(StudentDTO student) {
        try {
            if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
                log.warn("Attempted to create a student with an existing email: {}", student.getEmail());
                return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
            }

            Student studentEntity = modelMapper.map(student, Student.class);
            Student savedStudent = studentRepository.save(studentEntity);
            log.info("Student created successfully with ID: {}", savedStudent.getId());
            return new ResponseEntity<>("Student created with ID: " + savedStudent.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create student: {}", e.getMessage(), e);
            return new ResponseEntity<>("Failed to create student", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteStudentById(Long id) {
        try {
            if (studentRepository.existsById(id)) {
                studentRepository.deleteById(id);
                log.info("Student with ID {} deleted successfully.", id);
                return new ResponseEntity<>("Student deleted successfully.", HttpStatus.OK);
            } else {
                log.warn("Attempted to delete a non-existing student with ID: {}", id);
                return new ResponseEntity<>("Student not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Failed to delete student with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("An error occurred while deleting the student.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateStudentById(Long id, StudentDTO studentDTO) {
        try {
            if (studentRepository.existsById(id)) {
                Student existingStudent = studentRepository.findById(id).orElseThrow(() ->
                        new RuntimeException("Student with ID " + id + " not found."));

                existingStudent.setName(studentDTO.getName());
                existingStudent.setAge(studentDTO.getAge());
                existingStudent.setEmail(studentDTO.getEmail());
                existingStudent.setDob(studentDTO.getDob());

                studentRepository.save(existingStudent);
                log.info("Student with ID {} updated successfully.", id);
                return new ResponseEntity<>("Student details updated successfully.", HttpStatus.OK);
            } else {
                log.warn("Attempted to update a non-existing student with ID: {}", id);
                return new ResponseEntity<>("Student not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Failed to update student with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("An error occurred while updating the student details.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<StudentDTO> getStudentById(Long id) {
        try {
            return studentRepository.findById(id)
                    .map(student -> {
                        StudentDTO studentDTO = modelMapper.map(student, StudentDTO.class);
                        log.info("Student with ID {} retrieved successfully.", id);
                        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        log.warn("Student with ID {} not found.", id);
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    });
        } catch (Exception e) {
            log.error("Failed to retrieve student with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
