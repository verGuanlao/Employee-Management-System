package com.example.employeemanagementsystem.service.impl;

import com.example.employeemanagementsystem.dto.DepartmentDTO;
import com.example.employeemanagementsystem.exception.*;
import com.example.employeemanagementsystem.model.Department;
import com.example.employeemanagementsystem.repository.DepartmentRepository;
import com.example.employeemanagementsystem.repository.EmployeeRepository;
import com.example.employeemanagementsystem.service.DepartmentService;
import com.example.employeemanagementsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

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

    public DepartmentDTO addDepartment(DepartmentDTO departmentDTO) {
        if (departmentDTO.getDepartmentName() == null ||  departmentDTO.getDepartmentName().isBlank()) {
            throw new MissingFieldsException("Department name cannot be empty");
        }
        if (departmentRepository.existsByDepartmentNameIgnoreCase(departmentDTO.getDepartmentName())) {
            throw new DepartmentAlreadyExistsException(departmentDTO.getDepartmentName());
        }
        Department department = new Department();
        department.setDepartmentName(departmentDTO.getDepartmentName());
        Department newDepartment = departmentRepository.save(department);
        return new DepartmentDTO(newDepartment.getDepartmentId(), newDepartment.getDepartmentName());
    }

    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO) {
        if (departmentDTO.getDepartmentName() == null ||  departmentDTO.getDepartmentName().isBlank()) {
            throw new MissingFieldsException("Department name cannot be empty");
        }

        if (departmentDTO.getDepartmentId() == null) {
            throw new MissingFieldsException("Department Id cannot be empty");
        }

        Department updateDepartment = departmentRepository.findById(departmentDTO.getDepartmentId()).orElse(null);
        if (updateDepartment == null) {
            throw new DepartmentNotFoundException(departmentDTO.getDepartmentId());
        }
        if (departmentRepository.existsByDepartmentNameIgnoreCase(departmentDTO.getDepartmentName())) {
            throw new DepartmentAlreadyExistsException(departmentDTO.getDepartmentName());
        }
        updateDepartment.setDepartmentName(departmentDTO.getDepartmentName());
        Department newDepartment = departmentRepository.save(updateDepartment);
        return new DepartmentDTO(newDepartment.getDepartmentId(), newDepartment.getDepartmentName());
    }

    public DepartmentDTO deleteDepartment(DepartmentDTO departmentDTO) {
        if (departmentDTO.getDepartmentId() == null) {
            throw new MissingFieldsException("Department Id cannot be empty");
        }

        Department updateDepartment = departmentRepository.findById(departmentDTO.getDepartmentId()).orElse(null);
        if (updateDepartment == null) {
            throw new DepartmentNotFoundException(departmentDTO.getDepartmentId());
        }
        if ((employeeRepository.existsByDepartmentDepartmentId(departmentDTO.getDepartmentId()))) {
            throw new InvalidDepartmentDeleteException(departmentDTO.getDepartmentId());
        }

        updateDepartment.setDepartmentName(departmentDTO.getDepartmentName());
        DepartmentDTO departmentToDelete = new DepartmentDTO(updateDepartment.getDepartmentId(), updateDepartment.getDepartmentName());
        departmentRepository.delete(updateDepartment);
        return departmentToDelete;
    }

}
