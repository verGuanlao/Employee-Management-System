package com.example.employeemanagementsystem.exception;

import com.example.employeemanagementsystem.component.MessageHelper;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(MessageHelper.get("error.user.not.found.id", id));
    }
    public UserNotFoundException(String username) {
        super(MessageHelper.get("error.user.not.found.name", username));
    }
}