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
import java.util.Set;

import static com.example.employeemanagementsystem.service.impl.DepartmentServiceImpl.ValidationRule.*;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    enum ValidationRule {
        CHECK_NAME,
        CHECK_ID,
        CHECK_EXISTS,
        CHECK_DUPLICATE_NAME,
        CHECK_HAS_EMPLOYEES
    }

    public boolean departmentExistsByName(String name) {
        return departmentRepository.existsByDepartmentNameIgnoreCase(name);
    }

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
        validateDepartment(departmentDTO, CHECK_NAME, CHECK_DUPLICATE_NAME);

        Department department = new Department();
        department.setDepartmentName(departmentDTO.getDepartmentName());
        Department newDepartment = departmentRepository.save(department);
        return new DepartmentDTO(newDepartment.getDepartmentId(), newDepartment.getDepartmentName());
    }

    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO) {
        validateDepartment(departmentDTO, CHECK_NAME, CHECK_ID, CHECK_EXISTS, CHECK_DUPLICATE_NAME);

        Department updateDepartment = departmentRepository.findById(departmentDTO.getDepartmentId()).get();
        updateDepartment.setDepartmentName(departmentDTO.getDepartmentName());
        Department newDepartment = departmentRepository.save(updateDepartment);
        return new DepartmentDTO(newDepartment.getDepartmentId(), newDepartment.getDepartmentName());
    }

    public DepartmentDTO deleteDepartment(DepartmentDTO departmentDTO) {
        validateDepartment(departmentDTO, CHECK_ID, CHECK_EXISTS, CHECK_HAS_EMPLOYEES);

        Department department = departmentRepository.findById(departmentDTO.getDepartmentId()).get();
        DepartmentDTO departmentToDelete = new DepartmentDTO(department.getDepartmentId(), department.getDepartmentName());
        departmentRepository.delete(department);
        return departmentToDelete;
    }



    private void validateDepartment(DepartmentDTO departmentDTO, ValidationRule... rules) {
        Set<ValidationRule> ruleSet = Set.of(rules);

        if (ruleSet.contains(CHECK_NAME) &&
                (departmentDTO.getDepartmentName() == null || departmentDTO.getDepartmentName().isBlank())) {
            throw new MissingFieldsException(MissingFieldsException.DEPARTMENT_NAME);
        }

        if (ruleSet.contains(CHECK_ID) && departmentDTO.getDepartmentId() == null) {
            throw new MissingFieldsException(MissingFieldsException.DEPARTMENT_ID);
        }

        if (ruleSet.contains(CHECK_EXISTS)) {
            departmentRepository.findById(departmentDTO.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(departmentDTO.getDepartmentId()));
        }

        if (ruleSet.contains(CHECK_DUPLICATE_NAME)) {
            Department existingDepartment = departmentRepository
                    .findByDepartmentNameIgnoreCase(departmentDTO.getDepartmentName()).orElse(null);

            if (existingDepartment != null && !existingDepartment.getDepartmentId().equals(departmentDTO.getDepartmentId())) {
                throw new DepartmentAlreadyExistsException(departmentDTO.getDepartmentName());
            }
        }

        if (ruleSet.contains(CHECK_HAS_EMPLOYEES) &&
                employeeRepository.existsByDepartmentDepartmentId(departmentDTO.getDepartmentId())) {
            throw new InvalidDepartmentDeleteException(departmentDTO.getDepartmentName());
        }
    }
}
