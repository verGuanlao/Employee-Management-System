package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.dto.ApiResponse;
import com.example.employeemanagementsystem.dto.DepartmentDTO;
import com.example.employeemanagementsystem.dto.EmployeeDTO;
import com.example.employeemanagementsystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentDTO>> addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        ApiResponse<DepartmentDTO> apiResponse = ApiResponse.<DepartmentDTO>builder()
                .status("success")
                .message("Department added successfully")
                .data(departmentService.addDepartment(departmentDTO)).build();
        return  ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentDTO>> updateDepartment(@RequestBody DepartmentDTO departmentDTO) {
        ApiResponse<DepartmentDTO> apiResponse = ApiResponse.<DepartmentDTO>builder()
                .status("success")
                .message("Department updated successfully")
                .data(departmentService.updateDepartment(departmentDTO)).build();
        return  ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentDTO>> deleteDepartment(@RequestBody DepartmentDTO departmentDTO) {
        ApiResponse<DepartmentDTO> apiResponse = ApiResponse.<DepartmentDTO>builder()
                .status("success")
                .message("Department deleted successfully")
                .data(departmentService.deleteDepartment(departmentDTO)).build();
        return  ResponseEntity.ok().body(apiResponse);
    }
}
