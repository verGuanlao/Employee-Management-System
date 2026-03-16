package com.example.employeemanagementsystem.exception;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long id) {
        super("Department with ID " + id + " not found");
    }

    public DepartmentNotFoundException(String name) {
        super("Department with name " + name + " not found");
    }
}