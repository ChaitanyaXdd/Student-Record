package com.example.StudentCRUD.service;

import com.example.StudentCRUD.dto.UserDTO;
import com.example.StudentCRUD.entity.User;
import com.example.StudentCRUD.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userEntity -> modelMapper.map(userEntity, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<String> createNewUser(UserDTO user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            User userEntity = modelMapper.map(user, User.class);
            User savedUser = userRepository.save(userEntity);
            return new ResponseEntity<>("User created with ID: " + savedUser.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> saveNewUser(UserDTO user) {
        try {
//            user.setRoles(Arrays.asList("USER","ADMIN"));
            User userEntity = modelMapper.map(user, User.class);
            User savedUser = userRepository.save(userEntity);
            return new ResponseEntity<>("User created with ID: " + savedUser.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteUserById(Long id) {
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return new ResponseEntity<>("User  deleted successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User  not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while deleting the user.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateUser(UserDTO userDTO) {
        try {

            if (userRepository.existsById(userDTO.getId())) {

                User existingUser = userRepository.findById(userDTO.getId()).orElseThrow(() ->
                        new RuntimeException("User with ID " + userDTO.getId() + " not found."));
                existingUser.setName(userDTO.getName());
                existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//                existingUser.setAge(userDTO.getAge());
//                existingUser.setEmail(userDTO.getEmail());
//                existingUser.setDob(userDTO.getDob());
//                existingUser.setRoles(userDTO.getRoles());

                userRepository.save(existingUser);

                return new ResponseEntity<>("User details updated successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while updating the user details.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long id) {
        try {
            return userRepository.findById(id)
                    .map(user -> {
                        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                        return new ResponseEntity<>(userDTO, HttpStatus.OK);
                    })
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    }

