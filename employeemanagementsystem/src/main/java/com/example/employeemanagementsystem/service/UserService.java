package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.UserDTO;
import com.example.employeemanagementsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    public User getByUsername(String username);
    public UserDTO registerUser(UserDTO userDTO);
    public String login(UserDTO userDTO);
    public UserDTO updateUser(UserDTO userDTO);
    public UserDTO deleteUser(UserDTO userDTO);
    public Page<UserDTO> findAllUsers(Pageable pageable);
    public Page<UserDTO> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
