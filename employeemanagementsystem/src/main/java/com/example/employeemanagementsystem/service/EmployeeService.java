package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.EmployeeDTO;
import com.example.employeemanagementsystem.dto.EmployeeStatsResponse;
import org.springframework.data.domain.Pageable;


public interface EmployeeService {
    public EmployeeStatsResponse getEmployeesByNameContainingIgnoreCase(String name, Pageable pageable);
    public EmployeeDTO getEmployeeById(Long id);
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO);
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO);
    public EmployeeDTO deleteEmployee(EmployeeDTO employeeDTO);
    public EmployeeStatsResponse getEmployeesWithStats(Long deptId, Integer minAge, Integer maxAge, Pageable pageable);
}
