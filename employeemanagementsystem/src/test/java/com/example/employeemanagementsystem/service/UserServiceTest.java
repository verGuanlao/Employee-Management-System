package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.config.JwtService;
import com.example.employeemanagementsystem.dto.UserDTO;
import com.example.employeemanagementsystem.exception.MissingFieldsException;
import com.example.employeemanagementsystem.exception.UserAlreadyExistsException;
import com.example.employeemanagementsystem.exception.UserNotFoundException;
import com.example.employeemanagementsystem.model.User;
import com.example.employeemanagementsystem.repository.UserRepository;
import com.example.employeemanagementsystem.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void findAllUsers_shouldReturnPageOfUserDTOs() {
        User user = buildUser(1L, "john", "encoded", "USER");
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<UserDTO> result = userService.findAllUsers(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("john", result.getContent().get(0).getUsername());
        assertEquals("", result.getContent().get(0).getPassword());
    }


    @Test
    void findByUsername_shouldReturnMatchingUsers() {
        User user = buildUser(1L, "john", "encoded", "USER");
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findByUsernameContainingIgnoreCase(eq("john"), any(Pageable.class)))
                .thenReturn(page);

        Page<UserDTO> result = userService.findByUsernameContainingIgnoreCase("john", PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("john", result.getContent().get(0).getUsername());
    }


    @Test
    void registerUser_shouldSaveAndReturnUserDTO() {
        UserDTO dto = buildUserDTO(null, "john", "password123", "USER");
        User saved = buildUser(1L, "john", "encoded", "USER");

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserDTO result = userService.registerUser(dto);

        assertEquals("john", result.getUsername());
        assertEquals("USER", result.getRole());
        assertEquals("", result.getPassword());
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void registerUser_shouldThrow_whenUsernameBlank() {
        UserDTO dto = buildUserDTO(null, "", "password123", "USER");
        assertThrows(MissingFieldsException.class, () -> userService.registerUser(dto));
    }

    @Test
    void registerUser_shouldThrow_whenPasswordBlank() {
        UserDTO dto = buildUserDTO(null, "john", "", "USER");
        assertThrows(MissingFieldsException.class, () -> userService.registerUser(dto));
    }

    @Test
    void registerUser_shouldThrow_whenRoleBlank() {
        UserDTO dto = buildUserDTO(null, "john", "password123", "");
        assertThrows(MissingFieldsException.class, () -> userService.registerUser(dto));
    }

    @Test
    void registerUser_shouldThrow_whenUsernameAlreadyExists() {
        UserDTO dto = buildUserDTO(null, "john", "password123", "USER");
        User existing = buildUser(2L, "john", "encoded", "USER");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(existing));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(dto));
    }


    @Test
    void updateUser_shouldUpdateAndReturnUserDTO() {
        UserDTO dto = buildUserDTO(1L, "johnUpdated", "password123", "ADMIN");
        User existing = buildUser(1L, "john", "encoded", "USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findByUsername("johnUpdated")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(existing);

        UserDTO result = userService.updateUser(dto);

        assertEquals("johnUpdated", result.getUsername());
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    void updateUser_shouldThrow_whenIdNull() {
        UserDTO dto = buildUserDTO(null, "john", "password123", "USER");
        assertThrows(MissingFieldsException.class, () -> userService.updateUser(dto));
    }

    @Test
    void updateUser_shouldThrow_whenUserNotFound() {
        UserDTO dto = buildUserDTO(99L, "john", "password123", "USER");
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(dto));
    }

    @Test
    void updateUser_shouldThrow_whenDuplicateUsername() {
        UserDTO dto = buildUserDTO(1L, "jane", "password123", "USER");
        User existingJane = buildUser(2L, "jane", "encoded", "USER"); // different user owns "jane"
        User existingJohn = buildUser(1L, "john", "encoded", "USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingJohn));
        when(userRepository.findByUsername("jane")).thenReturn(Optional.of(existingJane));

        assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(dto));
    }

    @Test
    void updateUser_shouldAllow_whenUpdatingOwnUsername() {
        UserDTO dto = buildUserDTO(1L, "john", "password123", "USER");
        User existing = buildUser(1L, "john", "encoded", "USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(existing)); // same user
        when(userRepository.save(any(User.class))).thenReturn(existing);

        assertDoesNotThrow(() -> userService.updateUser(dto));
    }


    @Test
    void deleteUser_shouldDeleteAndReturnUserDTO() {
        UserDTO dto = buildUserDTO(1L, null, null, null);
        User existing = buildUser(1L, "john", "encoded", "USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));

        UserDTO result = userService.deleteUser(dto);

        assertEquals(1L, result.getUserId());
        verify(userRepository).delete(any(User.class));
    }

    @Test
    void deleteUser_shouldThrow_whenIdNull() {
        UserDTO dto = buildUserDTO(null, null, null, null);
        assertThrows(MissingFieldsException.class, () -> userService.deleteUser(dto));
    }

    @Test
    void deleteUser_shouldThrow_whenUserNotFound() {
        UserDTO dto = buildUserDTO(99L, null, null, null);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(dto));
    }


    @Test
    void login_shouldReturnToken_whenCredentialsValid() {
        UserDTO dto = buildUserDTO(null, "john", "password123", null);
        User dbUser = buildUser(1L, "john", "encoded", "USER");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(dbUser));
        when(passwordEncoder.matches("password123", "encoded")).thenReturn(true);
        when(jwtService.generateToken("john", "USER")).thenReturn("jwt-token");

        String token = userService.login(dto);

        assertEquals("jwt-token", token);
    }

    @Test
    void login_shouldThrow_whenUserNotFound() {
        UserDTO dto = buildUserDTO(null, "unknown", "password123", null);
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.login(dto));
    }

    @Test
    void login_shouldThrow_whenPasswordIncorrect() {
        UserDTO dto = buildUserDTO(null, "john", "wrongpassword", null);
        User dbUser = buildUser(1L, "john", "encoded", "USER");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(dbUser));
        when(passwordEncoder.matches("wrongpassword", "encoded")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userService.login(dto));
    }


    private User buildUser(Long id, String username, String password, String role) {
        User user = new User();
        user.setUserId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }

    private UserDTO buildUserDTO(Long id, String username, String password, String role) {
        UserDTO dto = new UserDTO();
        dto.setUserId(id);
        dto.setUsername(username);
        dto.setPassword(password);
        dto.setRole(role);
        return dto;
    }
}