package com.example.springbootproject.repository;

import com.example.springbootproject.entity.Post;
import com.example.springbootproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserOrderByCreatedDateDesc(User user);
    List<Post> findAllByOrderByCreatedDateDesc();
    Optional<Post> findByIdAndUser(Long id, User user);
}
