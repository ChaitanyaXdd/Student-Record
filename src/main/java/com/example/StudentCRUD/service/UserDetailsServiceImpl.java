package com.example.StudentCRUD.service;

import com.example.StudentCRUD.entity.User;
import com.example.StudentCRUD.entity.UserPrincipal;
import com.example.StudentCRUD.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);
        if (user == null){
            throw new UsernameNotFoundException("User Not Found.");
        }
        return new UserPrincipal(user);
    }
}
