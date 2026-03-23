package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.component.MessageHelper;
import com.example.employeemanagementsystem.controller.auth.AuthController;
import com.example.employeemanagementsystem.dto.UserDTO;
import com.example.employeemanagementsystem.model.User;
import com.example.employeemanagementsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private UserDTO sampleUserDTO;
    private User sampleUser;

    private static final String REGISTER_REQUEST_JSON = """
            {
                "username": "admin",
                "password": "password123",
                "role": "ADMIN"
            }
            """;

    private static final String LOGIN_REQUEST_JSON = """
            {
                "username": "admin",
                "password": "password123"
            }
            """;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        sampleUserDTO = UserDTO.builder()
                .userId(127L)
                .username("admin")
                .password("")
                .role("ADMIN")
                .build();

        // Mock User model — adjust setter/constructor names to match your actual User class
        sampleUser = new User();
        sampleUser.setUserId(127L);
        sampleUser.setUsername("admin");
        sampleUser.setRole("ADMIN");
    }


    @Test
    void registerUser_validRequest_returnsCreatedUser() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.auth.registered"))
                    .thenReturn("User registered successfully");
            when(userService.registerUser(any(UserDTO.class))).thenReturn(sampleUserDTO);

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(REGISTER_REQUEST_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("User registered successfully"))
                    .andExpect(jsonPath("$.data.userId").value(127))
                    .andExpect(jsonPath("$.data.username").value("admin"))
                    .andExpect(jsonPath("$.data.role").value("ADMIN"))
                    // password should never be returned in response
                    .andExpect(jsonPath("$.data.password").value(""));

            verify(userService).registerUser(any(UserDTO.class));
        }
    }

    @Test
    void registerUser_serviceReturnsNewUser_reflectedInResponse() throws Exception {
        UserDTO newUser = UserDTO.builder()
                .userId(128L)
                .username("newadmin")
                .password("")
                .role("ADMIN")
                .build();

        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.auth.registered"))
                    .thenReturn("User registered successfully");
            when(userService.registerUser(any(UserDTO.class))).thenReturn(newUser);

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(REGISTER_REQUEST_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.userId").value(128))
                    .andExpect(jsonPath("$.data.username").value("newadmin"));

            verify(userService).registerUser(any(UserDTO.class));
        }
    }


    @Test
    void login_validCredentials_returnsUserWithToken() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.auth.login"))
                    .thenReturn("Login successful");
            when(userService.login(any(UserDTO.class))).thenReturn("mock-jwt-token");
            when(userService.getByUsername("admin")).thenReturn(sampleUser);

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(LOGIN_REQUEST_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().string("Authorization", "Bearer mock-jwt-token"))
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Login successful"))
                    .andExpect(jsonPath("$.data.userId").value(127))
                    .andExpect(jsonPath("$.data.username").value("admin"))
                    .andExpect(jsonPath("$.data.role").value("ADMIN"))
                    // password should be empty string in login response
                    .andExpect(jsonPath("$.data.password").value(""));

            verify(userService).login(any(UserDTO.class));
            verify(userService).getByUsername("admin");
        }
    }

    @Test
    void login_validCredentials_authorizationHeaderPresent() throws Exception {
        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.auth.login"))
                    .thenReturn("Login successful");
            when(userService.login(any(UserDTO.class))).thenReturn("mock-jwt-token");
            when(userService.getByUsername("admin")).thenReturn(sampleUser);

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(LOGIN_REQUEST_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Authorization"))
                    .andExpect(header().string("Authorization", "Bearer mock-jwt-token"));

            verify(userService).login(any(UserDTO.class));
        }
    }

    @Test
    void login_differentUser_reflectedInResponse() throws Exception {
        User otherUser = new User();
        otherUser.setUserId(128L);
        otherUser.setUsername("user");
        otherUser.setRole("USER");

        try (MockedStatic<MessageHelper> mh = mockStatic(MessageHelper.class)) {
            mh.when(() -> MessageHelper.get("success.auth.login"))
                    .thenReturn("Login successful");
            when(userService.login(any(UserDTO.class))).thenReturn("another-jwt-token");
            when(userService.getByUsername("admin")).thenReturn(otherUser);

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(LOGIN_REQUEST_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().string("Authorization", "Bearer another-jwt-token"))
                    .andExpect(jsonPath("$.data.userId").value(128))
                    .andExpect(jsonPath("$.data.username").value("user"))
                    .andExpect(jsonPath("$.data.role").value("USER"));

            verify(userService).login(any(UserDTO.class));
            verify(userService).getByUsername("admin");
        }
    }
}