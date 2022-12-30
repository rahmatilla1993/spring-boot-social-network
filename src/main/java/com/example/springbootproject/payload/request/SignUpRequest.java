package com.example.springbootproject.payload.request;

import com.example.springbootproject.annotations.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@PasswordMatches
public class SignUpRequest {

    @Email(message = "It should have email format")
    @NotEmpty(message = "User email is required")
    private String email;

    @NotEmpty(message = "Please enter your name")
    private String firstName;

    @NotEmpty(message = "Please enter your lastname")
    private String lastName;
    @NotEmpty(message = "Please enter your username")
    private String username;

    @NotEmpty(message = "Password is required")
    private String password;

    private String confirmPassword;
}
