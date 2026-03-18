package com.example.employeemanagementsystem.exception;

import com.example.employeemanagementsystem.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("error")
                .message(ex.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDepartmentNotFound(DepartmentNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("error")
                .message(ex.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingFieldsException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingFields(MissingFieldsException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("error")
                .message(ex.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("error")
                .message("Internal server error: " + ex.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}