package com.example.springbootproject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    @NotEmpty
    private String message;
    private String username;
}
