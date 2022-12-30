package com.example.springbootproject.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
     private Long id;
     private String title;
     private String text;
     private String location;
     private String username;
     private Integer likes;
     private Set<String> usersLiked;
}
