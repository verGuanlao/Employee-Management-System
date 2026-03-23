package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.component.MessageHelper;
import com.example.employeemanagementsystem.dto.DepartmentDTO;
import com.example.employeemanagementsystem.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    private DepartmentDTO sampleDepartment;

    private static final String SAMPLE_DEPARTMENT_JSON = """
            {
                "departmentId": 1,
                "departmentName": "Engineering"
            }
            """;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(departmentController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        sampleDepartment = DepartmentDTO.builder()
                .departmentId(1L)
                .departmentName("Engineering")
                .build();
    }


    @Test
    void getDepartments_returnsPageOfDepartments() throws Exception {
        Page<DepartmentDTO> page = new PageImpl<>(List.of(sampleDepartment), PageRequest.of(0, 20), 1);

        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.department.fetched"))
                    .thenReturn("Departments fetched successfully");
            when(departmentService.getDepartments(any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get("/api/departments").contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Departments fetched successfully"))
                    .andExpect(jsonPath("$.data.content[0].departmentId").value(1))
                    .andExpect(jsonPath("$.data.content[0].departmentName").value("Engineering"))
                    .andExpect(jsonPath("$.data.totalElements").value(1))
                    .andExpect(jsonPath("$.data.first").value(true))
                    .andExpect(jsonPath("$.data.last").value(true));

            verify(departmentService).getDepartments(any(Pageable.class));
        }
    }


    @Test
    void searchDepartments_withQuery_returnsMatchingDepartments() throws Exception {
        Page<DepartmentDTO> page = new PageImpl<>(List.of(sampleDepartment), PageRequest.of(0, 20), 1);

        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.department.searched"))
                    .thenReturn("Departments searched successfully");
            when(departmentService.getDepartmentsNameContainingIgnoreCase(eq("eng"), any(Pageable.class)))
                    .thenReturn(page);

            mockMvc.perform(get("/api/departments/search")
                            .param("query", "eng")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Departments searched successfully"))
                    .andExpect(jsonPath("$.data.content[0].departmentId").value(1))
                    .andExpect(jsonPath("$.data.content[0].departmentName").value("Engineering"))
                    .andExpect(jsonPath("$.data.totalElements").value(1));

            verify(departmentService).getDepartmentsNameContainingIgnoreCase(eq("eng"), any(Pageable.class));
        }
    }

    @Test
    void searchDepartments_withNoQuery_returnsAllDepartments() throws Exception {
        Page<DepartmentDTO> page = new PageImpl<>(List.of(sampleDepartment), PageRequest.of(0, 20), 1);

        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.department.searched"))
                    .thenReturn("Departments searched successfully");
            when(departmentService.getDepartmentsNameContainingIgnoreCase(isNull(), any(Pageable.class)))
                    .thenReturn(page);

            mockMvc.perform(get("/api/departments/search").contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.content[0].departmentId").value(1))
                    .andExpect(jsonPath("$.data.content[0].departmentName").value("Engineering"));

            verify(departmentService).getDepartmentsNameContainingIgnoreCase(isNull(), any(Pageable.class));
        }
    }


    @Test
    void addDepartment_validRequest_returnsCreatedDepartment() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.department.added"))
                    .thenReturn("Department added successfully");
            when(departmentService.addDepartment(any(DepartmentDTO.class))).thenReturn(sampleDepartment);

            mockMvc.perform(post("/api/departments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_DEPARTMENT_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Department added successfully"))
                    .andExpect(jsonPath("$.data.departmentId").value(1))
                    .andExpect(jsonPath("$.data.departmentName").value("Engineering"));

            verify(departmentService).addDepartment(any(DepartmentDTO.class));
        }
    }

    @Test
    void addDepartment_serviceReturnsNewDepartment_reflectedInResponse() throws Exception {
        DepartmentDTO newDept = DepartmentDTO.builder()
                .departmentId(2L)
                .departmentName("Finance")
                .build();

        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.department.added"))
                    .thenReturn("Department added successfully");
            when(departmentService.addDepartment(any(DepartmentDTO.class))).thenReturn(newDept);

            mockMvc.perform(post("/api/departments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_DEPARTMENT_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.departmentId").value(2))
                    .andExpect(jsonPath("$.data.departmentName").value("Finance"));

            verify(departmentService).addDepartment(any(DepartmentDTO.class));
        }
    }


    @Test
    void updateDepartment_validRequest_returnsUpdatedDepartment() throws Exception {
        DepartmentDTO updated = DepartmentDTO.builder()
                .departmentId(1L)
                .departmentName("Engineering Updated")
                .build();

        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.department.updated"))
                    .thenReturn("Department updated successfully");
            when(departmentService.updateDepartment(any(DepartmentDTO.class))).thenReturn(updated);

            mockMvc.perform(put("/api/departments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_DEPARTMENT_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Department updated successfully"))
                    .andExpect(jsonPath("$.data.departmentId").value(1))
                    .andExpect(jsonPath("$.data.departmentName").value("Engineering Updated"));

            verify(departmentService).updateDepartment(any(DepartmentDTO.class));
        }
    }


    @Test
    void deleteDepartment_validRequest_returnsDeletedDepartment() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.department.deleted"))
                    .thenReturn("Department deleted successfully");
            when(departmentService.deleteDepartment(any(DepartmentDTO.class))).thenReturn(sampleDepartment);

            mockMvc.perform(delete("/api/departments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_DEPARTMENT_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Department deleted successfully"))
                    .andExpect(jsonPath("$.data.departmentId").value(1))
                    .andExpect(jsonPath("$.data.departmentName").value("Engineering"));

            verify(departmentService).deleteDepartment(any(DepartmentDTO.class));
        }
    }

    @Test
    void deleteDepartment_serviceReturnsDeletedDepartment_reflectedInResponse() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.department.deleted"))
                    .thenReturn("Department deleted successfully");
            when(departmentService.deleteDepartment(any(DepartmentDTO.class))).thenReturn(sampleDepartment);

            mockMvc.perform(delete("/api/departments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_DEPARTMENT_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.departmentId").value(1))
                    .andExpect(jsonPath("$.data.departmentName").value("Engineering"));

            verify(departmentService, times(1)).deleteDepartment(any(DepartmentDTO.class));
        }
    }
}