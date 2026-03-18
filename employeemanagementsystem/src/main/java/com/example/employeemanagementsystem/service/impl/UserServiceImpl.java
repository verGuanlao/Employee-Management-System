package com.example.employeemanagementsystem.service.impl;

import com.example.employeemanagementsystem.config.JwtService;
import com.example.employeemanagementsystem.dto.UserDTO;
import com.example.employeemanagementsystem.exception.MissingFieldsException;
import com.example.employeemanagementsystem.model.User;
import com.example.employeemanagementsystem.repository.UserRepository;
import com.example.employeemanagementsystem.service.UserService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public User getByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    public User registerUser(UserDTO userDTO) {

        if (userDTO.getUsername() == null || userDTO.getPassword() == null) {
            throw new MissingFieldsException();
        }
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // hash password
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        } else {
            user.setRole("ROLE_USER");
        }
        user = userRepository.save(user);
        return user;
    }

    public String login(UserDTO userDTO) {
        User dbUser = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(userDTO.getPassword(), dbUser.getPassword())) {
            return jwtService.generateToken(dbUser.getUsername(), dbUser.getRole());
        }
        throw new RuntimeException("Invalid credentials");
    }
}
