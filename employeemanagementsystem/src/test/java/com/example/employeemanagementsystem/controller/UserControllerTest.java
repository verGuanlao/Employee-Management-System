package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.component.MessageHelper;
import com.example.employeemanagementsystem.dto.UserDTO;
import com.example.employeemanagementsystem.service.UserService;
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
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDTO sampleUser;

    private static final String SAMPLE_USER_JSON = "{\"userId\": 127, \"username\": \"admin\", \"password\": \"\", \"role\": \"ADMIN\"}";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        sampleUser = UserDTO.builder()
                .userId(127L)
                .username("admin")
                .password("")
                .role("ADMIN")
                .build();
    }

    @Test
    void getAllUsers_noFilter_returnsPageOfUsers() throws Exception {
        Page<UserDTO> page = new PageImpl<>(List.of(sampleUser), PageRequest.of(0, 20), 1);

        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.user.fetched")).thenReturn("Users retrieved successfully");
            when(userService.findAllUsers(any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Users retrieved successfully"))
                    .andExpect(jsonPath("$.data.content[0].userId").value(127))
                    .andExpect(jsonPath("$.data.content[0].username").value("admin"))
                    .andExpect(jsonPath("$.data.content[0].role").value("ADMIN"))
                    .andExpect(jsonPath("$.data.totalElements").value(1));

            verify(userService).findAllUsers(any(Pageable.class));
            verify(userService, never()).findByUsernameContainingIgnoreCase(any(), any());
        }
    }

    @Test
    void getAllUsers_withUsernameFilter_returnsFilteredPage() throws Exception {
        Page<UserDTO> page = new PageImpl<>(List.of(sampleUser), PageRequest.of(0, 20), 1);

        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.user.fetched")).thenReturn("Users retrieved successfully");
            when(userService.findByUsernameContainingIgnoreCase(eq("admin"), any(Pageable.class)))
                    .thenReturn(page);

            mockMvc.perform(get("/api/users")
                            .param("username", "admin")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.content[0].userId").value(127))
                    .andExpect(jsonPath("$.data.content[0].username").value("admin"))
                    .andExpect(jsonPath("$.data.totalElements").value(1));

            verify(userService).findByUsernameContainingIgnoreCase(eq("admin"), any(Pageable.class));
            verify(userService, never()).findAllUsers(any());
        }
    }

    @Test
    void getAllUsers_withBlankUsername_fallsBackToFindAll() throws Exception {
        Page<UserDTO> page = new PageImpl<>(List.of(sampleUser), PageRequest.of(0, 20), 1);

        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.user.fetched")).thenReturn("Users retrieved successfully");
            when(userService.findAllUsers(any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get("/api/users")
                            .param("username", "   ")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.content[0].username").value("admin"));

            verify(userService).findAllUsers(any(Pageable.class));
            verify(userService, never()).findByUsernameContainingIgnoreCase(any(), any());
        }
    }


    @Test
    void updateUser_validRequest_returnsUpdatedUser() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.user.updated")).thenReturn("User updated successfully");
            when(userService.updateUser(any(UserDTO.class))).thenReturn(sampleUser);

            mockMvc.perform(patch("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_USER_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("User updated successfully"))
                    .andExpect(jsonPath("$.data.userId").value(127))
                    .andExpect(jsonPath("$.data.username").value("admin"))
                    .andExpect(jsonPath("$.data.role").value("ADMIN"));

            verify(userService).updateUser(any(UserDTO.class));
        }
    }

    @Test
    void updateUser_serviceReturnsUpdatedFields_reflectedInResponse() throws Exception {
        UserDTO updated = UserDTO.builder()
                .userId(127L)
                .username("admin_updated")
                .password("")
                .role("ADMIN")
                .build();

        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.user.updated")).thenReturn("User updated successfully");
            when(userService.updateUser(any(UserDTO.class))).thenReturn(updated);

            mockMvc.perform(patch("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_USER_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.userId").value(127))
                    .andExpect(jsonPath("$.data.username").value("admin_updated"));

            verify(userService).updateUser(any(UserDTO.class));
        }
    }


    @Test
    void deleteUser_validRequest_returnsDeletedUser() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.user.deleted")).thenReturn("User deleted successfully");
            when(userService.deleteUser(any(UserDTO.class))).thenReturn(sampleUser);

            mockMvc.perform(delete("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_USER_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("User deleted successfully"))
                    .andExpect(jsonPath("$.data.userId").value(127))
                    .andExpect(jsonPath("$.data.username").value("admin"))
                    .andExpect(jsonPath("$.data.role").value("ADMIN"));

            verify(userService).deleteUser(any(UserDTO.class));
        }
    }

    @Test
    void deleteUser_serviceReturnsDeletedUser_reflectedInResponse() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.user.deleted")).thenReturn("User deleted successfully");
            when(userService.deleteUser(any(UserDTO.class))).thenReturn(sampleUser);

            mockMvc.perform(delete("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SAMPLE_USER_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.userId").value(127))
                    .andExpect(jsonPath("$.data.username").value("admin"));

            verify(userService, times(1)).deleteUser(any(UserDTO.class));
        }
    }
}