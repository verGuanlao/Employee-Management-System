package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.dto.ApiResponse;
import com.example.employeemanagementsystem.dto.DepartmentDTO;
import com.example.employeemanagementsystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DepartmentDTO>>> getDepartments(Pageable pageable) {
        ApiResponse<Page<DepartmentDTO>> apiResponse = ApiResponse.<Page<DepartmentDTO>>builder()
                .status("success")
                .message("Departments fetched successfully")
                .data(departmentService.getDepartments(pageable))
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<DepartmentDTO>>> getDepartmentsNameContainingIgnoreCase(
            @RequestParam(required = false) String query, Pageable pageable) {
        ApiResponse<Page<DepartmentDTO>> apiResponse = ApiResponse.<Page<DepartmentDTO>>builder()
                .status("success")
                .message("Departments search successfully")
                .data(departmentService.getDepartmentsNameContainingIgnoreCase(query, pageable))
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
