package com.example.springbootproject.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {

    @NotEmpty(message = "Username cannot be empty")
    private String email;

    @NotEmpty(message = "Username cannot be empty")
    private String password;
}
