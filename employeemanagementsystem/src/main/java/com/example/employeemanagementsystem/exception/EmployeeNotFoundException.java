package com.example.employeemanagementsystem.exception;

import com.example.employeemanagementsystem.component.MessageHelper;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super(MessageHelper.get("error.employee.not.found.id", id));
    }

    public EmployeeNotFoundException(String name) {
        super(MessageHelper.get("error.employee.not.found.name", name));
    }
}
