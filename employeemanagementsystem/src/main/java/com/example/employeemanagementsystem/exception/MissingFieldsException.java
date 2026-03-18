package com.example.employeemanagementsystem.exception;

public class MissingFieldsException extends RuntimeException {
    public MissingFieldsException(String message) {
        super(message);
    }

    public MissingFieldsException() {
        super("Fields cannot be empty!");
    }
}
