package com.example.employeemanagementsystem.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User with id " +  userId + " not found");
    }

    public UserNotFoundException(String username) {
        super("User with name " + username + " not found");
    }
}
