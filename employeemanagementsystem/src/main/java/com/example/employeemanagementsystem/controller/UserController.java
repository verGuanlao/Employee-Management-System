package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.component.MessageHelper;
import com.example.employeemanagementsystem.dto.ApiResponse;
import com.example.employeemanagementsystem.dto.UserDTO;
import com.example.employeemanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(
            @RequestParam(required = false) String username, Pageable pageable) {
        Page<UserDTO> users = (username != null && !username.isBlank())
                ? userService.findByUsernameContainingIgnoreCase(username, pageable)
                : userService.findAllUsers(pageable);
        ApiResponse<Page<UserDTO>> response = ApiResponse.<Page<UserDTO>>builder()
                .status("success")
                .message(MessageHelper.get("success.user.fetched"))
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@RequestBody UserDTO userDTO) {
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .status("success")
                .message(MessageHelper.get("success.user.updated"))
                .data(userService.updateUser(userDTO))
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> deleteUser(@RequestBody UserDTO userDTO) {
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .status("success")
                .message(MessageHelper.get("success.user.deleted"))
                .data(userService.deleteUser(userDTO))
                .build();
        return ResponseEntity.ok(response);
    }
}