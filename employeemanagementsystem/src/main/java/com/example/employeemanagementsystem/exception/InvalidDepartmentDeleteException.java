package com.example.employeemanagementsystem.exception;

import com.example.employeemanagementsystem.component.MessageHelper;

public class InvalidDepartmentDeleteException extends RuntimeException {
    public InvalidDepartmentDeleteException(String name) {
        super(MessageHelper.get("error.department.has.employees", name));
    }
}