package com.example.employeemanagementsystem.exception;

public class NonPositiveSalaryException extends RuntimeException {
    public NonPositiveSalaryException() {
        super("Salary must be a positive number");
    }
}
