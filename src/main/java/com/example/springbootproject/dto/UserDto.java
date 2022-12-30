package com.example.springbootproject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotEmpty(message = "Firstname cannot be empty")
    private String firstName;
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotEmpty(message = "Lastname cannot be empty")
    private String lastName;
    private String bio;
}
