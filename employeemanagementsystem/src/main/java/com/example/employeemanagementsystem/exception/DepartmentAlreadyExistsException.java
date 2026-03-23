package com.example.employeemanagementsystem.exception;

import com.example.employeemanagementsystem.component.MessageHelper;

public class DepartmentAlreadyExistsException extends RuntimeException{
    public DepartmentAlreadyExistsException(String departmentName) {
        super(MessageHelper.get("error.department.already.exists", departmentName));
    }
}
