package com.example.StudentCRUD.service;

import com.example.StudentCRUD.entity.User;
import com.example.StudentCRUD.entity.UserPrincipal;
import com.example.StudentCRUD.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by username: {}", username);
        User user = userRepository.findByName(username);
        if (user == null) {
            log.warn("User with username '{}' not found in the database.", username);
            throw new UsernameNotFoundException("User Not Found.");
        }
        log.info("User with username '{}' found. Proceeding to create UserPrincipal.", username);
        return new UserPrincipal(user);
    }
}
