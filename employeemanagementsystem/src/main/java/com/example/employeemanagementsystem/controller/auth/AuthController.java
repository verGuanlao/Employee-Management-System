package com.example.employeemanagementsystem.controller.auth;

import com.example.employeemanagementsystem.dto.ApiResponse;
import com.example.employeemanagementsystem.dto.UserDTO;
import com.example.employeemanagementsystem.model.User;
import com.example.employeemanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDTO>> login(@RequestBody UserDTO request) {
        try {
            String token = userService.login(request);
            User validatedUser = userService.getByUsername(request.getUsername());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            ApiResponse<UserDTO> apiResponse = ApiResponse.<UserDTO>builder()
                    .status("success")
                    .message("Login successfully")
                    .data(new UserDTO(validatedUser.getUsername(), "password",validatedUser.getRole()))
                    .build();

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(apiResponse);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            ApiResponse<UserDTO> errorResponse = ApiResponse.<UserDTO>builder()
                    .status("error")
                    .message(e.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            ApiResponse<UserDTO> errorResponse = ApiResponse.<UserDTO>builder()
                    .status("error")
                    .message("An unexpected error occurred")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}