package com.example.employeemanagementsystem.exception;

import com.example.employeemanagementsystem.component.MessageHelper;

public class MissingFieldsException extends RuntimeException {

    // Department
    public static final String DEPARTMENT_NAME = "error.department.name.missing";
    public static final String DEPARTMENT_ID   = "error.department.id.missing";

    // Employee
    public static final String EMPLOYEE_ID         = "error.employee.id.missing";
    public static final String EMPLOYEE_NAME       = "error.employee.name.missing";
    public static final String EMPLOYEE_DEPARTMENT = "error.employee.department.missing";
    public static final String EMPLOYEE_BIRTHDATE  = "error.employee.birthdate.missing";
    public static final String EMPLOYEE_SALARY     = "error.employee.salary.missing";

    // User
    public static final String USER_ID       = "error.user.id.missing";
    public static final String USER_USERNAME = "error.user.username.missing";
    public static final String USER_PASSWORD = "error.user.password.missing";
    public static final String USER_ROLE     = "error.user.role.missing";

    public MissingFieldsException(String key) {
        super(MessageHelper.get(key));
    }

    public MissingFieldsException() {
        super(MessageHelper.get("error.missing.fields.default"));
    }
}
