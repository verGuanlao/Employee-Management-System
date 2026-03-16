package com.example.employeemanagementsystem.dto;

import com.example.employeemanagementsystem.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDTO {
    private Long employeeId;
    private String name;
    private LocalDate birthDate;
    private String department;
    private BigDecimal salary;

    public EmployeeDTO(Employee employee) {
        this.employeeId = employee.getEmployeeId();
        this.name = employee.getName();
        this.birthDate = employee.getBirthDate();
        this.department = employee.getDepartment().getDepartmentName();
        this.salary = employee.getEmployeeSalary();
    }

    public boolean areFieldsMissing() {
        return name.isEmpty() || birthDate == null || salary == null || department.isEmpty();
    }
}
