package com.example.employeemanagementsystem.repository;

import com.example.employeemanagementsystem.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByDepartmentName(String name);
    Optional<Department> findByDepartmentNameIgnoreCase(String departmentName);
    Page<Department> findByDepartmentNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByDepartmentNameIgnoreCase(String name);
}

