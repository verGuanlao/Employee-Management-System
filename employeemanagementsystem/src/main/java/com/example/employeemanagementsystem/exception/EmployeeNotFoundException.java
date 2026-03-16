package com.example.employeemanagementsystem.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {

        super("Employee with ID " + id + " not found");
    }

    public EmployeeNotFoundException(String name) {
        super("Employee with name " + name + " not found");
    }

    public EmployeeNotFoundException() {
        super("Employee fields are missing");
    }
}
