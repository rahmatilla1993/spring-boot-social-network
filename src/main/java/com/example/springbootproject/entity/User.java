package com.example.springbootproject.entity;

import com.example.springbootproject.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "text")
    private String bio;
    @Column(nullable = false)
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Role> roles;

    @OneToMany(cascade = CascadeType.ALL,
               fetch = FetchType.LAZY,mappedBy = "user",
               orphanRemoval = true)
    private List<Post> posts;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
}
