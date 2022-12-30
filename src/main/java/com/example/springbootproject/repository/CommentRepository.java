package com.example.springbootproject.repository;

import com.example.springbootproject.entity.Comment;
import com.example.springbootproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
    Comment findByIdAndUserId(Long id, UUID userId);
}
