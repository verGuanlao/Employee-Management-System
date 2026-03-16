package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.DepartmentDTO;
import com.example.employeemanagementsystem.model.Department;

public interface DepartmentService {
    public DepartmentDTO getDepartmentName(Integer id);
    public DepartmentDTO getDepartmentId(String name);
}
