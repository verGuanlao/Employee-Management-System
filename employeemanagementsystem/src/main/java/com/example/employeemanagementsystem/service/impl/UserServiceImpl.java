package com.example.employeemanagementsystem.service.impl;

import com.example.employeemanagementsystem.model.User;
import com.example.employeemanagementsystem.repository.UserRepository;
import com.example.employeemanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public User getByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

}
