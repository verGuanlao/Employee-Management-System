package com.example.employeemanagementsystem;

import com.example.employeemanagementsystem.dto.UserDTO;
import com.example.employeemanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserSeeder {

    @Autowired
    private UserService userService;

    @Bean
    CommandLineRunner initUsers(UserService userService) {
        return args -> {
            List<UserDTO> users = List.of(
                    createUser("admin",  "harderpassword", "ADMIN"),
                    createUser("user",   "password",       "USER"),
                    createUser("john",   "password123",    "USER"),
                    createUser("jane",   "password123",    "USER"),
                    createUser("bob",    "password123",    "USER"),
                    createUser("alice",  "password123",    "ADMIN"),
                    createUser("charlie","password123",    "USER")
            );

            for (UserDTO user : users) {
                try {
                    userService.registerUser(user);
                } catch (Exception e) {
                    System.out.println("ℹ️ User already exists: " + user.getUsername());
                }
            }
            System.out.println("✅ Default users seeded");
        };
    }

    private UserDTO createUser(String username, String password, String role) {
        UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}