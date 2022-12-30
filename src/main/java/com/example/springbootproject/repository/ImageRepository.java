package com.example.springbootproject.repository;

import com.example.springbootproject.entity.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel,Long> {

    Optional<ImageModel> findByUserId(UUID userId);
    Optional<ImageModel> findByPostId(Long postId);
}
