package com.example.employeemanagementsystem.component;

import com.example.employeemanagementsystem.repository.DepartmentRepository;
import com.example.employeemanagementsystem.repository.EmployeeRepository;
import com.example.employeemanagementsystem.repository.UserRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataCleaner {

    @Autowired private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired private DepartmentRepository departmentRepository;

    @Value("${app.seeder.clear-on-shutdown:true}")
    private boolean clearOnShutdown;

    @PreDestroy
    public void clear() {
        if (!clearOnShutdown) return;
        employeeRepository.deleteAll();
        userRepository.deleteAll();
        departmentRepository.deleteAll();
        System.out.println("✅ Seeded data cleared");
    }
}