package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.component.MessageHelper;
import com.example.employeemanagementsystem.dto.DepartmentDTO;
import com.example.employeemanagementsystem.exception.*;
import com.example.employeemanagementsystem.model.Department;
import com.example.employeemanagementsystem.repository.DepartmentRepository;
import com.example.employeemanagementsystem.repository.EmployeeRepository;
import com.example.employeemanagementsystem.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock private DepartmentRepository departmentRepository;
    @Mock private EmployeeRepository employeeRepository;

    @InjectMocks private DepartmentServiceImpl departmentService;

    private MockedStatic<MessageHelper> messageHelperMock;

    @BeforeEach
    void setUp() {
        messageHelperMock = mockStatic(MessageHelper.class);
        messageHelperMock.when(() -> MessageHelper.get(anyString())).thenReturn("mocked message");
        messageHelperMock.when(() -> MessageHelper.get(anyString(), any())).thenReturn("mocked message");
    }

    @AfterEach
    void tearDown() {
        messageHelperMock.close();
    }


    @Test
    void departmentExistsByName_shouldReturnTrue_whenExists() {
        when(departmentRepository.existsByDepartmentNameIgnoreCase("Engineering")).thenReturn(true);
        assertTrue(departmentService.departmentExistsByName("Engineering"));
    }

    @Test
    void departmentExistsByName_shouldReturnFalse_whenNotExists() {
        when(departmentRepository.existsByDepartmentNameIgnoreCase("NonExistent")).thenReturn(false);
        assertFalse(departmentService.departmentExistsByName("NonExistent"));
    }

    @Test
    void getDepartmentName_shouldReturnDepartmentDTO() {
        Department dept = buildDepartment(1L, "Engineering");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));

        DepartmentDTO result = departmentService.getDepartmentName(1L);

        assertEquals("Engineering", result.getDepartmentName());
    }

    @Test
    void getDepartmentName_shouldThrow_whenNotFound() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getDepartmentName(99L));
    }


    @Test
    void getDepartmentId_shouldReturnDepartmentDTO() {
        Department dept = buildDepartment(1L, "Engineering");
        when(departmentRepository.findByDepartmentName("Engineering")).thenReturn(Optional.of(dept));

        DepartmentDTO result = departmentService.getDepartmentId("Engineering");

        assertEquals(1L, result.getDepartmentId());
        assertEquals("Engineering", result.getDepartmentName());
    }

    @Test
    void getDepartmentId_shouldThrow_whenNotFound() {
        when(departmentRepository.findByDepartmentName("NonExistent")).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getDepartmentId("NonExistent"));
    }


    @Test
    void getDepartments_shouldReturnPageOfDepartmentDTOs() {
        Department dept = buildDepartment(1L, "Engineering");
        Page<Department> page = new PageImpl<>(List.of(dept));
        when(departmentRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<DepartmentDTO> result = departmentService.getDepartments(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Engineering", result.getContent().get(0).getDepartmentName());
    }


    @Test
    void getDepartmentsNameContaining_shouldReturnMatchingDepartments() {
        Department dept = buildDepartment(1L, "Engineering");
        Page<Department> page = new PageImpl<>(List.of(dept));
        when(departmentRepository.findByDepartmentNameContainingIgnoreCase(eq("eng"), any(Pageable.class)))
                .thenReturn(page);

        Page<DepartmentDTO> result = departmentService.getDepartmentsNameContainingIgnoreCase("eng", PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Engineering", result.getContent().get(0).getDepartmentName());
    }

    @Test
    void getDepartmentsNameContaining_shouldReturnEmpty_whenNoMatch() {
        Page<Department> page = new PageImpl<>(List.of());
        when(departmentRepository.findByDepartmentNameContainingIgnoreCase(eq("xyz"), any(Pageable.class)))
                .thenReturn(page);

        Page<DepartmentDTO> result = departmentService.getDepartmentsNameContainingIgnoreCase("xyz", PageRequest.of(0, 10));

        assertEquals(0, result.getTotalElements());
    }


    @Test
    void addDepartment_shouldSaveAndReturnDepartmentDTO() {
        DepartmentDTO dto = buildDepartmentDTO(null, "Engineering");
        Department saved = buildDepartment(1L, "Engineering");

        when(departmentRepository.findByDepartmentNameIgnoreCase("Engineering")).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenReturn(saved);

        DepartmentDTO result = departmentService.addDepartment(dto);

        assertEquals("Engineering", result.getDepartmentName());
        assertEquals(1L, result.getDepartmentId());
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void addDepartment_shouldThrow_whenNameNull() {
        DepartmentDTO dto = buildDepartmentDTO(null, null);
        assertThrows(MissingFieldsException.class, () -> departmentService.addDepartment(dto));
    }

    @Test
    void addDepartment_shouldThrow_whenNameBlank() {
        DepartmentDTO dto = buildDepartmentDTO(null, "");
        assertThrows(MissingFieldsException.class, () -> departmentService.addDepartment(dto));
    }

    @Test
    void addDepartment_shouldThrow_whenDuplicateName() {
        DepartmentDTO dto = buildDepartmentDTO(null, "Engineering");
        Department existing = buildDepartment(2L, "Engineering");

        when(departmentRepository.findByDepartmentNameIgnoreCase("Engineering"))
                .thenReturn(Optional.of(existing));

        assertThrows(DepartmentAlreadyExistsException.class, () -> departmentService.addDepartment(dto));
    }


    @Test
    void updateDepartment_shouldUpdateAndReturnDepartmentDTO() {
        DepartmentDTO dto = buildDepartmentDTO(1L, "Engineering Updated");
        Department existing = buildDepartment(1L, "Engineering");
        Department updated = buildDepartment(1L, "Engineering Updated");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(departmentRepository.findByDepartmentNameIgnoreCase("Engineering Updated"))
                .thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenReturn(updated);

        DepartmentDTO result = departmentService.updateDepartment(dto);

        assertEquals("Engineering Updated", result.getDepartmentName());
        assertEquals(1L, result.getDepartmentId());
    }

    @Test
    void updateDepartment_shouldThrow_whenIdNull() {
        DepartmentDTO dto = buildDepartmentDTO(null, "Engineering");
        assertThrows(MissingFieldsException.class, () -> departmentService.updateDepartment(dto));
    }

    @Test
    void updateDepartment_shouldThrow_whenNameNull() {
        DepartmentDTO dto = buildDepartmentDTO(1L, null);
        assertThrows(MissingFieldsException.class, () -> departmentService.updateDepartment(dto));
    }

    @Test
    void updateDepartment_shouldThrow_whenNameBlank() {
        DepartmentDTO dto = buildDepartmentDTO(1L, "");
        assertThrows(MissingFieldsException.class, () -> departmentService.updateDepartment(dto));
    }

    @Test
    void updateDepartment_shouldThrow_whenNotFound() {
        DepartmentDTO dto = buildDepartmentDTO(99L, "Engineering");
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.updateDepartment(dto));
    }

    @Test
    void updateDepartment_shouldThrow_whenDuplicateName() {
        DepartmentDTO dto = buildDepartmentDTO(1L, "Finance");
        Department existing = buildDepartment(1L, "Engineering");
        Department finance = buildDepartment(2L, "Finance");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(departmentRepository.findByDepartmentNameIgnoreCase("Finance"))
                .thenReturn(Optional.of(finance));

        assertThrows(DepartmentAlreadyExistsException.class, () -> departmentService.updateDepartment(dto));
    }

    @Test
    void updateDepartment_shouldAllow_whenUpdatingOwnName() {
        DepartmentDTO dto = buildDepartmentDTO(1L, "Engineering");
        Department existing = buildDepartment(1L, "Engineering");
        Department updated = buildDepartment(1L, "Engineering");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(departmentRepository.findByDepartmentNameIgnoreCase("Engineering"))
                .thenReturn(Optional.of(existing));
        when(departmentRepository.save(any(Department.class))).thenReturn(updated);

        assertDoesNotThrow(() -> departmentService.updateDepartment(dto));
    }

    // =====================
    // deleteDepartment
    // =====================
    @Test
    void deleteDepartment_shouldDeleteAndReturnDepartmentDTO() {
        DepartmentDTO dto = buildDepartmentDTO(1L, null);
        Department existing = buildDepartment(1L, "Engineering");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(employeeRepository.existsByDepartmentDepartmentId(1L)).thenReturn(false);

        DepartmentDTO result = departmentService.deleteDepartment(dto);

        assertEquals(1L, result.getDepartmentId());
        assertEquals("Engineering", result.getDepartmentName());
        verify(departmentRepository).delete(existing);
    }

    @Test
    void deleteDepartment_shouldThrow_whenIdNull() {
        DepartmentDTO dto = buildDepartmentDTO(null, null);
        assertThrows(MissingFieldsException.class, () -> departmentService.deleteDepartment(dto));
    }

    @Test
    void deleteDepartment_shouldThrow_whenNotFound() {
        DepartmentDTO dto = buildDepartmentDTO(99L, null);
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.deleteDepartment(dto));
    }

    @Test
    void deleteDepartment_shouldThrow_whenHasEmployees() {
        DepartmentDTO dto = buildDepartmentDTO(1L, null);
        Department existing = buildDepartment(1L, "Engineering");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(employeeRepository.existsByDepartmentDepartmentId(1L)).thenReturn(true);

        assertThrows(InvalidDepartmentDeleteException.class, () -> departmentService.deleteDepartment(dto));
    }

    // =====================
    // helpers
    // =====================
    private Department buildDepartment(Long id, String name) {
        Department dept = new Department();
        dept.setDepartmentId(id);
        dept.setDepartmentName(name);
        return dept;
    }

    private DepartmentDTO buildDepartmentDTO(Long id, String name) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setDepartmentId(id);
        dto.setDepartmentName(name);
        return dto;
    }
}