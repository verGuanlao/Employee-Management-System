package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.component.MessageHelper;
import com.example.employeemanagementsystem.dto.EmployeeDTO;
import com.example.employeemanagementsystem.dto.EmployeeStatsResponse;
import com.example.employeemanagementsystem.service.EmployeeService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeDTO sampleEmployee;
    private EmployeeStatsResponse sampleStatsResponse;

    private static final String SAMPLE_EMPLOYEE_JSON = """
            {
                "employeeId": 1151,
                "name": "James Anderson",
                "birthDate": [1990, 3, 15],
                "department": "Engineering",
                "salary": 85000
            }
            """;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(employeeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        sampleEmployee = EmployeeDTO.builder()
                .employeeId(1151L)
                .name("James Anderson")
                .birthDate(LocalDate.of(1990, 3, 15))
                .department("Engineering")
                .salary(BigDecimal.valueOf(85000))
                .build();

        Page<EmployeeDTO> employeePage = new PageImpl<>(
                List.of(sampleEmployee), PageRequest.of(0, 20), 50
        );

        Map<String, Object> stats = new HashMap<>();
        stats.put("averageSalary", 78020.0);
        stats.put("averageAge", 35.52);

        sampleStatsResponse = EmployeeStatsResponse.builder()
                .employees(employeePage)
                .stats(stats)
                .build();
    }


    @Test
    void getEmployeeStats_noFilter_returnsStatsResponse() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.employee.stats.fetched"))
                    .thenReturn("Employee and stats fetched successfully");
            when(employeeService.getEmployeesWithStats(isNull(), isNull(), isNull(), any(Pageable.class)))
                    .thenReturn(sampleStatsResponse);

            mockMvc.perform(get("/api/employees").contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Employee and stats fetched successfully"))
                    .andExpect(jsonPath("$.data.employees.content[0].employeeId").value(1151))
                    .andExpect(jsonPath("$.data.employees.content[0].name").value("James Anderson"))
                    .andExpect(jsonPath("$.data.employees.content[0].department").value("Engineering"))
                    .andExpect(jsonPath("$.data.employees.content[0].salary").value(85000))
                    .andExpect(jsonPath("$.data.employees.totalElements").value(50))
                    .andExpect(jsonPath("$.data.stats.averageSalary").value(78020.0))
                    .andExpect(jsonPath("$.data.stats.averageAge").value(35.52));

            verify(employeeService).getEmployeesWithStats(isNull(), isNull(), isNull(), any(Pageable.class));
        }
    }

    @Test
    void getEmployeeStats_withDeptIdFilter_returnsFilteredStats() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.employee.stats.fetched"))
                    .thenReturn("Employee and stats fetched successfully");
            when(employeeService.getEmployeesWithStats(eq(1L), isNull(), isNull(), any(Pageable.class)))
                    .thenReturn(sampleStatsResponse);

            mockMvc.perform(get("/api/employees")
                            .param("deptId", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.employees.content[0].department").value("Engineering"))
                    .andExpect(jsonPath("$.data.stats.averageSalary").value(78020.0));

            verify(employeeService).getEmployeesWithStats(eq(1L), isNull(), isNull(), any(Pageable.class));
        }
    }

    @Test
    void getEmployeeStats_withAgeFilter_returnsFilteredStats() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.employee.stats.fetched"))
                    .thenReturn("Employee and stats fetched successfully");
            when(employeeService.getEmployeesWithStats(isNull(), eq(25), eq(40), any(Pageable.class)))
                    .thenReturn(sampleStatsResponse);

            mockMvc.perform(get("/api/employees")
                            .param("minAge", "25")
                            .param("maxAge", "40")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.employees.totalElements").value(50))
                    .andExpect(jsonPath("$.data.stats.averageAge").value(35.52));

            verify(employeeService).getEmployeesWithStats(isNull(), eq(25), eq(40), any(Pageable.class));
        }
    }


    @Test
    void getEmployeeById_validId_returnsEmployee() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.employee.fetched"))
                    .thenReturn("Employee fetched successfully");
            when(employeeService.getEmployeeById(1151L)).thenReturn(sampleEmployee);

            mockMvc.perform(get("/api/employees/1151").contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Employee fetched successfully"))
                    .andExpect(jsonPath("$.data.employeeId").value(1151))
                    .andExpect(jsonPath("$.data.name").value("James Anderson"))
                    .andExpect(jsonPath("$.data.department").value("Engineering"))
                    .andExpect(jsonPath("$.data.salary").value(85000));

            verify(employeeService).getEmployeeById(1151L);
        }
    }


    @Test
    void searchEmployees_withQuery_returnsMatchingEmployees() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.employee.searched"))
                    .thenReturn("Employees searched successfully");
            when(employeeService.getEmployeesByNameContainingIgnoreCase(eq("james"), any(Pageable.class)))
                    .thenReturn(sampleStatsResponse);

            mockMvc.perform(get("/api/employees/search")
                            .param("query", "james")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Employees searched successfully"))
                    .andExpect(jsonPath("$.data.employees.content[0].name").value("James Anderson"))
                    .andExpect(jsonPath("$.data.employees.totalElements").value(50));

            verify(employeeService).getEmployeesByNameContainingIgnoreCase(eq("james"), any(Pageable.class));
        }
    }

    @Test
    void searchEmployees_withNoQuery_returnsAllEmployees() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.employee.searched"))
                    .thenReturn("Employees searched successfully");
            when(employeeService.getEmployeesByNameContainingIgnoreCase(isNull(), any(Pageable.class)))
                    .thenReturn(sampleStatsResponse);

            mockMvc.perform(get("/api/employees/search").contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.employees.content[0].employeeId").value(1151));

            verify(employeeService).getEmployeesByNameContainingIgnoreCase(isNull(), any(Pageable.class));
        }
    }


    @Test
    void addEmployee_validRequest_returnsCreatedEmployee() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.employee.added"))
                    .thenReturn("Employee added successfully");
            when(employeeService.addEmployee(any(EmployeeDTO.class))).thenReturn(sampleEmployee);

            mockMvc.perform(post("/api/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_EMPLOYEE_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Employee added successfully"))
                    .andExpect(jsonPath("$.data.employeeId").value(1151))
                    .andExpect(jsonPath("$.data.name").value("James Anderson"))
                    .andExpect(jsonPath("$.data.department").value("Engineering"))
                    .andExpect(jsonPath("$.data.salary").value(85000));

            verify(employeeService).addEmployee(any(EmployeeDTO.class));
        }
    }


    @Test
    void updateEmployee_validRequest_returnsUpdatedEmployee() throws Exception {
        EmployeeDTO updated = EmployeeDTO.builder()
                .employeeId(1151L)
                .name("James Anderson")
                .birthDate(LocalDate.of(1990, 3, 15))
                .department("Finance")
                .salary(BigDecimal.valueOf(95000))
                .build();

        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.employee.updated"))
                    .thenReturn("Employee updated successfully");
            when(employeeService.updateEmployee(any(EmployeeDTO.class))).thenReturn(updated);

            mockMvc.perform(put("/api/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_EMPLOYEE_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Employee updated successfully"))
                    .andExpect(jsonPath("$.data.employeeId").value(1151))
                    .andExpect(jsonPath("$.data.department").value("Finance"))
                    .andExpect(jsonPath("$.data.salary").value(95000));

            verify(employeeService).updateEmployee(any(EmployeeDTO.class));
        }
    }


    @Test
    void deleteEmployee_validRequest_returnsDeletedEmployee() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.employee.deleted"))
                    .thenReturn("Employee deleted successfully");
            when(employeeService.deleteEmployee(any(EmployeeDTO.class))).thenReturn(sampleEmployee);

            mockMvc.perform(delete("/api/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_EMPLOYEE_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Employee deleted successfully"))
                    .andExpect(jsonPath("$.data.employeeId").value(1151))
                    .andExpect(jsonPath("$.data.name").value("James Anderson"))
                    .andExpect(jsonPath("$.data.department").value("Engineering"))
                    .andExpect(jsonPath("$.data.salary").value(85000));

            verify(employeeService).deleteEmployee(any(EmployeeDTO.class));
        }
    }

    @Test
    void deleteEmployee_serviceReturnsDeletedEmployee_reflectedInResponse() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.employee.deleted"))
                    .thenReturn("Employee deleted successfully");
            when(employeeService.deleteEmployee(any(EmployeeDTO.class))).thenReturn(sampleEmployee);

            mockMvc.perform(delete("/api/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_EMPLOYEE_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.employeeId").value(1151))
                    .andExpect(jsonPath("$.data.salary").value(85000));

            verify(employeeService, times(1)).deleteEmployee(any(EmployeeDTO.class));
        }
    }
}