package com.example.employeemanagementsystem.exception;

public class ImproperAgeException extends RuntimeException {
    public ImproperAgeException() {
        super("Age must be above 18!");
    }
}
