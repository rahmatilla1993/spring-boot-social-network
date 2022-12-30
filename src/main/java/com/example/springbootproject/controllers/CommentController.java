package com.example.springbootproject.controllers;

import com.example.springbootproject.dto.CommentDto;
import com.example.springbootproject.payload.ApiResponse;
import com.example.springbootproject.service.CommentService;
import com.example.springbootproject.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentController {

    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public CommentController(CommentService commentService, ModelMapper modelMapper,
                             ResponseErrorValidation responseErrorValidation) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/{postId}/create")
    public HttpEntity<?> saveComment(@PathVariable("postId") String postId,
                                     @Valid @RequestBody CommentDto commentDto,
                                     BindingResult bindingResult, Principal principal) {
        var errors = responseErrorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        var comment = commentService.saveComment(Long.parseLong(postId), commentDto, principal);
        var newComment = modelMapper.map(comment, CommentDto.class);
        return ResponseEntity.ok(newComment);
    }

    @GetMapping("/{postId}/all")
    public HttpEntity<?> getAllCommentsForPost(@PathVariable("postId") String postId) {
        List<CommentDto> comments = commentService
                .getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .toList();
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}/delete")
    public HttpEntity<?> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return ResponseEntity.ok(new ApiResponse("Comment deleted", true));
    }
}
