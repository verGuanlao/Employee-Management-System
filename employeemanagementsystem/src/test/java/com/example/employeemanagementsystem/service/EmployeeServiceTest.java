package com.example.employeemanagementsystem.service;


import com.example.employeemanagementsystem.dto.DepartmentDTO;
import com.example.employeemanagementsystem.dto.EmployeeDTO;
import com.example.employeemanagementsystem.dto.EmployeeStatsResponse;
import com.example.employeemanagementsystem.exception.*;
import com.example.employeemanagementsystem.model.Department;
import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.repository.EmployeeRepository;
import com.example.employeemanagementsystem.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private DepartmentService departmentService;

    @InjectMocks private EmployeeServiceImpl employeeService;


    @Test
    void getEmployeeById_shouldReturnEmployeeDTO() {
        Employee employee = buildEmployee(1L, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), buildDepartment(1L, "Engineering"));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDTO result = employeeService.getEmployeeById(1L);

        assertEquals("John Doe", result.getName());
        assertEquals(1L, result.getEmployeeId());
    }

    @Test
    void getEmployeeById_shouldThrow_whenNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(99L));
    }

    @Test
    void addEmployee_shouldSaveAndReturnEmployeeDTO() {
        EmployeeDTO dto = buildEmployeeDTO(null, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), "Engineering");
        Employee saved = buildEmployee(1L, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), buildDepartment(1L, "Engineering"));

        when(departmentService.departmentExistsByName("Engineering")).thenReturn(true);
        when(departmentService.getDepartmentId("Engineering")).thenReturn(new DepartmentDTO(1L, "Engineering"));
        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        EmployeeDTO result = employeeService.addEmployee(dto);

        assertEquals("John Doe", result.getName());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void addEmployee_shouldThrow_whenNameBlank() {
        EmployeeDTO dto = buildEmployeeDTO(null, "", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), "Engineering");
        assertThrows(MissingFieldsException.class, () -> employeeService.addEmployee(dto));
    }

    @Test
    void addEmployee_shouldThrow_whenNameNull() {
        EmployeeDTO dto = buildEmployeeDTO(null, null, LocalDate.of(1990, 1, 1), new BigDecimal("50000"), "Engineering");
        assertThrows(MissingFieldsException.class, () -> employeeService.addEmployee(dto));
    }

    @Test
    void addEmployee_shouldThrow_whenDepartmentNull() {
        EmployeeDTO dto = buildEmployeeDTO(null, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), null);
        assertThrows(MissingFieldsException.class, () -> employeeService.addEmployee(dto));
    }

    @Test
    void addEmployee_shouldThrow_whenDepartmentBlank() {
        EmployeeDTO dto = buildEmployeeDTO(null, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), "");
        assertThrows(MissingFieldsException.class, () -> employeeService.addEmployee(dto));
    }

    @Test
    void addEmployee_shouldThrow_whenDepartmentDoesNotExist() {
        EmployeeDTO dto = buildEmployeeDTO(null, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), "NonExistent");
        when(departmentService.departmentExistsByName("NonExistent")).thenReturn(false);
        assertThrows(DepartmentNotFoundException.class, () -> employeeService.addEmployee(dto));
    }

    @Test
    void addEmployee_shouldThrow_whenBirthDateNull() {
        EmployeeDTO dto = buildEmployeeDTO(null, "John Doe", null, new BigDecimal("50000"), "Engineering");
        when(departmentService.departmentExistsByName("Engineering")).thenReturn(true);
        assertThrows(MissingFieldsException.class, () -> employeeService.addEmployee(dto));
    }

    @Test
    void addEmployee_shouldThrow_whenSalaryNull() {
        EmployeeDTO dto = buildEmployeeDTO(null, "John Doe", LocalDate.of(1990, 1, 1), null, "Engineering");
        when(departmentService.departmentExistsByName("Engineering")).thenReturn(true);
        assertThrows(MissingFieldsException.class, () -> employeeService.addEmployee(dto));
    }

    @Test
    void addEmployee_shouldThrow_whenSalaryZero() {
        EmployeeDTO dto = buildEmployeeDTO(null, "John Doe", LocalDate.of(1990, 1, 1), BigDecimal.ZERO, "Engineering");
        when(departmentService.departmentExistsByName("Engineering")).thenReturn(true);
        assertThrows(NonPositiveSalaryException.class, () -> employeeService.addEmployee(dto));
    }

    @Test
    void addEmployee_shouldThrow_whenSalaryNegative() {
        EmployeeDTO dto = buildEmployeeDTO(null, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("-1000"), "Engineering");
        when(departmentService.departmentExistsByName("Engineering")).thenReturn(true);
        assertThrows(NonPositiveSalaryException.class, () -> employeeService.addEmployee(dto));
    }

    @Test
    void addEmployee_shouldThrow_whenUnder18() {
        LocalDate under18 = LocalDate.now().minusYears(17);
        EmployeeDTO dto = buildEmployeeDTO(null, "John Doe", under18, new BigDecimal("50000"), "Engineering");
        when(departmentService.departmentExistsByName("Engineering")).thenReturn(true);
        assertThrows(ImproperAgeException.class, () -> employeeService.addEmployee(dto));
    }

    @Test
    void addEmployee_shouldSucceed_whenExactly18() {
        LocalDate exactly18 = LocalDate.now().minusYears(18);
        EmployeeDTO dto = buildEmployeeDTO(null, "John Doe", exactly18, new BigDecimal("50000"), "Engineering");
        Employee saved = buildEmployee(1L, "John Doe", exactly18, new BigDecimal("50000"), buildDepartment(1L, "Engineering"));

        when(departmentService.departmentExistsByName("Engineering")).thenReturn(true);
        when(departmentService.getDepartmentId("Engineering")).thenReturn(new DepartmentDTO(1L, "Engineering"));
        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        assertDoesNotThrow(() -> employeeService.addEmployee(dto));
    }

    @Test
    void updateEmployee_shouldUpdateAndReturnEmployeeDTO() {
        EmployeeDTO dto = buildEmployeeDTO(1L, "John Updated", LocalDate.of(1990, 1, 1), new BigDecimal("60000"), "Engineering");
        Employee existing = buildEmployee(1L, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), buildDepartment(1L, "Engineering"));
        Employee updated = buildEmployee(1L, "John Updated", LocalDate.of(1990, 1, 1), new BigDecimal("60000"), buildDepartment(1L, "Engineering"));

        when(departmentService.departmentExistsByName("Engineering")).thenReturn(true);
        when(departmentService.getDepartmentId("Engineering")).thenReturn(new DepartmentDTO(1L, "Engineering"));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updated);

        EmployeeDTO result = employeeService.updateEmployee(dto);

        assertEquals("John Updated", result.getName());
        assertEquals(new BigDecimal("60000"), result.getSalary());
    }

    @Test
    void updateEmployee_shouldThrow_whenIdNull() {
        EmployeeDTO dto = buildEmployeeDTO(null, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), "Engineering");
        assertThrows(MissingFieldsException.class, () -> employeeService.updateEmployee(dto));
    }

    @Test
    void updateEmployee_shouldThrow_whenEmployeeNotFound() {
        EmployeeDTO dto = buildEmployeeDTO(99L, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), "Engineering");

        when(departmentService.departmentExistsByName("Engineering")).thenReturn(true);
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(dto));
    }

    @Test
    void updateEmployee_shouldThrow_whenUnder18() {
        LocalDate under18 = LocalDate.now().minusYears(17);
        EmployeeDTO dto = buildEmployeeDTO(1L, "John Doe", under18, new BigDecimal("50000"), "Engineering");
        Employee existing = buildEmployee(1L, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), buildDepartment(1L, "Engineering"));

        when(departmentService.departmentExistsByName("Engineering")).thenReturn(true);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(ImproperAgeException.class, () -> employeeService.updateEmployee(dto));
    }


    @Test
    void deleteEmployee_shouldDeleteAndReturnEmployeeDTO() {
        EmployeeDTO dto = buildEmployeeDTO(1L, null, null, null, null);
        Employee existing = buildEmployee(1L, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), buildDepartment(1L, "Engineering"));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));

        EmployeeDTO result = employeeService.deleteEmployee(dto);

        assertEquals(1L, result.getEmployeeId());
        verify(employeeRepository).delete(existing);
    }

    @Test
    void deleteEmployee_shouldThrow_whenIdNull() {
        EmployeeDTO dto = buildEmployeeDTO(null, null, null, null, null);
        assertThrows(MissingFieldsException.class, () -> employeeService.deleteEmployee(dto));
    }

    @Test
    void deleteEmployee_shouldThrow_whenNotFound() {
        EmployeeDTO dto = buildEmployeeDTO(99L, null, null, null, null);
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(dto));
    }


    @Test
    void getEmployeesWithStats_noFilters_shouldReturnAllWithStats() {
        Employee emp = buildEmployee(1L, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), buildDepartment(1L, "Engineering"));
        Page<Employee> page = new PageImpl<>(List.of(emp));

        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(employeeRepository.calculateAverageSalaryAll()).thenReturn(50000.0);
        when(employeeRepository.calculateAverageAgeAll()).thenReturn(34.0);

        EmployeeStatsResponse result = employeeService.getEmployeesWithStats(null, null, null, PageRequest.of(0, 10));

        assertEquals(1, result.getEmployees().getTotalElements());
        assertEquals(50000.0, result.getStats().get("averageSalary"));
        assertEquals(34.0, result.getStats().get("averageAge"));
    }


    @Test
    void getEmployeesWithStats_withDept_shouldReturnFilteredResults() {
        Employee emp = buildEmployee(1L, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), buildDepartment(1L, "Engineering"));
        Page<Employee> page = new PageImpl<>(List.of(emp));

        when(employeeRepository.findByDepartmentDepartmentId(eq(1L), any(Pageable.class))).thenReturn(page);
        when(employeeRepository.calculateAverageSalaryByDepartment(1L)).thenReturn(50000.0);
        when(employeeRepository.calculateAverageAgeByDepartment(1L)).thenReturn(34.0);

        EmployeeStatsResponse result = employeeService.getEmployeesWithStats(1L, null, null, PageRequest.of(0, 10));

        assertEquals(1, result.getEmployees().getTotalElements());
        assertEquals(50000.0, result.getStats().get("averageSalary"));
    }


    @Test
    void getEmployeesWithStats_withAgeRange_shouldReturnFilteredResults() {
        Employee emp = buildEmployee(1L, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), buildDepartment(1L, "Engineering"));
        Page<Employee> page = new PageImpl<>(List.of(emp));

        when(employeeRepository.findByBirthDateBetween(any(LocalDate.class), any(LocalDate.class), any(Pageable.class))).thenReturn(page);
        when(employeeRepository.calculateAverageSalaryByAgeRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(50000.0);
        when(employeeRepository.calculateAverageAgeByRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(34.0);

        EmployeeStatsResponse result = employeeService.getEmployeesWithStats(null, 30, 40, PageRequest.of(0, 10));

        assertEquals(1, result.getEmployees().getTotalElements());
    }


    @Test
    void getEmployeesWithStats_withDeptAndAgeRange_shouldReturnFilteredResults() {
        Employee emp = buildEmployee(1L, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), buildDepartment(1L, "Engineering"));
        Page<Employee> page = new PageImpl<>(List.of(emp));

        when(employeeRepository.findByDepartmentDepartmentIdAndBirthDateBetween(eq(1L), any(LocalDate.class), any(LocalDate.class), any(Pageable.class))).thenReturn(page);
        when(employeeRepository.calculateAverageSalaryByDepartmentAndAgeRange(eq(1L), any(LocalDate.class), any(LocalDate.class))).thenReturn(50000.0);
        when(employeeRepository.calculateAverageAgeByDepartmentAndAgeRange(eq(1L), any(LocalDate.class), any(LocalDate.class))).thenReturn(34.0);

        EmployeeStatsResponse result = employeeService.getEmployeesWithStats(1L, 30, 40, PageRequest.of(0, 10));

        assertEquals(1, result.getEmployees().getTotalElements());
        assertEquals(50000.0, result.getStats().get("averageSalary"));
    }


    @Test
    void getEmployeesByName_shouldReturnMatchingEmployees() {
        Employee emp = buildEmployee(1L, "John Doe", LocalDate.of(1990, 1, 1), new BigDecimal("50000"), buildDepartment(1L, "Engineering"));
        Page<Employee> page = new PageImpl<>(List.of(emp));

        when(employeeRepository.findByNameContainingIgnoreCase(eq("John"), any(Pageable.class))).thenReturn(page);

        EmployeeStatsResponse result = employeeService.getEmployeesByNameContainingIgnoreCase("John", PageRequest.of(0, 10));

        assertEquals(1, result.getEmployees().getTotalElements());
        assertEquals("John Doe", result.getEmployees().getContent().get(0).getName());
    }


    @Test
    void isAtLeast18_shouldReturnTrue_whenExactly18() {
        LocalDate exactly18 = LocalDate.now().minusYears(18);
        assertTrue(EmployeeServiceImpl.isAtLeast18(exactly18));
    }

    @Test
    void isAtLeast18_shouldReturnTrue_whenOver18() {
        LocalDate over18 = LocalDate.now().minusYears(30);
        assertTrue(EmployeeServiceImpl.isAtLeast18(over18));
    }

    @Test
    void isAtLeast18_shouldReturnFalse_whenUnder18() {
        LocalDate under18 = LocalDate.now().minusYears(17);
        assertFalse(EmployeeServiceImpl.isAtLeast18(under18));
    }


    private Employee buildEmployee(Long id, String name, LocalDate birthDate,
                                   BigDecimal salary, Department department) {
        Employee emp = new Employee();
        emp.setEmployeeId(id);
        emp.setName(name);
        emp.setBirthDate(birthDate);
        emp.setEmployeeSalary(salary);
        emp.setDepartment(department);
        return emp;
    }

    private EmployeeDTO buildEmployeeDTO(Long id, String name, LocalDate birthDate,
                                         BigDecimal salary, String department) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(id);
        dto.setName(name);
        dto.setBirthDate(birthDate);
        dto.setSalary(salary);
        dto.setDepartment(department);
        return dto;
    }

    private Department buildDepartment(Long id, String name) {
        Department dept = new Department();
        dept.setDepartmentId(id);
        dept.setDepartmentName(name);
        return dept;
    }
}