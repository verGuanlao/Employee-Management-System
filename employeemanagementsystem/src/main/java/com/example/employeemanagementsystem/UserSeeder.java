package com.example.employeemanagementsystem;

import com.example.employeemanagementsystem.dto.UserDTO;
import com.example.employeemanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserSeeder {

    @Autowired
    private UserService userService;

    @Bean
    CommandLineRunner initUsers(UserService userService) {
        return args -> {
            try {
                // Example: create an admin user
//                UserDTO admin = new UserDTO();
//                admin.setUsername("admin");
//                admin.setPassword("harderpassword"); // will be hashed by registerUser
//                admin.setRole("ADMIN");
//
//                userService.registerUser(admin);

                // Example: create a regular user
                UserDTO user = new UserDTO();
//                user.setUsername("user");
//                user.setPassword("password");
//                user.setRole("USER");
//
//                userService.registerUser(user);

                user.setUsername("user2");
                user.setPassword("password");
                user.setRole("ADMIN");

                userService.registerUser(user);

                System.out.println("✅ Default users seeded");
            } catch (Exception e) {
                // If users already exist, ignore
                System.out.println("ℹ️ Users already seeded: " + e.getMessage());
            }
        };
    }
}