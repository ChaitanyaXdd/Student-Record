package com.example.StudentCRUD.controller;

import com.example.StudentCRUD.dto.UserDTO;
import com.example.StudentCRUD.repository.UserRepository;
import com.example.StudentCRUD.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;



    public UserController(UserService userService) {
        this.userService = userService;
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return userService.deleteUserById(id);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userService.updateUser(userDTO);
    }



}
