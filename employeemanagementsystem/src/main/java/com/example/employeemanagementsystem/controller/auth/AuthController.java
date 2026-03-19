package com.example.employeemanagementsystem.controller.auth;

import com.example.employeemanagementsystem.dto.ApiResponse;
import com.example.employeemanagementsystem.dto.UserDTO;
import com.example.employeemanagementsystem.model.User;
import com.example.employeemanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO registeredUser = userService.registerUser(userDTO);
        ApiResponse<UserDTO> apiResponse = ApiResponse.<UserDTO>builder()
                .status("success")
                .message("Login successfully")
                .data(registeredUser)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDTO>> login(@RequestBody UserDTO request) {
        String token = userService.login(request);
        User validatedUser = userService.getByUsername(request.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        ApiResponse<UserDTO> apiResponse = ApiResponse.<UserDTO>builder()
                .status("success")
                .message("Login successfully")
                .data(new UserDTO(validatedUser.getUserId(), validatedUser.getUsername(), "",validatedUser.getRole()))
                .build();

        return ResponseEntity.ok()
                .headers(headers)
                .body(apiResponse);
    }
}