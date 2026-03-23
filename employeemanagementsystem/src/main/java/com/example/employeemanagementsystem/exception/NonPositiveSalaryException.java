package com.example.employeemanagementsystem.exception;

import com.example.employeemanagementsystem.component.MessageHelper;

public class NonPositiveSalaryException extends RuntimeException {
    public NonPositiveSalaryException() {
        super(MessageHelper.get("error.salary.non.positive"));
    }
}