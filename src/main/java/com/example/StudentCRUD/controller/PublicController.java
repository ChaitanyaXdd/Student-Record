package com.example.StudentCRUD.controller;

import com.example.StudentCRUD.dto.UserDTO;
import com.example.StudentCRUD.entity.User;
import com.example.StudentCRUD.service.UserDetailsServiceImpl;
import com.example.StudentCRUD.service.UserServiceImpl;
import com.example.StudentCRUD.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    private final UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private static final Logger log = LoggerFactory.getLogger(PublicController.class);

    public PublicController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @PostMapping("/create-user")
    public ResponseEntity<String> createNewUser(@RequestBody UserDTO userDTO){
        return userService.createNewUser(userDTO);
    }
    @GetMapping
    public String welcome(){
        return "Welcome";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(),user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }
        catch (Exception e){
            log.error("Exception occurred while creating Authentication Token ",e);
            return new ResponseEntity<>("Incorrect UserName or Password",HttpStatus.BAD_REQUEST);
        }
    }


}
