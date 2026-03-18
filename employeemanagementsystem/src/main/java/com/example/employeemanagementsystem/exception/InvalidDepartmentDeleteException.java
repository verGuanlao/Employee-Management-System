package com.example.employeemanagementsystem.exception;

public class InvalidDepartmentDeleteException extends RuntimeException {
    public InvalidDepartmentDeleteException(Long departmentId) {
        super("The department with id " + departmentId + " has existing employees");
    }
}
