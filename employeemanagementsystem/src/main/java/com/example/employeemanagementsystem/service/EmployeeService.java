package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.EmployeeDTO;
import com.example.employeemanagementsystem.dto.EmployeeStatsResponse;
import com.example.employeemanagementsystem.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface EmployeeService {
    public EmployeeDTO getEmployeeByName(String name);
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO);
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO);
    public EmployeeDTO deleteEmployee(EmployeeDTO employeeDTO);
    public EmployeeStatsResponse getEmployeesWithStats(Long deptId, Integer minAge, Integer maxAge, Pageable pageable);

}
