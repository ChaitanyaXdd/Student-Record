package com.example.StudentCRUD.controller;

import com.example.StudentCRUD.dto.UserDTO;
import com.example.StudentCRUD.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    private final UserServiceImpl userService;

    public PublicController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Ok";
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createNewUser(@RequestBody UserDTO userDTO){
        return userService.createNewUser(userDTO);
    }

}
