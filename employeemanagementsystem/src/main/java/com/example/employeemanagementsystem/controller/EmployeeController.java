package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.dto.ApiResponse;
import com.example.employeemanagementsystem.dto.EmployeeDTO;
import com.example.employeemanagementsystem.dto.EmployeeStatsResponse;
import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ApiResponse<EmployeeStatsResponse>> getEmployeeStats(
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            Pageable pageable) {

        EmployeeStatsResponse employeeStatsResponse = employeeService.getEmployeesWithStats(
                deptId, minAge, maxAge, pageable);

        ApiResponse<EmployeeStatsResponse> apiResponse = ApiResponse.<EmployeeStatsResponse>builder()
                .status("success")
                .message("Employee and stats fetched successfully")
                .data(employeeStatsResponse).build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable Long employeeId) {
        ApiResponse<EmployeeDTO> apiResponse = ApiResponse.<EmployeeDTO>builder()
            .status("success")
            .message("Employee fetched successfully")
            .data(employeeService.getEmployeeById(employeeId)).build();
        return  ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<EmployeeStatsResponse>> getEmployeeStatsByNameContainingIgnoreCase(
            @RequestParam(required = false) String query, Pageable pageable) {
        ApiResponse<EmployeeStatsResponse> apiResponse = ApiResponse.<EmployeeStatsResponse>builder()
                .status("success")
                .message("Employee searched successfully")
                .data(employeeService.getEmployeesByNameContainingIgnoreCase(query, pageable)).build();
        return  ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        ApiResponse<EmployeeDTO> apiResponse = ApiResponse.<EmployeeDTO>builder()
                .status("success")
                .message("Employee added successfully")
                .data(employeeService.addEmployee(employeeDTO)).build();
        return  ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        ApiResponse<EmployeeDTO> apiResponse = ApiResponse.<EmployeeDTO>builder()
                .status("success")
                .message("Employee updated successfully")
                .data(employeeService.updateEmployee(employeeDTO)).build();
        return  ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> deleteEmployee(@RequestBody EmployeeDTO employeeDTO) {
        ApiResponse<EmployeeDTO> apiResponse = ApiResponse.<EmployeeDTO>builder()
                .status("success")
                .message("Employee deleted successfully")
                .data(employeeService.deleteEmployee(employeeDTO)).build();
        return  ResponseEntity.ok(apiResponse);
    }
}
