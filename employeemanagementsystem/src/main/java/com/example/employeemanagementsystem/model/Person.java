package com.example.employeemanagementsystem.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class Person {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull
    @PastOrPresent
    private LocalDate birthDate;
}
