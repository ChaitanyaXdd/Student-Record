package com.example.StudentCRUD.service;

import com.example.StudentCRUD.dto.StudentDTO;
import com.example.StudentCRUD.entity.Student;
import com.example.StudentCRUD.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public StudentServiceImpl(StudentRepository studentRepository,ModelMapper modelMapper){
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<List<StudentDTO>> getAllStudents(){
        try {
            List<StudentDTO> studentList = studentRepository.findAll()
                    .stream()
                    .map(studentEntity -> modelMapper.map(studentEntity, StudentDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(studentList, HttpStatus.OK);
        } catch (Exception e) {
            // Use a proper logging framework
            e.printStackTrace();
            return new ResponseEntity<>(List.of(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> createNewStudent(StudentDTO student) {
        try {
            Student studentEntity = modelMapper.map(student, Student.class);
            Student savedStudent = studentRepository.save(studentEntity);
            return new ResponseEntity<>("Student created with ID: " + savedStudent.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create student", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteStudentById(Long id) {
        try {
            if (studentRepository.existsById(id)) {
                studentRepository.deleteById(id);
                return new ResponseEntity<>("Student  deleted successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Student  not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while deleting the student.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateStudentById(Long id, StudentDTO studentDTO) {
        try {

            if (studentRepository.existsById(id)) {

                Student existingStudent = studentRepository.findById(id).orElseThrow(() ->
                        new RuntimeException("Student with ID " + id + " not found."));
                existingStudent.setName(existingStudent.getName());
                existingStudent.setAge(existingStudent.getAge());
                existingStudent.setEmail(studentDTO.getEmail());
                existingStudent.setDob(existingStudent.getDob());

                studentRepository.save(existingStudent);

                return new ResponseEntity<>("Student details updated successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Student not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while updating the student details.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<StudentDTO> getStudentById(Long id) {
        try {
            return studentRepository.findById(id)
                    .map(student -> {
                        StudentDTO studentDTO = modelMapper.map(student, StudentDTO.class);
                        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
                    })
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
