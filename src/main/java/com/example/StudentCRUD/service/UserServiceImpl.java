package com.example.StudentCRUD.service;

import com.example.StudentCRUD.dto.UserDTO;
import com.example.StudentCRUD.entity.User;
import com.example.StudentCRUD.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);



    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        try {
            List<UserDTO> users = userRepository.findAll()
                    .stream()
                    .map(userEntity -> modelMapper.map(userEntity, UserDTO.class))
                    .collect(Collectors.toList());
            log.info("Successfully retrieved {} users.", users.size());
            return users;
        } catch (Exception e) {
            log.error("Error while retrieving all users: {}", e.getMessage(), e);
            throw new RuntimeException("Error retrieving users.");
        }
    }

    @Override
    public ResponseEntity<String> createNewUser(UserDTO user) {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                log.warn("Attempted to create a user with an existing email: {}", user.getEmail());
                return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            User userEntity = modelMapper.map(user, User.class);
            User savedUser = userRepository.save(userEntity);
            log.info("User created successfully with ID: {}", savedUser.getId());
            return new ResponseEntity<>("User created with ID: " + savedUser.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create user: {}", e.getMessage(), e);
            return new ResponseEntity<>("Failed to create user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> saveNewUser(UserDTO user) {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                log.warn("Attempted to create an admin user with an existing email: {}", user.getEmail());
                return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER", "ADMIN"));
            User userEntity = modelMapper.map(user, User.class);
            User savedUser = userRepository.save(userEntity);

            log.info("Admin user created successfully with ID: {}", savedUser.getId());
            return new ResponseEntity<>("Admin User created with ID: " + savedUser.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating an admin user: {}", e.getMessage(), e);
            return new ResponseEntity<>("Failed to create admin user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<String> deleteUserById(Long id) {
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                log.info("User with ID {} deleted successfully.", id);
                return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
            } else {
                log.warn("Attempted to delete a non-existing user with ID: {}", id);
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting user with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("An error occurred while deleting the user.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateUser(UserDTO userDTO) {
        try {
            if (userDTO.getId() == null) {
                log.warn("User ID is null in the update request.");
                return new ResponseEntity<>("User ID must not be null.", HttpStatus.BAD_REQUEST);
            }

            if (userRepository.existsById(userDTO.getId())) {
                User existingUser = userRepository.findById(userDTO.getId()).orElseThrow(() ->
                        new RuntimeException("User with ID " + userDTO.getId() + " not found."));

                existingUser.setId(userDTO.getId());
                existingUser.setName(userDTO.getName());
                existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                // Uncomment and update as needed for additional fields
                // existingUser.setAge(userDTO.getAge());
                // existingUser.setEmail(userDTO.getEmail());
                // existingUser.setDob(userDTO.getDob());
                // existingUser.setRoles(userDTO.getRoles());

                userRepository.save(existingUser);
                log.info("User with ID {} updated successfully.", userDTO.getId());
                return new ResponseEntity<>("User details updated successfully.", HttpStatus.OK);
            } else {
                log.warn("Attempted to update a non-existing user with ID: {}", userDTO.getId());
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error occurred while updating user with ID {}: {}", userDTO.getId(), e.getMessage(), e);
            return new ResponseEntity<>("An error occurred while updating the user details.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long id) {
        try {
            return userRepository.findById(id)
                    .map(user -> {
                        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                        log.info("User with ID {} retrieved successfully.", id);
                        return new ResponseEntity<>(userDTO, HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        log.warn("User with ID {} not found.", id);
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    });
        } catch (Exception e) {
            log.error("Error occurred while retrieving user with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


