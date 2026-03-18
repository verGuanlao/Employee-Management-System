package com.example.employeemanagementsystem.exception;

public class NonPositiveSalaryException extends RuntimeException {
    public NonPositiveSalaryException() {
        super("Age must be above 18!");
    }
}
