package com.example.employeemanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Role cannot be blank")
    private String role;

}
