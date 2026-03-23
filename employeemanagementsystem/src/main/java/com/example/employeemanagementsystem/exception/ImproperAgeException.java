package com.example.employeemanagementsystem.exception;

import com.example.employeemanagementsystem.component.MessageHelper;

public class ImproperAgeException extends RuntimeException {
    public ImproperAgeException() {
        super(MessageHelper.get("error.employee.underage"));
    }
}