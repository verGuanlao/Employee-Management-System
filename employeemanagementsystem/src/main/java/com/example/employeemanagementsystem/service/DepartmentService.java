package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.DepartmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    public boolean departmentExistsByName(String name);
    public DepartmentDTO getDepartmentName(Long id);
    public DepartmentDTO getDepartmentId(String name);
    public Page<DepartmentDTO> getDepartments(Pageable pageable);
    public Page<DepartmentDTO> getDepartmentsNameContainingIgnoreCase(String name, Pageable pageable);
    public DepartmentDTO addDepartment(DepartmentDTO departmentDTO);
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO);
    public DepartmentDTO deleteDepartment(DepartmentDTO departmentDTO);
}
