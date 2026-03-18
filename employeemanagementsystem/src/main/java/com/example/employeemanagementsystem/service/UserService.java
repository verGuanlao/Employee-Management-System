package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.UserDTO;
import com.example.employeemanagementsystem.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    public User getByUsername(String username);
    public User registerUser(UserDTO userDTO);
    public String login(UserDTO userDTO);
}
