package com.example.employeemanagementsystem.exception;

public class DepartmentAlreadyExistsException extends RuntimeException{
    public DepartmentAlreadyExistsException(String departmentName) {
        super("Department " + departmentName + " already exists");
    }
}
