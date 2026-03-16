package com.example.employeemanagementsystem.service.impl;

import com.example.employeemanagementsystem.dto.DepartmentDTO;
import com.example.employeemanagementsystem.exception.DepartmentNotFoundException;
import com.example.employeemanagementsystem.model.Department;
import com.example.employeemanagementsystem.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl {
    @Autowired
    private DepartmentRepository departmentRepository;

    public String getDepartmentName(Long id) {
        Department department = departmentRepository.findById(id).orElse(null);
        if (department == null) {
            throw new DepartmentNotFoundException(id);
        }
        return department.getDepartmentName();
    }

    public DepartmentDTO getDepartmentId(String name) {
        Department department = departmentRepository.findByDepartmentName(name).orElse(null);
        if (department == null) {
            throw new DepartmentNotFoundException(name);
        }
        return new DepartmentDTO(department.getDepartmentId(), department.getDepartmentName());
    }
}
