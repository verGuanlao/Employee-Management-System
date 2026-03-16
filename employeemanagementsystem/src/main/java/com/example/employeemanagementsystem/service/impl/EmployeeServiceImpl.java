package com.example.employeemanagementsystem.service.impl;

import com.example.employeemanagementsystem.dto.DepartmentDTO;
import com.example.employeemanagementsystem.dto.EmployeeDTO;
import com.example.employeemanagementsystem.dto.EmployeeStatsResponse;
import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.model.Department;
import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.repository.EmployeeRepository;
import com.example.employeemanagementsystem.service.DepartmentService;
import com.example.employeemanagementsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentService departmentService;

    private Department getDepartmentStub (String departmentName) {
        Department departmentStub = new Department();
        departmentStub.setDepartmentId(departmentService.getDepartmentId(departmentName).getDepartmentId());
        departmentStub.setDepartmentName(departmentName);
        return departmentStub;
    }

    private Employee dtoToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setDepartment(getDepartmentStub(employeeDTO.getDepartment()));
        employee.setBirthDate(employeeDTO.getBirthDate());
        employee.setEmployeeSalary(employeeDTO.getSalary());
        return employee;
    }

    public EmployeeStatsResponse getEmployeesByNameContainingIgnoreCase(String name, Pageable pageable) {
        Page<Employee> employeesPage = employeeRepository.findByNameContainingIgnoreCase(name,  pageable);
        return EmployeeStatsResponse.builder().employees(employeesPage.map(EmployeeDTO::new)).build();
    }

    public EmployeeDTO getEmployeeById(Long id){
        Employee employee = employeeRepository.findById(id).orElse(null);
        if(employee == null){
            throw new EmployeeNotFoundException(id);
        }
        return new EmployeeDTO(employee);
    }

    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO.areFieldsMissing()) {
            throw new EmployeeNotFoundException();
        }
        Employee newEmployee = employeeRepository.save(dtoToEntity(employeeDTO));
        return new EmployeeDTO(newEmployee);
    }

    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {
        Long employeeId = employeeDTO.getEmployeeId();
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if(employee == null){
            throw new EmployeeNotFoundException(employeeId);
        }
        Employee updatedEmployee = dtoToEntity(employeeDTO);
        updatedEmployee.setName(employeeDTO.getName());
        return new EmployeeDTO(employeeRepository.save(updatedEmployee));
    }
    public EmployeeDTO deleteEmployee(EmployeeDTO employeeDTO) {
        Long employeeId = employeeDTO.getEmployeeId();
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if(employee == null){
            throw new EmployeeNotFoundException(employeeId);
        }
        employeeRepository.delete(employee);
        return new EmployeeDTO(employee);
    }

    public EmployeeStatsResponse getEmployeesWithStats(Long deptId, Integer minAge, Integer maxAge, Pageable pageable) {
            Page<Employee> page;
            Double avgSalary;
            Double avgAge;


            LocalDate now = LocalDate.now();
            LocalDate minBirthDate = (maxAge != null) ? now.minusYears(maxAge) : null;
            LocalDate maxBirthDate = (minAge != null) ? now.minusYears(minAge) : null;

            // page, avgSalary, and avgAge calculation adaptation if some values are null
            if (deptId != null && minAge != null && maxAge != null) {
                page = employeeRepository.findByDepartmentDepartmentIdAndBirthDateBetween(deptId, minBirthDate, maxBirthDate, pageable);
                avgSalary = employeeRepository.calculateAverageSalaryByDepartmentAndAgeRange(deptId, minBirthDate, maxBirthDate);
                avgAge = employeeRepository.calculateAverageAgeByDepartmentAndAgeRange(deptId, minBirthDate, maxBirthDate);
            } else if (deptId != null) {
                page = employeeRepository.findByDepartmentDepartmentId(deptId, pageable);
                avgSalary = employeeRepository.calculateAverageSalaryByDepartment(deptId);
                avgAge = employeeRepository.calculateAverageAgeByDepartment(deptId);
            } else if (minAge != null && maxAge != null) {
                page = employeeRepository.findByBirthDateBetween(minBirthDate, maxBirthDate, pageable);
                avgSalary = employeeRepository.calculateAverageSalaryByAgeRange(minBirthDate, maxBirthDate);
                avgAge = employeeRepository.calculateAverageAgeByRange(minBirthDate, maxBirthDate);
            } else {
                page = employeeRepository.findAll(pageable);
                avgSalary = employeeRepository.calculateAverageSalaryAll();
                avgAge = employeeRepository.calculateAverageAgeAll();
            }

            Map<String, Object> stats = new HashMap<>();
            stats.put("averageSalary", avgSalary);
            stats.put("averageAge", avgAge);

            return EmployeeStatsResponse.builder()
                    .employees(page.map(EmployeeDTO::new))
                    .stats(stats)
                    .build();
    }
}
