package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.component.MessageHelper;
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

@RestController
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
        ApiResponse<EmployeeStatsResponse> apiResponse = ApiResponse.<EmployeeStatsResponse>builder()
                .status("success")
                .message(MessageHelper.get("success.employee.stats.fetched"))
                .data(employeeService.getEmployeesWithStats(deptId, minAge, maxAge, pageable))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable Long employeeId) {
        ApiResponse<EmployeeDTO> apiResponse = ApiResponse.<EmployeeDTO>builder()
                .status("success")
                .message(MessageHelper.get("success.employee.fetched"))
                .data(employeeService.getEmployeeById(employeeId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<EmployeeStatsResponse>> getEmployeeStatsByNameContainingIgnoreCase(
            @RequestParam(required = false) String query, Pageable pageable) {
        ApiResponse<EmployeeStatsResponse> apiResponse = ApiResponse.<EmployeeStatsResponse>builder()
                .status("success")
                .message(MessageHelper.get("success.employee.searched"))
                .data(employeeService.getEmployeesByNameContainingIgnoreCase(query, pageable))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        ApiResponse<EmployeeDTO> apiResponse = ApiResponse.<EmployeeDTO>builder()
                .status("success")
                .message(MessageHelper.get("success.employee.added"))
                .data(employeeService.addEmployee(employeeDTO))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        ApiResponse<EmployeeDTO> apiResponse = ApiResponse.<EmployeeDTO>builder()
                .status("success")
                .message(MessageHelper.get("success.employee.updated"))
                .data(employeeService.updateEmployee(employeeDTO))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> deleteEmployee(@RequestBody EmployeeDTO employeeDTO) {
        ApiResponse<EmployeeDTO> apiResponse = ApiResponse.<EmployeeDTO>builder()
                .status("success")
                .message(MessageHelper.get("success.employee.deleted"))
                .data(employeeService.deleteEmployee(employeeDTO))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
