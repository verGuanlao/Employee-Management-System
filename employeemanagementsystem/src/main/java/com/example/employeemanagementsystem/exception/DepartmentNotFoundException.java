package com.example.employeemanagementsystem.exception;

import com.example.employeemanagementsystem.component.MessageHelper;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long id) {
        super(MessageHelper.get("error.department.not.found.id", id));
    }
    public DepartmentNotFoundException(String name) {
        super(MessageHelper.get("error.department.not.found.name", name));
    }
}