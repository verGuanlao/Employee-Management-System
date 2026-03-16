package com.example.employeemanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Min(value = 0, message = "Salary cannot be negative")
    private BigDecimal employeeSalary;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
