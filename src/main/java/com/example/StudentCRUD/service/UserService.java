package com.example.StudentCRUD.service;

import com.example.StudentCRUD.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService{
    public List<UserDTO> getAllUsers();
    public ResponseEntity<String> createNewUser(UserDTO user);
    public ResponseEntity<String> saveNewUser(UserDTO user);
    public ResponseEntity<String> deleteUserById(Long id);
    public ResponseEntity<String> updateUser(UserDTO userDTO);
    public ResponseEntity<UserDTO> getUserById(Long id);
}
