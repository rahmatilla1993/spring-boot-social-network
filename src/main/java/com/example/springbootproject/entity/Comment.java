package com.example.springbootproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Post post;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private UUID userId;
    @Column(columnDefinition = "text",nullable = false)
    private String message;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;
}
