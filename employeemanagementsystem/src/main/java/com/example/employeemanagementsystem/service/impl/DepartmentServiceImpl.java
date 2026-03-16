package com.example.employeemanagementsystem.service.impl;

import com.example.employeemanagementsystem.dto.DepartmentDTO;
import com.example.employeemanagementsystem.exception.DepartmentNotFoundException;
import com.example.employeemanagementsystem.model.Department;
import com.example.employeemanagementsystem.repository.DepartmentRepository;
import com.example.employeemanagementsystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public DepartmentDTO getDepartmentName(Long id) {
        Department department = departmentRepository.findById(id).orElse(null);
        if (department == null) {
            throw new DepartmentNotFoundException(id);
        }
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentName(department.getDepartmentName());
        return departmentDTO;
    }

    public DepartmentDTO getDepartmentId(String name) {
        Department department = departmentRepository.findByDepartmentName(name).orElse(null);
        if (department == null) {
            throw new DepartmentNotFoundException(name);
        }
        return new DepartmentDTO(department.getDepartmentId(), department.getDepartmentName());
    }

    public Page<DepartmentDTO> getDepartments(Pageable pageable) {
        Page<Department> departmentsPage = departmentRepository.findAll(pageable);
        return departmentsPage.map(department ->
                new DepartmentDTO(
                        department.getDepartmentId(),
                        department.getDepartmentName()
                )
        );
    }

    public Page<DepartmentDTO> getDepartmentsNameContainingIgnoreCase(String name, Pageable pageable) {
        Page<Department> departmentsPage =
                departmentRepository.findByDepartmentNameContainingIgnoreCase(name, pageable);

        return departmentsPage.map(department ->
                new DepartmentDTO(
                        department.getDepartmentId(),
                        department.getDepartmentName()
                )
        );
    }

}
